package com.example.media_album.repositories

import com.example.media_album.models.documents.PermissionDocument
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
interface PermissionRepository : CommonRepository<PermissionDocument, ObjectId> {
    fun findAllById(userId: Long): List<PermissionDocument>
}