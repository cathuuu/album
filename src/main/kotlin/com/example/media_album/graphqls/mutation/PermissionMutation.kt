package com.example.media_album.graphqls.mutation

import com.example.media_album.models.documents.PermissionDocument
import com.example.media_album.services.PermissionService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class PermissionMutation(
    private val permissionService: PermissionService,
) {
    @DgsMutation
    fun deletePermission(@InputArgument permissionDocument: PermissionDocument) {
        permissionService.deleteById(permissionDocument.id)
    }

    @DgsMutation
    fun createPermission(@InputArgument  permissionDocument: PermissionDocument): PermissionDocument? {
        return permissionService.save(permissionDocument)
    }

    @DgsMutation
    fun updatePermission(@InputArgument  permissionDocument: PermissionDocument): PermissionDocument? {
        return permissionService.updatePermission(permissionDocument)
    }
}