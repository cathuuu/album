package com.example.media_album.services.impl

import com.example.media_album.models.documents.MediaDocument
import com.example.media_album.models.documents.MediaShareDocument
import com.example.media_album.repositories.MediaShareRepository
import com.example.media_album.services.MediaService
import com.example.media_album.services.MediaShareService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class MediaShareServiceImpl(repo : MediaShareRepository) : CommonServiceImpl<MediaShareDocument, ObjectId, MediaShareRepository>(repo),
    MediaShareService {
    override fun updateMediaShare(mediaShareDocument: MediaShareDocument): MediaShareDocument {
        val id = mediaShareDocument.id ?: throw IllegalArgumentException("Folder ID is required!")

        val existingFolder = repo.findById(id)
            .orElseThrow { RuntimeException("Folder not found") }

        // Cập nhật thông tin
        val updatedFolder = existingFolder.copy(
            permission = mediaShareDocument.permission,
            updatedAt = Instant.now()
        )
        return repo.save(updatedFolder)
    }
}