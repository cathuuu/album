package com.example.media_album.repositories

import com.example.media_album.models.documents.FolderShareDocument
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
interface FolderShareRepository : CommonRepository<FolderShareDocument, ObjectId> {

    fun findBySharedWith_FullName(fullName: String): List<FolderShareDocument>

    fun findBySharedBy_FullName(fullName: String): List<FolderShareDocument>
}