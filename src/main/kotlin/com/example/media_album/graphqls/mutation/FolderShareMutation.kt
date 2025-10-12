package com.example.media_album.graphqls.mutation

import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.services.FolderService
import com.example.media_album.services.FolderShareService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class FolderShareMutation(
    private val folderShareService: FolderShareService,
) {
    @DgsMutation
    fun deleteShareFolder(@InputArgument folderShareDocument: FolderShareDocument) {
        folderShareService.deleteById(folderShareDocument.id)
    }

    @DgsMutation
    fun createShareFolder(@InputArgument folderShareDocument: FolderShareDocument): FolderShareDocument? {
        return folderShareService.save(folderShareDocument)
    }

    @DgsMutation
    fun updateShareFolder(@InputArgument folderShareDocument: FolderShareDocument): FolderShareDocument? {
        return folderShareService.updateShareFolder(folderShareDocument)
    }
}