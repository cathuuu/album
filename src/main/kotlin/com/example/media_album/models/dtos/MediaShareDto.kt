package com.example.media_album.models.dtos

import com.example.media_album.models.documents.MediaDocument
import org.bson.types.ObjectId

import java.time.Instant

data class MediaShareDto(
    val id: ObjectId? = null,

    val media: MediaDocument,

    val sharedWith: ObjectId,

    val sharedBy: ObjectId,

    val permission: List<String> = emptyList(),

    val inherited: Boolean = false,

    val createdAt: Instant = Instant.now(),

    val updatedAt: Instant? = null
) {
}