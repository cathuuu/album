package com.example.media_album.models.documents


import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document(collection = "folder_shares")
data class FolderShareDocument(
    @Id
    val id: ObjectId? = null,

    @Field("folder_id")
    val folder: ObjectId,

    @Field("shared_with")
    val sharedWith: ObjectId,

    @Field("shared_by")
    val sharedBy: ObjectId,

    val permission: String, // "view" | "edit"

    @CreatedDate
    @Field("created_at")
    val createdAt: Instant = Instant.now(),

    @LastModifiedDate
    @Field("updated_at")
    val updatedAt: Instant? = null
) {
}