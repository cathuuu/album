package com.example.media_album.services.impl

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.repositories.FolderRepository
import com.example.media_album.services.FolderService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class FolderServiceImpl(repo: FolderRepository) : CommonServiceImpl<FolderDocument, ObjectId, FolderRepository>(repo), FolderService {
    override fun updateFolder(folderDocument: FolderDocument): FolderDocument? {
        val id = folderDocument.id ?: throw IllegalArgumentException("Folder ID is required!")

        val existingFolder = repo.findById(id)
            .orElseThrow { RuntimeException("Folder not found") }

        // Cập nhật thông tin
        val updatedFolder = existingFolder.copy(
            name = folderDocument.name,
            parentFolder = folderDocument.parentFolder,
            user = folderDocument.user,
            coverUrl = folderDocument.coverUrl,
            isShared = folderDocument.isShared,
            updatedAt = Instant.now() // Nên dùng Instant.now() thay vì giá trị từ input
        )

        return repo.save(updatedFolder)
    }

    override fun findRootFoldersByOwnerId(ownerId: ObjectId): List<FolderDocument> {
        return repo.findByUserIdAndParentFolderIsNull(ownerId)
    }

    override fun findSubFoldersByParentId(parentId: ObjectId): List<FolderDocument> {
        return repo.findByParentFolderId(parentId)
    }
}