package com.example.media_album.repositories

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.MediaDocument
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
interface MediaRepository: CommonRepository<MediaDocument, ObjectId> {
    fun findByFolderIsNullAndUserId(userId: ObjectId): List<MediaDocument>
    fun findByFolderId(folderId: ObjectId): List<MediaDocument>
    fun findByFilename(filename: String): MediaDocument?
    fun id(id: ObjectId): MutableList<MediaDocument>
}