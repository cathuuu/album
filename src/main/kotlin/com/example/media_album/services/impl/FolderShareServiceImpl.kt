package com.example.media_album.services.impl

import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.models.dtos.input.FolderShareInput
import com.example.media_album.repositories.FolderRepository
import com.example.media_album.repositories.FolderShareRepository
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.FolderShareService
import com.example.media_album.services.PermissionService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.nio.file.AccessDeniedException
import java.time.Instant

@Service
class FolderShareServiceImpl(repo : FolderShareRepository,
                             private val userRepo: UserRepository,
                             private val folderRepo: FolderRepository,
                             private val permissionService: PermissionService,
): CommonServiceImpl<FolderShareDocument, ObjectId, FolderShareRepository>(repo),
    FolderShareService {
    override fun updateShareFolder(folderShareDocument: FolderShareInput): FolderShareDocument? {

        val userId = ObjectId(folderShareDocument.sharedById)
        val folderId = ObjectId(folderShareDocument.folderId)

        // --- Kiểm tra quyền ---
        if (!permissionService.checkPermission(userId, folderId, "EDIT")) {
            throw AccessDeniedException("You do not have permission to edit this media")
        }


        val id = folderShareDocument.id ?: throw IllegalArgumentException("Share ID is required!")

        val existingShare = repo.findById(ObjectId(id))
            .orElseThrow { RuntimeException("Folder share not found") }

        // Lấy ra các document tham chiếu
        val folder = folderRepo.findById(ObjectId(folderShareDocument.folderId))
            .orElseThrow { RuntimeException("Folder not found") }

        val sharedWith = userRepo.findById(ObjectId(folderShareDocument.sharedWithId))
            .orElseThrow { RuntimeException("Shared user not found") }

        val sharedBy = userRepo.findById(ObjectId(folderShareDocument.sharedById))
            .orElseThrow { RuntimeException("Owner not found") }

        // Tạo bản cập nhật mới
        val updatedShare = existingShare.copy(
            folder = folder.id!!,
            sharedWith = sharedWith.id!!,
            sharedBy = sharedBy.id!!,
            permission = folderShareDocument.permission,
            updatedAt = Instant.now()
        )

        return repo.save(updatedShare)
    }


    override fun findByShareWithUserFullName(userName: String): List<FolderShareDocument?> {
        return repo.findBySharedWithName(userName)
    }

    override fun findByShareByUserFullName(userName: String): List<FolderShareDocument?> {
        return repo.findBySharedByName(userName)
    }

    override fun createFolderShare(folderShareDocument: FolderShareInput): FolderShareDocument? {

//        val userId = ObjectId(folderShareDocument.sharedById)
//        val folderId = ObjectId(folderShareDocument.folderId)
//
//        // --- Kiểm tra quyền ---
//        if (!permissionService.checkPermission(userId, folderId, "WRITE")) {
//            throw AccessDeniedException("You do not have permission to share this media")
//        }

        val folderId = folderShareDocument.folderId?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("folderId cannot be null or empty")

        val sharedWithId = folderShareDocument.sharedWithId?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("sharedWithId cannot be null or empty")

        val sharedById = folderShareDocument.sharedById?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("sharedById cannot be null or empty")

        // 🧩 Kiểm tra tồn tại
        val folder = folderRepo.findById(folderId)
            .orElseThrow { RuntimeException("Folder not found") }

        val sharedWith = userRepo.findById(sharedWithId)
            .orElseThrow { RuntimeException("Shared user not found") }

        val sharedBy = userRepo.findById(sharedById)
            .orElseThrow { RuntimeException("Sharing user not found") }

        // 🧩 Tạo tài liệu chia sẻ
        val share = FolderShareDocument(
            folder = folder.id!!,
            sharedWith = sharedWith.id!!,
            sharedBy = sharedBy.id!!,
            permission = folderShareDocument.permission ?: emptyList(),
        )


        return repo.save(share)
    }
}