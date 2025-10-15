package com.example.media_album.controllers

import com.example.media_album.models.documents.MediaDocument
import com.example.media_album.services.MediaService
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("api/v1/media")
class MediaController(
    private val mediaService: MediaService
) {
    @PostMapping("/upload")
    fun uploadMedia(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("userId") userId: String, // thay từ RequestAttribute → RequestParam
        @RequestParam(value = "folderId", required = false) folderId: String?
    ): ResponseEntity<*> {

        if (file.isEmpty) {
            return ResponseEntity("Please select a file to upload", HttpStatus.BAD_REQUEST)
        }

        try {
            val userObjectId = ObjectId(userId)
            val folderObjectId = if (!folderId.isNullOrBlank() && ObjectId.isValid(folderId)) {
                ObjectId(folderId)
            } else null

            val savedDocument = mediaService.uploadNewMedia(file, userObjectId, folderObjectId)

            return ResponseEntity(savedDocument, HttpStatus.CREATED)
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity("Failed to upload file: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @PostMapping("/update")
    fun updateMedia(@RequestParam mediaDocument: MediaDocument): ResponseEntity<MediaDocument> {
        val result = mediaService.save(mediaDocument)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{user_id}")
    fun getAllByUserId(@PathVariable("user_id") userId: ObjectId): ResponseEntity<List<MediaDocument>> {
        val result = mediaService.getAllByUserId(userId)
        return ResponseEntity.ok(result)
    }
}