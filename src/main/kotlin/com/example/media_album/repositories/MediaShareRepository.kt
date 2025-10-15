package com.example.media_album.repositories

import com.example.media_album.models.documents.MediaShareDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.stereotype.Repository

@Repository
interface MediaShareRepository : CommonRepository<MediaShareDocument, ObjectId> {
    fun findByMediaAndSharedWith(media: ObjectId, sharedWith: ObjectId) : MediaShareDocument?

    @Aggregation(pipeline = [
        "{ \$lookup: { from: 'users', localField: 'sharedWith', foreignField: '_id', as: 'sharedUser' } }",
        "{ \$unwind: '\$sharedUser' }",
        "{ \$match: { 'sharedUser.fullName': ?0 } }"
    ])
    fun findBySharedWithName(fullName: String): List<MediaShareDocument>


    @Aggregation(pipeline = [
        "{ \$lookup: { from: 'users', localField: 'sharedBy', foreignField: '_id', as: 'sharedUser' } }",
        "{ \$unwind: '\$sharedUser' }",
        "{ \$match: { 'sharedUser.fullName': ?0 } }"
    ])
    fun findBySharedByName(fullName: String): List<MediaShareDocument>
}