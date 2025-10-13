package com.example.media_album.services

import com.example.media_album.codegen.types.MediaLikeInput
import com.example.media_album.models.documents.MediaLikeDocument
import com.netflix.graphql.dgs.InputArgument
import org.bson.types.ObjectId

interface MediaLikeService : CommonService<MediaLikeDocument, ObjectId> {
    fun createMediaLike(mediaLikeDocument: MediaLikeInput) : MediaLikeDocument?

    fun findAllByUserId(userId: String): List<MediaLikeDocument>
}