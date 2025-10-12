package com.example.media_album.services.impl

import com.example.media_album.models.documents.MediaLikeDocument
import com.example.media_album.repositories.MediaLikeRepository
import com.example.media_album.services.MediaLikeService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class MediaLikeServiceImpl(repo : MediaLikeRepository) : CommonServiceImpl<MediaLikeDocument, ObjectId, MediaLikeRepository>(repo),
    MediaLikeService {

}