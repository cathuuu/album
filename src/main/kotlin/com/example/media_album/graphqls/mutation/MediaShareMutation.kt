package com.example.media_album.graphqls.mutation

import com.example.media_album.models.documents.MediaShareDocument
import com.example.media_album.services.MediaShareService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class MediaShareMutation(
    private val mediaShareService: MediaShareService
) {
    @DgsMutation
    fun deleteShareFolder(@InputArgument mediaShareDocument: MediaShareDocument) {
        mediaShareService.deleteById(mediaShareDocument.id)
    }

    @DgsMutation
    fun createShareFolder(@InputArgument mediaShareDocument: MediaShareDocument): MediaShareDocument? {
        return mediaShareService.save(mediaShareDocument)
    }

    @DgsMutation
    fun updateShareFolder(@InputArgument mediaShareDocument: MediaShareDocument): MediaShareDocument? {
        return mediaShareService.updateMediaShare(mediaShareDocument)
    }
}