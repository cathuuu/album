package com.example.media_album.models.dtos

import com.example.media_album.models.documents.FolderDocument
import java.time.Instant

data class FolderShareDto(
    val id: String,
    val sharedWith: String,
    val sharedBy: String,
    val permission: List<String>,
    val inherited: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val folder: FolderDocument? // gắn full folder object
)