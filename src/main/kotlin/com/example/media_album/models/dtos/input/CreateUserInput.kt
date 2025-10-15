package com.example.media_album.models.dtos.input

import java.time.LocalDate
import java.time.LocalDateTime

data class CreateUserInput(
    val username: String,
    val password: String,
    val phone: String?,
    val email: String?,
    val fullName: String?,
    val gender: String?,
    val dob: LocalDate?,
)
