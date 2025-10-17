package com.example.media_album.services.impl

import com.example.media_album.enums.MediaType
import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.MediaDocument
import com.example.media_album.models.dtos.input.FolderInput
import com.example.media_album.repositories.FolderRepository
import com.example.media_album.repositories.FolderShareRepository
import com.example.media_album.repositories.MediaRepository
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.FolderService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.Instant
import java.util.UUID

@Service
class FolderServiceImpl(
    repo: FolderRepository,
    private val userRepository: UserRepository,
    private val mediaRepository: MediaRepository
) : CommonServiceImpl<FolderDocument, ObjectId, FolderRepository>(repo), FolderService {
    override fun updateFolder(folderDocument: FolderInput): FolderDocument? {
        val existingFolder = repo.findById(ObjectId(folderDocument.id))
            .orElseThrow { RuntimeException("Folder not found") }

        val user = userRepository.findById(ObjectId(folderDocument.userId))
            .orElseThrow { RuntimeException("User not found") }

        val parentFolder = folderDocument.parentId?.let {
            repo.findById(ObjectId(it)).orElse(null)
        }

        //  Tạo lại path
        val newPath = if (parentFolder != null)
            "${parentFolder.path}/${folderDocument.name}"
        else
            "/${folderDocument.name}"

        //  Cập nhật folder
        val updatedFolder = existingFolder.copy(
            name = folderDocument.name!!,
            parentId = parentFolder?.id,
            ownerId = user.id,
            coverUrl = folderDocument.coverUrl,
            isShared = folderDocument.isShared,
            path = newPath,
            updatedAt = Instant.now()
        )

        return repo.save(updatedFolder)
    }
    override fun findRootFoldersByOwnerId(ownerId: ObjectId): List<FolderDocument> {
        return repo.findByOwnerIdAndParentIdIsNull(ownerId)
    }

    override fun findSubFoldersByParentId(parentId: ObjectId): List<FolderDocument> {
        return repo.findByParentId(parentId)
    }


    @Value("\${media.upload.base-path}")
    private lateinit var basePath: String
    override fun saveFolder(folderDocument: FolderInput?): FolderDocument {
        if (folderDocument == null) {
            throw IllegalArgumentException("Folder input cannot be null")
        }

        // 🔹 1. Tìm user theo ID
        val user = userRepository.findById(ObjectId(folderDocument.userId))
            .orElseThrow { RuntimeException("User not found") }

        // 🔹 2. Tìm folder cha (nếu có)
        val parentFolder = folderDocument.parentId?.let {
            repo.findById(ObjectId(it)).orElse(null)
        }

        // 🔹 3. Sinh đường dẫn logic trong DB
        val logicalPath = if (parentFolder != null)
            "${parentFolder.path}/${folderDocument.name}"
        else
            "${user.id}/${folderDocument.name}"

        // 🔹 4. Tạo đường dẫn vật lý (filesystem)
        val physicalPath = "$basePath/$logicalPath"
        val folderFile = File(physicalPath)

        if (!folderFile.exists()) {
            val created = folderFile.mkdirs()
            if (!created) {
                throw RuntimeException("Failed to create physical folder: $physicalPath")
            }
        }

        // 🔹 5. Tạo document mới
        val newFolder = FolderDocument(
            ownerId = user.id,
            name = folderDocument.name,
            parentId = parentFolder?.id,
            coverUrl = folderDocument.coverUrl,
            isShared = folderDocument.isShared,
            path = logicalPath,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        // 🔹 6. Lưu vào MongoDB
        return repo.save(newFolder)

    }

    override fun findByFolderName(folderName: String): List<FolderDocument?> {
        return repo.findByFolderName(folderName)
    }

    override fun findByUserIdAndIsDeletedTrue(userId: ObjectId): List<FolderDocument> {
        return repo.findByOwnerIdAndIsDeletedTrue(userId)
    }

    }