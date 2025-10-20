package com.example.media_album.services

import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.models.dtos.FolderShareDto
import com.example.media_album.models.dtos.input.FolderShareInput
import org.bson.types.ObjectId

interface FolderShareService : CommonService<FolderShareDocument, ObjectId> {
    fun createFolderShare(folderShareDocument: FolderShareInput): FolderShareDocument?

    fun updateShareFolder( folderShareDocument: FolderShareInput): FolderShareDocument?


    fun findByShareWith(shareWith: ObjectId): List<FolderShareDto?>

    fun findByShareBy(shareBy: ObjectId): List<FolderShareDto?>
}