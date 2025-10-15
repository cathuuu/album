package com.example.media_album.services.impl

import com.example.media_album.models.documents.UserDocument
import com.example.media_album.models.dtos.input.UserInput
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.UserService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime

@Service
class UserServiceImpl(repo: UserRepository) : CommonServiceImpl<UserDocument, ObjectId, UserRepository>(repo),
    UserService {
    override fun updateUser(userDocument: UserInput): UserDocument {
        val id = userDocument.id ?: throw IllegalArgumentException("User ID is required!")

        val objectId = id
        val existingUser = repo.findById(objectId)
            .orElseThrow { RuntimeException("User not found") }

        val updatedUser = existingUser.copy(
            password = userDocument.password,
            phone = userDocument.phone,
            fullName = userDocument.fullName,
            email = userDocument.email,
            gender = userDocument.gender,
            dob = userDocument.dob,
            statusUser = userDocument.statusUser,
            roles = userDocument.roles!!,
            updatedAt = Instant.now()
        )

        return repo.save(updatedUser)
    }
}