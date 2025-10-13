package com.example.media_album.services.impl

import com.example.media_album.codegen.types.PermissionInput
import com.example.media_album.models.documents.PermissionDocument
import com.example.media_album.repositories.PermissionRepository
import com.example.media_album.services.PermissionService
import com.example.media_album.utils.getByIdOrThrow
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class PermissionServiceImpl(repo : PermissionRepository) : CommonServiceImpl<PermissionDocument, ObjectId, PermissionRepository>(repo),
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
}