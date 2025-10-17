package com.example.media_album.graphqls.mutation

import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.models.dtos.input.FolderShareInput
import com.example.media_album.services.FolderService
import com.example.media_album.services.FolderShareService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.bson.types.ObjectId

@DgsComponent
class FolderShareMutation(
    private val folderShareService: FolderShareService,
) {
    @DgsMutation
    fun deleteShareFolder(@InputArgument(name = "id") id: String): Boolean {
        folderShareService.deleteById(ObjectId(id))
        return true
    }

    @DgsMutation
    fun createShareFolder(@InputArgument input: FolderShareInput): FolderShareDocument? {
        return folderShareService.createFolderShare(input)
    }

    @DgsMutation
    fun updateShareFolder(@InputArgument input: FolderShareInput): FolderShareDocument? {
        return folderShareService.updateShareFolder(input)
    }
}