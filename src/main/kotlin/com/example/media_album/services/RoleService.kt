package com.example.media_album.services

import com.example.media_album.models.documents.RoleDocument
import org.bson.types.ObjectId

interface RoleService : CommonService<RoleDocument, ObjectId> {
    fun updateRole(roleDocument: RoleDocument): RoleDocument
}