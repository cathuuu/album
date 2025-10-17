package com.example.media_album.services

import com.example.media_album.models.documents.UserDocument
import com.example.media_album.models.dtos.input.UpdateUserInput
import org.bson.types.ObjectId

interface UserService : CommonService<UserDocument, ObjectId> {
    fun updateUser( userDocument: UpdateUserInput): UserDocument
}