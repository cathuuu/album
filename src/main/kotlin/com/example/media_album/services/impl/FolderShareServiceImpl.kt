package com.example.media_album.services.impl

import com.example.media_album.enums.PermissionType
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
class FolderShareServiceImpl(
    repo: FolderShareRepository,
    private val userRepo: UserRepository,
    private val folderRepo: FolderRepository,
    private val permissionService: PermissionService,
) : CommonServiceImpl<FolderShareDocument, ObjectId, FolderShareRepository>(repo),
    FolderShareService {

    // ==================== UPDATE SHARE ====================
    override fun updateShareFolder(folderShareDocument: FolderShareInput): FolderShareDocument {
        val id = folderShareDocument.id ?: throw IllegalArgumentException("Share ID is required!")
        val folderId = folderShareDocument.folderId?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("folderId cannot be null")
        val sharedById = folderShareDocument.sharedById?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("sharedById cannot be null")

        // --- Kiểm tra quyền người sửa ---
        val canEdit = permissionService.hasFolderInheritedPermission(
            sharedById, folderId, PermissionType.EDIT.value
        )
        if (!canEdit) throw AccessDeniedException("Bạn không có quyền chỉnh sửa chia sẻ này.")

        val existingShare = repo.findById(ObjectId(id))
            .orElseThrow { RuntimeException("Folder share not found") }

        val folder = folderRepo.findById(folderId)
            .orElseThrow { RuntimeException("Folder not found") }

        val sharedWith = userRepo.findById(ObjectId(folderShareDocument.sharedWithId))
            .orElseThrow { RuntimeException("Shared user not found") }

        val updatedShare = existingShare.copy(
            folder = folder.id!!,
            sharedWith = sharedWith.id!!,
            sharedBy = sharedById,
            permission = folderShareDocument.permission ?: existingShare.permission,
            updatedAt = Instant.now()
        )

        return repo.save(updatedShare)
    }

    // ==================== CREATE SHARE ====================
    override fun createFolderShare(folderShareDocument: FolderShareInput): FolderShareDocument {
        val folderId = folderShareDocument.folderId?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("folderId cannot be null")
        val sharedById = folderShareDocument.sharedById?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("sharedById cannot be null")
        val sharedWithId = folderShareDocument.sharedWithId?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("sharedWithId cannot be null")

        val folder = folderRepo.findById(folderId)
            .orElseThrow { RuntimeException("Folder not found") }

        // 🔹 Nếu người chia sẻ là chủ sở hữu → luôn có quyền SHARE
        val isOwner = folder.ownerId == sharedById
        val admin = permissionService.hasFolderInheritedPermission(sharedById, folderId, PermissionType.ALL.value)
        if (!isOwner && !admin &&
            !permissionService.hasFolderInheritedPermission(sharedById, folderId, PermissionType.SHARE.value)
        ) {
            throw AccessDeniedException("Bạn không có quyền chia sẻ thư mục này.")
        }

        val sharedWith = userRepo.findById(sharedWithId)
            .orElseThrow { RuntimeException("Shared user not found") }

        val share = FolderShareDocument(
            folder = folder.id!!,
            sharedWith = sharedWith.id!!,
            sharedBy = sharedById,
            permission = folderShareDocument.permission ?: listOf(PermissionType.VIEW.value),
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        val saved = repo.save(share)

        // 🔹 Tự động áp dụng kế thừa cho folder con (nếu chưa có share riêng)
        permissionService.applyInheritedPermission(folder.id!!, sharedById, sharedWithId, share.permission)

        return saved
    }

    // ==================== FIND ====================
    override fun findByShareWithUserFullName(userName: String): List<FolderShareDocument> =
        repo.findBySharedWithName(userName)

    override fun findByShareByUserFullName(userName: String): List<FolderShareDocument> =
        repo.findBySharedByName(userName)
}
