package com.example.media_album.graphqls.mutation

import com.example.media_album.models.documents.RoleDocument
import com.example.media_album.services.RoleService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class RoleMutation(
    private val roleService: RoleService,
) {
    @DgsMutation
    fun createRole(@InputArgument roleDocument: RoleDocument) : RoleDocument? {
        return roleService.save(roleDocument)
    }

    @DgsMutation
    fun updateRole(@InputArgument roleDocument: RoleDocument) : RoleDocument? {
        return roleService.updateRole(roleDocument)
    }

    @DgsMutation
    fun deleteRole(@InputArgument roleDocument: RoleDocument)  {
        roleService.deleteById(roleDocument.id)
    }
}