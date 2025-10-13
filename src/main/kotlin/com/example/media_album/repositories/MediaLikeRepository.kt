package com.example.media_album.repositories

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.MediaDocument
import com.example.media_album.models.documents.MediaLikeDocument
import com.example.media_album.models.documents.UserDocument
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
interface MediaLikeRepository: CommonRepository<MediaLikeDocument, ObjectId> {
    fun findByMediaAndUser(media: MediaDocument, user: UserDocument)
}