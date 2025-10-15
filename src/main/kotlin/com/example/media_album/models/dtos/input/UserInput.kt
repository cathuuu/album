package com.example.media_album.models.dtos.input

import org.bson.types.ObjectId
import java.time.Instant
import java.time.LocalDate

data class  UserInput(
    val id: ObjectId? = null,
    val username: String,
    val password: String,
    val phone: String?,
    val email: String?,
    val fullName: String?,
    val gender: String?, // male | female | other
    val dob: LocalDate?,
    val statusUser: String = "active", // active | inactive | banned
    val roles: List<ObjectId>? = emptyList(),
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant? = null
) {
}