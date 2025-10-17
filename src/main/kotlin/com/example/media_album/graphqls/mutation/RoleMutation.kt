package com.example.media_album.graphqls.mutation

import com.example.media_album.models.documents.RoleDocument
import com.example.media_album.models.dtos.input.RoleInput
import com.example.media_album.repositories.PermissionRepository
import com.example.media_album.services.RoleService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.bson.types.ObjectId

@DgsComponent
class RoleMutation(
    private val roleService: RoleService,
     val permissionRepository: PermissionRepository
) {
    @DgsMutation
    fun createRole(
        @InputArgument input: RoleInput
    ): RoleDocument? {
        return roleService.createRole(input)
    }
    @DgsMutation
    fun updateRole(@InputArgument input: RoleInput) : RoleDocument? {
        return roleService.updateRole(input)
    }

    @DgsMutation
    fun deleteRole(@InputArgument(name = "id") id: String): Boolean  {
        roleService.deleteById(ObjectId(id))
        return true
    }
}