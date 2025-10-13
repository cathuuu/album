package com.example.media_album.services.impl

import com.example.media_album.codegen.types.MediaShareInput
import com.example.media_album.models.documents.MediaShareDocument
import com.example.media_album.repositories.MediaRepository
import com.example.media_album.repositories.MediaShareRepository
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.MediaShareService
import com.example.media_album.utils.getByIdOrThrow
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class MediaShareServiceImpl(repo : MediaShareRepository,
                            private val mediaRepo: MediaRepository,
                            private val userRepo: UserRepository
) : CommonServiceImpl<MediaShareDocument, ObjectId, MediaShareRepository>(repo),
    MediaShareService {
    override fun updateMediaShare(mediaShareDocument: MediaShareInput): MediaShareDocument {
        val id = mediaShareDocument.id ?: throw IllegalArgumentException("MediaShare ID is required!")

        val existingShare = repo.getByIdOrThrow(id, "MediaShare")
        val media = mediaRepo.getByIdOrThrow(mediaShareDocument.mediaId, "Media")
        val sharedWith = userRepo.getByIdOrThrow(mediaShareDocument.sharedWithId, "Shared user")
        val sharedBy = userRepo.getByIdOrThrow(mediaShareDocument.sharedById, "Owner")

        val updatedShare = existingShare.copy(
            media = media,
            sharedWith = sharedWith,
            sharedBy = sharedBy,
            permission = mediaShareDocument.permission,
            updatedAt = Instant.now()
        )

        return repo.save(updatedShare)
    }

    override fun createMediaShare(mediaShareDocument: MediaShareInput): MediaShareDocument? {
        val media = mediaRepo.getByIdOrThrow(mediaShareDocument.mediaId, "Media")
        val sharedWith = userRepo.getByIdOrThrow(mediaShareDocument.sharedWithId, "Shared user")
        val sharedBy = userRepo.getByIdOrThrow(mediaShareDocument.sharedById, "Owner")

        // Kiểm tra trùng chia sẻ
        repo.findByMediaAndSharedWith(media, sharedWith)?.let {
            throw RuntimeException("This media has already been shared with this user")
        }

        val newShare = MediaShareDocument(
            media = media,
            sharedWith = sharedWith,
            sharedBy = sharedBy,
            permission = mediaShareDocument.permission,
            createdAt = Instant.now()
        )

        return repo.save(newShare)
    }
}