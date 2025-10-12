package com.example.media_album.repositories

import com.example.media_album.models.documents.UserDocument
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CommonRepository<UserDocument, ObjectId>