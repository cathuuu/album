package com.example.media_album.models.documents


import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document(collection = "media_likes")
data class MediaLikeDocument(
    @Id
    val id: ObjectId? = null,

    @DBRef
    @Field("media_id")
    val media: MediaDocument,

    @DBRef
    @Field("user_id")
    val user: UserDocument,

    @CreatedDate
    @Field("created_at")
    val createdAt: Instant = Instant.now()
)
