package com.example.media_album.services

import com.example.media_album.models.documents.MediaShareDocument
import org.bson.types.ObjectId

interface MediaShareService : CommonService<MediaShareDocument, ObjectId> {
    fun updateMediaShare(mediaShareDocument: MediaShareDocument): MediaShareDocument
}