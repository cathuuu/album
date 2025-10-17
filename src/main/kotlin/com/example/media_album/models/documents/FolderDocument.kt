package com.example.media_album.models.documents


import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document(collection = "folders")
data class FolderDocument(
    @Id
    val id: ObjectId? = null,

    @Field("owner_id")
    var ownerId: ObjectId?=null,

    var name: String,

    // Folder cha (null nếu là root)
    @Field("parent_id")
    var parentId: ObjectId? = null,

    @Field("cover_url")
    var coverUrl: String? = null,

    @Field("is_shared")
    var isShared: Boolean = false,

    @Field("path")
    var path: String? = null,

    @Field("is_deleted")
    var isDeleted: Boolean = false,

    @CreatedDate
    @Field("created_at")
    val createdAt: Instant = Instant.now(),

    @LastModifiedDate
    @Field("updated_at")
    var updatedAt: Instant? = null
) {

}