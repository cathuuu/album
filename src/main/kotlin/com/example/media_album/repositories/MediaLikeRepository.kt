package com.example.media_album.repositories

import com.example.media_album.models.documents.MediaLikeDocument
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
interface MediaLikeRepository: CommonRepository<MediaLikeDocument, ObjectId> {
    fun findByMediaAndUser(media: ObjectId, user: ObjectId)

    fun findAllByUser(userId: ObjectId): List<MediaLikeDocument>
}