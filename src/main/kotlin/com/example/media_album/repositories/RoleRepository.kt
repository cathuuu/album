package com.example.media_album.repositories

import com.example.media_album.models.documents.RoleDocument
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : CommonRepository<RoleDocument, ObjectId>
