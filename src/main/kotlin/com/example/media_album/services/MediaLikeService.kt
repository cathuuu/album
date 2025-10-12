package com.example.media_album.services

import com.example.media_album.models.documents.MediaLikeDocument
import org.bson.types.ObjectId

interface MediaLikeService : CommonService<MediaLikeDocument, ObjectId> {
}