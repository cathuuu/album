package com.example.media_album.models.dtos.input

data class FolderInput(
    val id: String? = null,
    val userId: String,
    val name: String,
    val parentFolderId: String? = null,
    val coverUrl: String? = null,
    val isShared: Boolean? = false,
    val isDeleted: Boolean? = false,
)
