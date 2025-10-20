package com.example.media_album.repositories

import com.example.media_album.models.documents.FolderShareDocument
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
interface FolderShareRepository : CommonRepository<FolderShareDocument, ObjectId> {


    fun findBySharedWith(sharedWith: ObjectId): List<FolderShareDocument>

    fun findBySharedBy(sharedBy: ObjectId): List<FolderShareDocument>

    fun findByFolderAndSharedWith(folderId: ObjectId, userId: ObjectId) : FolderShareDocument?
}