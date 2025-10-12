package com.example.media_album.services.impl

import com.example.media_album.models.documents.MediaShareDocument
import com.example.media_album.models.documents.RoleDocument
import com.example.media_album.repositories.MediaShareRepository
import com.example.media_album.repositories.RoleRepository
import com.example.media_album.services.MediaShareService
import com.example.media_album.services.RoleService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class RoleServiceImpl(repo : RoleRepository) : CommonServiceImpl<RoleDocument, ObjectId, RoleRepository>(repo),
    RoleService {
    override fun updateRole(roleDocument: RoleDocument): RoleDocument {
        val id = roleDocument.id ?: throw IllegalArgumentException("Folder ID is required!")

        val existingFolder = repo.findById(id)
            .orElseThrow { RuntimeException("Folder not found") }

        // Cập nhật thông tin
        val updatedFolder = existingFolder.copy(
            roleName = roleDocument.roleName,
            permissions = roleDocument.permissions,
            updatedAt = Instant.now()
        )
        return repo.save(updatedFolder)
    }
}