package com.example.media_album.services

import com.example.media_album.models.documents.PermissionDocument
import com.netflix.graphql.dgs.InputArgument
import org.bson.types.ObjectId

interface PermissionService: CommonService<PermissionDocument, ObjectId> {
    fun updatePermission(permissionDocument: PermissionDocument): PermissionDocument
}