package com.example.media_album.services

import com.example.media_album.models.documents.RoleDocument
import com.example.media_album.models.dtos.input.RoleInput
import org.bson.types.ObjectId

interface RoleService : CommonService<RoleDocument, ObjectId> {
    fun updateRole(roleDocument: RoleInput): RoleDocument

    fun createRole(roleDocument: RoleInput): RoleDocument
}