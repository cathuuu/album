package com.example.media_album.graphqls.mutation

import com.example.media_album.codegen.types.PermissionInput
import com.example.media_album.models.documents.PermissionDocument
import com.example.media_album.services.PermissionService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.bson.types.ObjectId

@DgsComponent
class PermissionMutation(
    private val permissionService: PermissionService,
) {
    @DgsMutation
    fun deletePermission(@InputArgument(name = "id") id: String): Boolean {
        permissionService.deleteById(ObjectId(id))
        return true
    }

    @DgsMutation
    fun createPermission(@InputArgument permissionDocument: PermissionInput): PermissionDocument? {
        return permissionService.createPermission(permissionDocument)
    }

    @DgsMutation
    fun updatePermission(@InputArgument permissionDocument: PermissionInput): PermissionDocument? {
        return permissionService.updatePermission(permissionDocument)
    }
}