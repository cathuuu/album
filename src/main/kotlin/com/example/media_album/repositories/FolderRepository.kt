package com.example.media_album.repositories

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.FolderShareDocument
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
interface FolderRepository: CommonRepository<FolderDocument, ObjectId>  {
    fun findByUserIdAndParentFolderIsNull(ownerId: ObjectId): List<FolderDocument>
    fun findByParentFolderId(parentId: ObjectId): List<FolderDocument>
}