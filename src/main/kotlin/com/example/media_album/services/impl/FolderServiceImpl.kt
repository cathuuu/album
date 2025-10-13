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

        val parentFolder = folderDocument.parentFolderId?.let { repo.findById(ObjectId(it)).orElse(null) }

        val updatedFolder = existingFolder.copy(
            name = folderDocument.name!!,
            parentFolder = parentFolder,
            user = user,
            coverUrl = folderDocument.coverUrl,
            isShared = folderDocument.isShared,
            updatedAt = Instant.now()
        )


        return repo.save(updatedFolder)
    }
    override fun findRootFoldersByOwnerId(ownerId: ObjectId): List<FolderDocument> {
        return repo.findByUserIdAndParentFolderIsNull(ownerId)
    }

    override fun findSubFoldersByParentId(parentId: ObjectId): List<FolderDocument> {
        return repo.findByParentFolderId(parentId)
    }

    override fun saveFolder(folderDocument: FolderInput?): FolderDocument {
        val user = userRepository.findById(ObjectId(folderDocument?.userId))
            .orElseThrow { RuntimeException("User not found") }

        val parentFolder = folderDocument?.parentFolderId?.let { repo.findById(ObjectId(it)).orElse(null) }

        val newFolder = FolderDocument(
            user = user,
            name = folderDocument?.name!!,
            parentFolder = parentFolder,
            coverUrl = folderDocument.coverUrl,
            isShared = folderDocument.isShared,
            updatedAt = Instant.now()
        )

        return repo.save(newFolder)
    }

    override fun findByFolderName(folderName: String): List<FolderDocument?> {
        return repo.findByFolderName(folderName)
    }

    override fun findByUserIdAndIsDeletedTrue(userId: ObjectId): List<FolderDocument> {
        return repo.findByUserIdAndIsDeletedTrue(userId)
    }
}