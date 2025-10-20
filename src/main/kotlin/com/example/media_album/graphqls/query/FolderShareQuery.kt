package com.example.media_album.graphqls.query


import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.models.dtos.FolderShareDto
import com.example.media_album.services.FolderShareService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.bson.types.ObjectId


@DgsComponent
class FolderShareQuery(
    private val folderShareService: FolderShareService
) {
    @DgsQuery
    fun findFolderSharesWith(@InputArgument shareWith: ObjectId): List<FolderShareDto?> {
        return folderShareService.findByShareWith(shareWith)
    }

    @DgsQuery
    fun findFolderSharesBy(@InputArgument shareBy: ObjectId): List<FolderShareDto?> {
        return folderShareService.findByShareBy(shareBy)
    }
}