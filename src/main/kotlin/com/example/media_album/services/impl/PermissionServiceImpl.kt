package com.example.media_album.services.impl

import com.example.media_album.codegen.types.PermissionInput
import com.example.media_album.models.documents.PermissionDocument
import com.example.media_album.repositories.FolderShareRepository
import com.example.media_album.repositories.MediaRepository
import com.example.media_album.repositories.MediaShareRepository
import com.example.media_album.repositories.PermissionRepository
import com.example.media_album.repositories.RoleRepository
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.PermissionService
import com.example.media_album.utils.getByIdOrThrow
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class PermissionServiceImpl(repo : PermissionRepository,
                            private val userRepository: UserRepository,
                            private val roleRepository: RoleRepository,
                            private val folderShareRepository: FolderShareRepository,
                            private val mediaShareRepository: MediaShareRepository,
                            private val mediaRepository: MediaRepository
) : CommonServiceImpl<PermissionDocument, ObjectId, PermissionRepository>(repo),
    PermissionService {
    override fun updatePermission(permissionDocument: PermissionInput): PermissionDocument {
        val id = permissionDocument.id ?: throw IllegalArgumentException("Permission ID is required!")

        val existing = repo.getByIdOrThrow(id, "Permission")

        val updated = existing.copy(
            permissionName = permissionDocument.permissionName,
            updatedAt = Instant.now()
        )

        return repo.save(updated)
    }

    override fun createPermission(permissionDocument: PermissionInput): PermissionDocument? {
        val newPermission = PermissionDocument(
            permissionName = permissionDocument.permissionName
        )
        return repo.save(newPermission)
    }

    override fun checkPermission(
        userId: ObjectId,
        mediaId: ObjectId,
        permission: String
    ): Boolean {
        // ---  Quyền trực tiếp theo user ---
        val userShare = mediaShareRepository.findByMediaAndSharedWith(mediaId, userId)
        if (userShare?.permission?.contains(permission) == true) return true

        // --- kiểm tra quyền kế thừa từ folder ---
        val media = mediaRepository.findById(mediaId).orElse(null) ?: return false
        val folderId = media.folder ?: return false
        val folderShare = folderShareRepository.findByFolderAndSharedWith(folderId, userId)
        if (folderShare?.permission?.contains(permission) == true) return true

        // --- Kiểm tra quyền theo role ---
        val user = userRepository.findById(userId).orElse(null) ?: return false
        val roles = roleRepository.findAllById(user.roleIds)
        val permissionIds = roles.flatMap { it.permissions }.distinct()
        val permissions = repo.findAllById(permissionIds).toList()

        // So sánh theo tên quyền
        val hasPermission = permissions.any { it.permissionName == permission || it.permissionName == "ALL" }

        return hasPermission
    }
}