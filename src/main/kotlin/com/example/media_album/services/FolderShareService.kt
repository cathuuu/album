package com.example.media_album.services

import com.example.media_album.codegen.types.MediaShareInput
import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.models.documents.MediaShareDocument
import com.example.media_album.models.dtos.input.FolderShareInput
import com.netflix.graphql.dgs.InputArgument
import org.bson.types.ObjectId

interface FolderShareService : CommonService<FolderShareDocument, ObjectId> {
    fun createFolderShare(folderShareDocument: FolderShareInput): FolderShareDocument?

    fun updateShareFolder( folderShareDocument: FolderShareInput): FolderShareDocument?


    fun findByShareWithUserFullName(userName: String): List<FolderShareDocument?>

    fun findByShareByUserFullName(userName: String): List<FolderShareDocument?>
}