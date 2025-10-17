package com.example.media_album.repositories

import com.example.media_album.models.documents.FolderDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FolderRepository: CommonRepository<FolderDocument, ObjectId>  {
    fun findByOwnerIdAndParentIdIsNull(ownerId: ObjectId): List<FolderDocument>
    fun findByParentId(parentId: ObjectId): List<FolderDocument>
    @Query("{ 'name': ?0 }")
    fun findByFolderName(name: String): List<FolderDocument>
    fun findByOwnerIdAndIsDeletedTrue(userId: ObjectId): List<FolderDocument>
    @Query("{ 'userId': ?0, 'name': ?1, 'parentId': ?2 }")
    fun findByUserIdAndNameAndParentId(
        userId: ObjectId,
        name: String,
        parentId: ObjectId?
    ): FolderDocument?

    fun findByOwnerIdAndNameAndParentId(ownerId: ObjectId, folderName: String, currentParentId: ObjectId?) : FolderDocument?
}