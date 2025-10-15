package com.example.media_album.services.impl

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.dtos.input.FolderInput
import com.example.media_album.repositories.FolderRepository
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.FolderService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class FolderServiceImpl(repo: FolderRepository,
private val userRepository: UserRepository) : CommonServiceImpl<FolderDocument, ObjectId, FolderRepository>(repo), FolderService {
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
            userId = user.id,
            coverUrl = folderDocument.coverUrl,
            isShared = folderDocument.isShared,
            path = newPath,
            updatedAt = Instant.now()
        )

        return repo.save(updatedFolder)
    }
    override fun findRootFoldersByOwnerId(ownerId: ObjectId): List<FolderDocument> {
        return repo.findByUserIdAndParentIdIsNull(ownerId)
    }

    override fun findSubFoldersByParentId(parentId: ObjectId): List<FolderDocument> {
        return repo.findByParentId(parentId)
    }

    override fun saveFolder(folderDocument: FolderInput?): FolderDocument {
        if (folderDocument == null) {
            throw IllegalArgumentException("Folder input cannot be null")
        }

        //  Tìm user theo ID
        val user = userRepository.findById(ObjectId(folderDocument.userId))
            .orElseThrow { RuntimeException("User not found") }

        // Kiểm tra folder cha (nếu có)
        val parentFolder = folderDocument.parentId?.let {
            repo.findById(ObjectId(it)).orElse(null)
        }

        // Sinh đường dẫn path tự động
        val path = if (parentFolder != null)
            "${parentFolder.path}/${folderDocument.name}"
        else
            "/${folderDocument.name}"

        // tạo folder mới
        val newFolder = FolderDocument(
            userId = user.id,
            name = folderDocument.name,
            parentId = parentFolder?.id,
            coverUrl = folderDocument.coverUrl,
            isShared = folderDocument.isShared,
            path = path
        )

        // 5️⃣ Lưu vào Mongo
        return repo.save(newFolder)
    }

    override fun findByFolderName(folderName: String): List<FolderDocument?> {
        return repo.findByFolderName(folderName)
    }

    override fun findByUserIdAndIsDeletedTrue(userId: ObjectId): List<FolderDocument> {
        return repo.findByUserIdAndIsDeletedTrue(userId)
    }
}