package com.example.media_album.repositories

import com.example.media_album.models.documents.FolderShareDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.stereotype.Repository

@Repository
interface FolderShareRepository : CommonRepository<FolderShareDocument, ObjectId> {

    @Aggregation(pipeline = [
        "{ \$lookup: { from: 'users', localField: 'sharedWith', foreignField: '_id', as: 'sharedUser' } }",
        "{ \$unwind: '\$sharedUser' }",
        "{ \$match: { 'sharedUser.fullName': ?0 } }"
    ])
    fun findBySharedWithName(fullName: String): List<FolderShareDocument>

    @Aggregation(pipeline = [
        "{ \$lookup: { from: 'users', localField: 'sharedBy', foreignField: '_id', as: 'sharedUser' } }",
        "{ \$unwind: '\$sharedBy' }",
        "{ \$match: { 'sharedUser.fullName': ?0 } }"
    ])
    fun findBySharedByName(fullName: String): List<FolderShareDocument>

    fun findByFolderAndSharedWith(folderId: ObjectId, userId: ObjectId) : FolderShareDocument?
}