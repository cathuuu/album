package com.example.media_album.graphqls.query


import com.example.media_album.models.documents.FolderShareDocument
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
    fun findFolderSharesWithByUserFullName(@InputArgument fullName: String): List<FolderShareDocument?> {
        return folderShareService.findByShareWithUserFullName(fullName)
    }

    @DgsQuery
    fun findFolderSharesByUserFullName(@InputArgument fullName: String): List<FolderShareDocument?> {
        return folderShareService.findByShareByUserFullName(fullName)
    }
}