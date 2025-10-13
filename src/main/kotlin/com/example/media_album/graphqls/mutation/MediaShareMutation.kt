package com.example.media_album.graphqls.mutation

import com.example.media_album.codegen.types.MediaShareInput
import com.example.media_album.models.documents.MediaShareDocument
import com.example.media_album.services.MediaShareService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.bson.types.ObjectId

@DgsComponent
class MediaShareMutation(
    private val mediaShareService: MediaShareService
) {
    @DgsMutation
    fun deleteMediaShare(@InputArgument(name = "id") id: String): Boolean {
        mediaShareService.deleteById(ObjectId(id))
        return true
    }

    @DgsMutation
    fun createMediaShare(@InputArgument mediaShareDocument: MediaShareInput): MediaShareDocument? {
        return mediaShareService.createMediaShare(mediaShareDocument)
    }

    @DgsMutation
    fun updateMediaShare(@InputArgument mediaShareDocument: MediaShareInput): MediaShareDocument? {
        return mediaShareService.updateMediaShare(mediaShareDocument)
    }
}