package com.example.media_album.graphqls.mutation

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.models.documents.MediaLikeDocument
import com.example.media_album.repositories.MediaLikeRepository
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class MediaLikeMutation(
    private val mediaLike: MediaLikeRepository,
) {
    @DgsMutation
    fun createMediaLike(@InputArgument MediaLikeDocument: MediaLikeDocument) : MediaLikeDocument? {
        return mediaLike.save(MediaLikeDocument)
    }
    @DgsMutation
    fun deleteMediaLike(@InputArgument mediaLikeDocument: MediaLikeDocument)  {
        mediaLike.deleteById(mediaLikeDocument.id!!)
    }
}