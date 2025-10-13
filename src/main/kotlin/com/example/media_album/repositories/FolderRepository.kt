package com.example.media_album.repositories

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.FolderShareDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FolderRepository: CommonRepository<FolderDocument, ObjectId>  {
    fun findByUserIdAndParentFolderIsNull(ownerId: ObjectId): List<FolderDocument>
    fun findByParentFolderId(parentId: ObjectId): List<FolderDocument>
    @Query("{ 'name': ?0 }")
    fun findByFolderName(name: String): List<FolderDocument>
}