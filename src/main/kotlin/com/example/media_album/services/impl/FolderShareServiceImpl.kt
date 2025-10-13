package com.example.media_album.services.impl

import com.example.media_album.codegen.types.MediaShareInput
import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.models.documents.MediaShareDocument
import com.example.media_album.models.dtos.input.FolderShareInput
import com.example.media_album.repositories.FolderRepository
import com.example.media_album.repositories.FolderShareRepository
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.FolderService
import com.example.media_album.services.FolderShareService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class FolderShareServiceImpl(repo : FolderShareRepository,
                             private val userRepo: UserRepository,
                             private val folderRepo: FolderRepository
): CommonServiceImpl<FolderShareDocument, ObjectId, FolderShareRepository>(repo),
    FolderShareService {
    override fun updateShareFolder(folderShareDocument: FolderShareInput): FolderShareDocument? {
        val id = folderShareDocument.id ?: throw IllegalArgumentException("Share ID is required!")

        val existingShare = repo.findById(ObjectId(id))
            .orElseThrow { RuntimeException("Folder share not found") }

        // Lấy ra các document tham chiếu
        val folder = folderRepo.findById(ObjectId(folderShareDocument.folder))
            .orElseThrow { RuntimeException("Folder not found") }

        val sharedWith = userRepo.findById(ObjectId(folderShareDocument.sharedWith))
            .orElseThrow { RuntimeException("Shared user not found") }

        val sharedBy = userRepo.findById(ObjectId(folderShareDocument.sharedBy))
            .orElseThrow { RuntimeException("Owner not found") }

        // Tạo bản cập nhật mới
        val updatedShare = existingShare.copy(
            folder = folder,
            sharedWith = sharedWith,
            sharedBy = sharedBy,
            permission = folderShareDocument.permission,
            updatedAt = Instant.now()
        )

        return repo.save(updatedShare)
    }

    override fun createFolderShare(folderShareDocument: FolderShareInput): FolderShareDocument? {
        val folder = folderRepo.findById(ObjectId(folderShareDocument.id))
            .orElseThrow { RuntimeException("Folder not found") }

        val sharedWith = userRepo.findById(ObjectId(folderShareDocument.sharedWith))
            .orElseThrow { RuntimeException("Folder not found") }

        val sharedBy = userRepo.findById(ObjectId(folderShareDocument.sharedBy))
            .orElseThrow { RuntimeException("Folder not found") }

        val share = FolderShareDocument(
            folder = folder,
            sharedWith = sharedWith,
            sharedBy = sharedBy,
            permission = folderShareDocument.permission,
        )

        return repo.save(share)
    }
}