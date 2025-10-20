package com.example.media_album.graphqls.query

import com.example.media_album.models.documents.MediaShareDocument
import com.example.media_album.models.dtos.MediaShareDto
import com.example.media_album.services.MediaShareService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.bson.types.ObjectId

@DgsComponent
class MediaShareQuery(
    private val mediaShareService: MediaShareService
) {
    @DgsQuery
    fun findMediaShareWith(@InputArgument shareWith: ObjectId): List<MediaShareDto?> {
        return mediaShareService.findMediaShareBySharedWith(shareWith)
    }

    @DgsQuery
    fun findMediaShareBy(@InputArgument  shareBy: ObjectId): List<MediaShareDto?> {
        return mediaShareService.findMediaShareByShareBy(shareBy)
    }
}