package com.example.media_album.graphqls.query

import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.models.documents.MediaShareDocument
import com.example.media_album.services.MediaShareService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class MediaShareQuery(
    private val mediaShareService: MediaShareService
) {
    @DgsQuery
    fun findMediaShareWithByUserFullName(@InputArgument fullName: String): List<MediaShareDocument?> {
        return mediaShareService.findByShareWithUserFullName(fullName)
    }

    @DgsQuery
    fun findMediaShareByUserFullName(@InputArgument fullName: String): List<MediaShareDocument?> {
        return mediaShareService.findByShareByUserFullName(fullName)
    }
}