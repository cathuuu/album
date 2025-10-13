package com.example.media_album.models.dtos.input

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.UserDocument
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

data class FolderShareInput(

    val id: String? = null,

    val folder: String,

    val sharedWith: String,

    val sharedBy: String,

    val permission: String, // "view" | "edit"

    val createdAt: Instant = Instant.now(),

    val updatedAt: Instant? = null
) {
}