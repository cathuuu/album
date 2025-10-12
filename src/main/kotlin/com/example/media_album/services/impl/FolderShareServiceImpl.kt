package com.example.media_album.services.impl

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.repositories.FolderRepository
import com.example.media_album.repositories.FolderShareRepository
import com.example.media_album.services.FolderService
import com.example.media_album.services.FolderShareService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class FolderShareServiceImpl(repo : FolderShareRepository): CommonServiceImpl<FolderShareDocument, ObjectId, FolderShareRepository>(repo),
    FolderShareService {
    override fun updateShareFolder(folderShareDocument: FolderShareDocument): FolderShareDocument? {
        val id = folderShareDocument.id ?: throw IllegalArgumentException("Folder ID is required!")

        val existingFolder = repo.findById(id)
            .orElseThrow { RuntimeException("Folder not found") }

        // Cập nhật thông tin
        val updatedFolder = existingFolder.copy(
            folder = folderShareDocument.folder,
            sharedWith = folderShareDocument.sharedWith,
            sharedBy = folderShareDocument.sharedBy,
            permission = folderShareDocument.permission,
            updatedAt = Instant.now()
        )
        return repo.save(updatedFolder)
    }
}