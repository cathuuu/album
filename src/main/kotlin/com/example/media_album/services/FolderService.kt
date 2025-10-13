package com.example.media_album.services

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.dtos.input.FolderInput
import org.bson.types.ObjectId

interface FolderService : CommonService<FolderDocument, ObjectId> {
    fun updateFolder(folderDocument: FolderInput) : FolderDocument?

    fun findRootFoldersByOwnerId(ownerId: ObjectId): List<FolderDocument>

    fun findSubFoldersByParentId(parentId: ObjectId): List<FolderDocument>

    fun saveFolder(folderDocument: FolderInput?): FolderDocument

    fun findByFolderName( folderName: String): List<FolderDocument?>

}