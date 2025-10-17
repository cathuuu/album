package com.example.media_album.services.impl

import com.example.media_album.enums.MediaType
import com.example.media_album.models.documents.MediaDocument
import com.example.media_album.models.documents.PhotoMeta
import com.example.media_album.models.documents.VideoMeta
import com.example.media_album.repositories.FolderRepository
import com.example.media_album.repositories.MediaRepository
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.MediaService
import com.example.media_album.services.PermissionService
import com.example.media_album.utils.getByIdOrThrow
import jakarta.annotation.PostConstruct
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.time.Instant
import java.util.*

@Service
class MediaServiceImpl(
    repo: MediaRepository,
    private val userRepository: UserRepository,
    private val folderRepository: FolderRepository,
    private val permissionService: PermissionService
) : CommonServiceImpl<MediaDocument, ObjectId, MediaRepository>(repo),
    MediaService {

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
        val userDocument = userRepository.getByIdOrThrow(userId.toString(), "User")

        val folderDocument = folderId?.let {
            folderRepository.getByIdOrThrow(it.toString(), "Folder")
        }

        // 🔹 Kiểm tra quyền nếu upload vào folder
        if (folderId != null && !permissionService.checkPermission(userId, folderId, "EDIT")) {
            throw SecurityException("You do not have permission to upload to this folder.")
        }

        // 🔹 Tạo thư mục upload nếu chưa có
        if (Files.notExists(UPLOAD_DIR)) {
            Files.createDirectories(UPLOAD_DIR)
        }

        // 🔹 Sinh tên file và lưu
        val uniqueFilename = UUID.randomUUID().toString() + "_" + (file.originalFilename ?: "unknown")
        val targetLocation = UPLOAD_DIR.resolve(uniqueFilename)
        try {
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: IOException) {
            throw RuntimeException("Không thể lưu file vào $targetLocation", e)
        }

        val fileType = when {
            file.contentType?.startsWith("image") == true -> "photo"
            file.contentType?.startsWith("video") == true -> "video"
            else -> "unknown"
        }

        val photoMeta = if (fileType == "photo")
            PhotoMeta(cameraModel = "DummyCamera", iso = 400, aperture = "f/2.8")
        else null

        val videoMeta = if (fileType == "video")
            VideoMeta(duration = 60, resolution = "1080p", frameRate = 30)
        else null

        val mediaDocument = MediaDocument(
            user = userDocument.id!!,
            folder = folderDocument?.id,
            type = MediaType.OTHER,
            filename = file.originalFilename ?: uniqueFilename,
            storedFilename = uniqueFilename,
            mimeType = file.contentType,
            size = file.size,
            url = "/files/$uniqueFilename", // có thể tùy chỉnh build URL động
            photoMeta = photoMeta,
            videoMeta = videoMeta,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        val savedMedia = repo.save(mediaDocument)

        // 🔹 Kế thừa quyền từ folder nếu có
        folderId?.let {
            val sharedById = userDocument.id!!
            val sharedWithId = userDocument.id!! // hoặc người được chia sẻ, tùy logic bạn
            val inheritedPermissions = listOf("VIEW")

            permissionService.inheritPermissionsFromFolder(
                it,                // folder cha
                sharedById,        // người chia sẻ
                sharedWithId,      // người được chia sẻ
                inheritedPermissions // danh sách quyền
            )
        }


        return savedMedia
    }

    override fun findUnorganizedMediaByUserId(userId: ObjectId): List<MediaDocument> =
        repo.findByFolderIsNullAndUser(userId)

    override fun findMediaByFolderId(folderId: ObjectId): List<MediaDocument> =
        repo.findByFolder(folderId)

    override fun findByFilename(mediaName: String): MediaDocument? =
        repo.findByFilename(mediaName)

    override fun findByUserIdAndIsDeletedTrue(userId: ObjectId): List<MediaDocument> =
        repo.findByUserAndIsDeletedTrue(userId)

    override fun getAllByUserId(userId: ObjectId): List<MediaDocument> =
        repo.findAllByUser(userId)
}
