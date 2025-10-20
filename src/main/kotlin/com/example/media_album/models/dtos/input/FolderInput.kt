package com.example.media_album.models.dtos.input

data class FolderInput(
    val id: String? = null,
    val ownerId: String,
    val name: String,
    val parentId: String? = null,
    val coverUrl: String? = null,
    val isShared: Boolean = false
) {
}