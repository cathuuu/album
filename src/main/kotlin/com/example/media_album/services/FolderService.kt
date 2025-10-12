package com.example.media_album.services

import com.example.media_album.models.documents.FolderDocument
import org.bson.types.ObjectId

interface FolderService : CommonService<FolderDocument, ObjectId> {
    fun updateFolder(folderDocument: FolderDocument) : FolderDocument?

    fun findRootFoldersByOwnerId(ownerId: ObjectId): List<FolderDocument>

    fun findSubFoldersByParentId(parentId: ObjectId): List<FolderDocument>
}