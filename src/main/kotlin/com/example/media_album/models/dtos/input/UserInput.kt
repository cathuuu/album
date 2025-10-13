package com.example.media_album.models.dtos.input

import com.example.media_album.models.documents.RoleDocument
import org.bson.types.ObjectId
import java.time.Instant
import java.time.LocalDateTime

data class UserInput(
    val id: ObjectId? = null,
    val username: String,
    val password: String,
    val phone: String?,
    val email: String?,
    val fullName: String?,
    val gender: String?, // male | female | other
    val dob: LocalDateTime?,
    val statusUser: String = "active", // active | inactive | banned
    val roles: List<RoleDocument>? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant? = null
) {
}