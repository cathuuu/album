package com.example.media_album.repositories

import com.example.media_album.models.documents.MediaDocument
import com.example.media_album.models.documents.MediaShareDocument
import com.example.media_album.models.documents.UserDocument
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
interface MediaShareRepository : CommonRepository<MediaShareDocument, ObjectId> {
    fun findByMediaAndSharedWith(media: MediaDocument, sharedWith: UserDocument)
}