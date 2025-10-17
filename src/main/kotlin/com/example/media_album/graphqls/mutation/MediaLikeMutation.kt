package com.example.media_album.graphqls.mutation

import com.example.media_album.codegen.types.MediaLikeInput
import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.models.documents.MediaLikeDocument
import com.example.media_album.repositories.MediaLikeRepository
import com.example.media_album.services.MediaLikeService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.bson.types.ObjectId

@DgsComponent
class MediaLikeMutation(
    private val mediaLike: MediaLikeService,
) {
    @DgsMutation
    fun createMediaLike(@InputArgument input: MediaLikeInput) : MediaLikeDocument? {
        return mediaLike.createMediaLike(input)
    }
    @DgsMutation
    fun deleteMediaLike(@InputArgument(name = "id") id: String): Boolean  {
        mediaLike.deleteById(ObjectId(id))
        return true
    }
}