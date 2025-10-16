package com.example.media_album.models.documents


import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant
import java.time.LocalDate

@Document(collection = "users")
data class UserDocument(
    @Id
    val id: ObjectId? = null,

    val username: String,
    val password: String,
    val phone: String?,
    val email: String?,
    @Field("full_name")
    val fullName: String?,

    val gender: String?, // male | female | other
    val dob: LocalDate?,

    @Field("status_user")
    val statusUser: String = "active", // active | inactive | banned

    val roleIds: List<ObjectId> = emptyList(),

    @CreatedDate
    @Field("created_at")
    val createdAt: Instant = Instant.now(),

    @LastModifiedDate
    @Field("updated_at")
    val updatedAt: Instant? = null
)