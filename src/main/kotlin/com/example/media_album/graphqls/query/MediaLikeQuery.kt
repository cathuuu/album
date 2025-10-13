package com.example.media_album.graphqls.query

import com.example.media_album.models.documents.MediaLikeDocument
import com.example.media_album.services.MediaLikeService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery

@DgsComponent
class MediaLikeQuery(
    private val mediaLikeService: MediaLikeService,
) {
    @DgsQuery
    fun findAllByUserId(userId: String): List<MediaLikeDocument> {
        return mediaLikeService.findAllByUserId(userId)
    }
}