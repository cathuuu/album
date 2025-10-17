package com.example.media_album.services

import com.example.media_album.codegen.types.PermissionInput
import com.example.media_album.models.documents.PermissionDocument
import org.bson.types.ObjectId

interface PermissionService: CommonService<PermissionDocument, ObjectId> {
    fun updatePermission(permissionDocument: PermissionInput): PermissionDocument

    fun createPermission(permissionDocument: PermissionInput): PermissionDocument?

    fun checkPermission(userId: ObjectId, mediaId: ObjectId, requiredPermission: String): Boolean

    fun revokeInheritedPermission(folderId: ObjectId, sharedWith: ObjectId, visited: MutableSet<ObjectId> = mutableSetOf())

    fun applyInheritedPermission(
        folderId: ObjectId,
        sharedBy: ObjectId,
        sharedWith: ObjectId,
        permissions: List<String>,
        visited: MutableSet<ObjectId> = mutableSetOf()
    )

    fun hasFolderInheritedPermission(
        userId: ObjectId,
        folderId: ObjectId?,
        permission: String
    ): Boolean

    fun inheritPermissionsFromFolder(parentId: ObjectId, sharedBy: ObjectId, sharedWith: ObjectId, permissions: List<String>)
}