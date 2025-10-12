package com.example.media_album.services.impl

import com.example.media_album.models.documents.MediaDocument
import com.example.media_album.models.documents.PhotoMeta
import com.example.media_album.models.documents.VideoMeta
import com.example.media_album.repositories.FolderRepository
import com.example.media_album.repositories.MediaRepository
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.MediaService
import jakarta.annotation.PostConstruct
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.UUID

@Service
class MediaServiceImpl(repo: MediaRepository,
    private val userRepository: UserRepository,
    private val folderRepository: FolderRepository) : CommonServiceImpl<MediaDocument, ObjectId, MediaRepository>(repo), MediaService {
    @Value("\${app.upload.dir}")
    private lateinit var uploadDirString: String

    private lateinit var UPLOAD_DIR: Path

    @PostConstruct
    private fun init() {
        UPLOAD_DIR = Path.of(uploadDirString)
        try {
            if (Files.notExists(UPLOAD_DIR)) {
                Files.createDirectories(UPLOAD_DIR)
                println("✅ Đã tạo thư mục upload tại: $UPLOAD_DIR")
            }
        } catch (e: Exception) {
            throw RuntimeException("❌ Không thể tạo thư mục upload: $UPLOAD_DIR", e)
        }
    }

    override fun uploadNewMedia(
        file: MultipartFile,
        userId: ObjectId,
        folderId: ObjectId?
    ): MediaDocument {
        // 1. Lấy document liên quan
        val userDocument = userRepository.findById(userId)
            .orElseThrow { RuntimeException("User not found") }

        val folderDocument = folderId?.let {
            folderRepository.findById(it).orElse(null)
        }

        // 2. Đảm bảo thư mục tồn tại
        if (Files.notExists(UPLOAD_DIR)) {
            Files.createDirectories(UPLOAD_DIR)
        }

        // 3. Sinh tên file & lưu
        val uniqueFilename = UUID.randomUUID().toString() + "_" + file.originalFilename
        val targetLocation = UPLOAD_DIR.resolve(uniqueFilename)

        try {
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: IOException) {
            throw RuntimeException("Không thể lưu file vào $targetLocation", e)
        }

        val fileUrl = "/files/$uniqueFilename"
        val fileType = if (file.contentType?.startsWith("image") == true) "photo" else "video"

        // 4. Metadata
        val photoMeta = if (fileType == "photo")
            PhotoMeta(cameraModel = "DummyCamera", iso = 400, aperture = "f/2.8")
        else null

        val videoMeta = if (fileType == "video")
            VideoMeta(duration = 60, resolution = "1080p", frameRate = 30)
        else null

        // 5. Tạo document và lưu DB
        val mediaDocument = MediaDocument(
            user = userDocument,
            folder = folderDocument,
            type = fileType,
            url = fileUrl,
            filename = file.originalFilename ?: uniqueFilename,
            mimeType = file.contentType,
            size = file.size,
            photoMeta = photoMeta,
            videoMeta = videoMeta
        )

        return repo.save(mediaDocument)
    }

    override fun findUnorganizedMediaByUserId(userId: ObjectId): List<MediaDocument> {
        return repo.findByFolderIsNullAndUserId(userId)
    }

    override fun findMediaByFolderId(folderId: ObjectId): List<MediaDocument> {
        return repo.findByFolderId(folderId)
    }

    override fun findByFilename(mediaName: String): MediaDocument? {
        return repo.findByFilename(mediaName)
    }
}