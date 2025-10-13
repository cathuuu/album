package com.example.media_album.models.dtos.input

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.UserDocument

data class FolderInput(
    val id: String? = null,
    val userId: String,
    val name: String?,
    val parentFolderId: String?,
    val coverUrl: String,
    val isShared: Boolean,
    val isDeleted: Boolean,
) {
}