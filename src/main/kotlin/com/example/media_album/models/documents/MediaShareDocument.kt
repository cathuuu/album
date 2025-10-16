package com.example.media_album.models.documents


import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document(collection = "media_shares")
data class MediaShareDocument(
    @Id
    val id: ObjectId? = null,

    @Field("media_id")
    val media: ObjectId,

    @Field("shared_with")
    val sharedWith: ObjectId,

    @Field("shared_by")
    val sharedBy: ObjectId,

    val permission: List<String> = emptyList(),

    val inherited: Boolean = false,

    @CreatedDate
    @Field("created_at")
    val createdAt: Instant = Instant.now(),

    @LastModifiedDate
    @Field("updated_at")
    val updatedAt: Instant? = null
) {
}