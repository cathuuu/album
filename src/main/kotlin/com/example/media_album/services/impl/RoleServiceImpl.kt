package com.example.media_album.services.impl

import com.example.media_album.models.documents.RoleDocument
import com.example.media_album.models.dtos.input.RoleInput
import com.example.media_album.repositories.PermissionRepository
import com.example.media_album.repositories.RoleRepository
import com.example.media_album.services.RoleService
import com.example.media_album.utils.getByIdOrThrow
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class RoleServiceImpl(repo : RoleRepository,
    private val permissionRepository: PermissionRepository) : CommonServiceImpl<RoleDocument, ObjectId, RoleRepository>(repo),
    RoleService {
    override fun updateRole(roleDocument: RoleInput): RoleDocument {
        val id = roleDocument.id ?: throw IllegalArgumentException("Role ID is required!")

        val existingRole = repo.findById(ObjectId(id))
            .orElseThrow { RuntimeException("Role not found") }

        // Lấy danh sách permission từ DB nếu có
        val permissions = roleDocument.permissionIds ?: existingRole.permissions


        // Cập nhật thông tin
        val updatedRole = existingRole.copy(
            roleName = roleDocument.roleName,
            permissions = permissions,
            updatedAt = Instant.now()
        )

        return repo.save(updatedRole)
    }

    override fun createRole(roleDocument: RoleInput): RoleDocument {


        val role = RoleDocument(
            roleName = roleDocument.roleName,
            permissions = roleDocument.permissionIds ?: emptyList(),
        )
        return repo.save(role)
    }
}