package com.example.media_album.repositories

import com.example.media_album.models.documents.MediaShareDocument
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
interface MediaShareRepository : CommonRepository<MediaShareDocument, ObjectId> {
    fun findByMediaAndSharedWith(media: ObjectId, sharedWith: ObjectId) : MediaShareDocument?

    fun findBySharedWith(sharedWith: ObjectId): List<MediaShareDocument>

    fun findBySharedBy(sharedBy: ObjectId): List<MediaShareDocument>
}