package com.example.media_album.services

import com.example.media_album.models.documents.MediaDocument
import org.bson.types.ObjectId
import org.springframework.web.multipart.MultipartFile

interface MediaService : CommonService<MediaDocument, ObjectId> {
    fun uploadNewMedia(
        file: MultipartFile,
        userId: ObjectId,
        folderId: ObjectId?
    ): MediaDocument

    fun findUnorganizedMediaByUserId(userId: ObjectId): List<MediaDocument>

    fun findMediaByFolderId(folderId: ObjectId): List<MediaDocument>

    fun findByFilename(mediaName: String): MediaDocument?

    fun findByUserIdAndIsDeletedTrue(userId: ObjectId) : List<MediaDocument>

    fun getAllByUserId(userId: ObjectId) : List<MediaDocument>
}