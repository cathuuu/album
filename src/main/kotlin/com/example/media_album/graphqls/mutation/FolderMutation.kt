package com.example.media_album.graphqls.mutation

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.services.FolderService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class FolderMutation(private val folderService: FolderService) {
    @DgsMutation
    fun createFolder(@InputArgument folderDocument: FolderDocument) : FolderDocument? {
        return folderService.save(folderDocument)
    }

    @DgsMutation
    fun updateFolder(@InputArgument folderDocument: FolderDocument) : FolderDocument? {
        return folderService.updateFolder(folderDocument)
    }

    @DgsMutation
    fun deleteFolder(@InputArgument folderDocument: FolderDocument)  {
         folderService.deleteById(folderDocument.id)
    }
}