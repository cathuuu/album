package com.example.media_album.repositories

import com.example.media_album.models.documents.MediaShareDocument
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
interface MediaShareRepository : CommonRepository<MediaShareDocument, ObjectId>