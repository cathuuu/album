package com.example.media_album.services

import com.example.media_album.models.documents.FolderShareDocument
import org.bson.types.ObjectId

interface FolderShareService : CommonService<FolderShareDocument, ObjectId> {
    fun updateShareFolder( folderShareDocument: FolderShareDocument): FolderShareDocument?
}