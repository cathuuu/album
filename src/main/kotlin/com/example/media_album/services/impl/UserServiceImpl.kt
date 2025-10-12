package com.example.media_album.services.impl

import com.example.media_album.models.documents.UserDocument
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.UserService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class UserServiceImpl(repo: UserRepository) : CommonServiceImpl<UserDocument, ObjectId, UserRepository>(repo),
    UserService {
    override fun updateUser(userDocument: UserDocument): UserDocument {
        val id = userDocument.id ?: throw IllegalArgumentException("Folder ID is required!")

        val existingFolder = repo.findById(id)
            .orElseThrow { RuntimeException("Folder not found") }

        // Cập nhật thông tin
        val updatedFolder = existingFolder.copy(
            password = userDocument.password,
            phone = userDocument.phone,
            fullName = userDocument.fullName,
            email = userDocument.email,
            gender = userDocument.gender,
            dob = userDocument.dob,
            statusUser = userDocument.statusUser,
            roles = userDocument.roles,
            updatedAt = Instant.now()
        )
        return repo.save(updatedFolder)
    }
}