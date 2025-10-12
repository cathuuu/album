package com.example.media_album.graphqls.query

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.services.FolderService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.com.google.common.base.Objects
import org.bson.types.ObjectId

@DgsComponent
class FolderQuery(
    private val folderService: FolderService
) {
    @DgsQuery
    fun getFolder(@InputArgument id: String): FolderDocument? {
        return folderService.getById(ObjectId(id))
    }

    @DgsQuery
    fun getFolderByName(@InputArgument name: String): FolderDocument? {
        return folderService.getByName(name)
    }

    @DgsQuery
    fun getAllFolders(): MutableList<FolderDocument?>? {
        return folderService.getAll()
    }
}