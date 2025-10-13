package com.example.media_album.models.dtos.input

data class RoleInput(
    val id: String? = null,
    val roleName: String,
    val permissionIds: List<String>? = null
) {

}