package com.example.media_album.services.impl

import com.example.media_album.models.documents.MediaShareDocument
import com.example.media_album.models.documents.PermissionDocument
import com.example.media_album.repositories.MediaShareRepository
import com.example.media_album.repositories.PermissionRepository
import com.example.media_album.services.MediaShareService
import com.example.media_album.services.PermissionService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class PermissionServiceImpl(repo : PermissionRepository) : CommonServiceImpl<PermissionDocument, ObjectId, PermissionRepository>(repo),
    PermissionService {
    override fun updatePermission(permissionDocument: PermissionDocument): PermissionDocument {
        val id = permissionDocument.id ?: throw IllegalArgumentException("Folder ID is required!")

        val existingFolder = repo.findById(id)
            .orElseThrow { RuntimeException("Folder not found") }

        // Cập nhật thông tin
        val updatedFolder = existingFolder.copy(
            permissionName = permissionDocument.permissionName,
            updatedAt = Instant.now()
        )
        return repo.save(updatedFolder)
    }
}