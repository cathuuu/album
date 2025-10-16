package com.example.media_album.services.impl

import com.example.media_album.codegen.types.MediaShareInput
import com.example.media_album.models.documents.MediaShareDocument
import com.example.media_album.repositories.MediaRepository
import com.example.media_album.repositories.MediaShareRepository
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.MediaShareService
import com.example.media_album.services.PermissionService
import com.example.media_album.utils.getByIdOrThrow
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.nio.file.AccessDeniedException
import java.time.Instant

@Service
class MediaShareServiceImpl(repo : MediaShareRepository,
                            private val mediaRepo: MediaRepository,
                            private val userRepo: UserRepository,
                            private val permissionService: PermissionService
) : CommonServiceImpl<MediaShareDocument, ObjectId, MediaShareRepository>(repo),
    MediaShareService {
    override fun updateMediaShare(mediaShareDocument: MediaShareInput): MediaShareDocument {

        val userId = ObjectId(mediaShareDocument.sharedById)
        val mediaId = ObjectId(mediaShareDocument.mediaId)

        if (!permissionService.checkPermission(userId, mediaId, "EDIT")) {
            throw AccessDeniedException("You do not have permission to delete this media")
        }
        val id = mediaShareDocument.id ?: throw IllegalArgumentException("MediaShare ID is required!")

        val existingShare = repo.getByIdOrThrow(id, "MediaShare")
        val media = mediaRepo.getByIdOrThrow(mediaShareDocument.mediaId, "Media")
        val sharedWith = userRepo.getByIdOrThrow(mediaShareDocument.sharedWithId, "Shared user")
        val sharedBy = userRepo.getByIdOrThrow(mediaShareDocument.sharedById, "Owner")

        val updatedShare = existingShare.copy(
            media = media.id!!,
            sharedWith = sharedWith.id!!,
            sharedBy = sharedBy.id!!,
            permission = mediaShareDocument.permission,
            updatedAt = Instant.now()
        )

        return repo.save(updatedShare)
    }

    override fun createMediaShare(mediaShareDocument: MediaShareInput): MediaShareDocument? {

        val userId = ObjectId(mediaShareDocument.sharedById)
        val mediaId = ObjectId(mediaShareDocument.mediaId)

        if (!permissionService.checkPermission(userId, mediaId, "SHARE")) {
            throw AccessDeniedException("You do not have permission to delete this media")
        }

        val media = mediaRepo.getByIdOrThrow(mediaShareDocument.mediaId, "Media")
        val sharedWith = userRepo.getByIdOrThrow(mediaShareDocument.sharedWithId, "Shared user")
        val sharedBy = userRepo.getByIdOrThrow(mediaShareDocument.sharedById, "Owner")

        // Kiểm tra trùng chia sẻ
        repo.findByMediaAndSharedWith(media.id!!, sharedWith.id!!)?.let {
            throw RuntimeException("This media has already been shared with this user")
        }

        val newShare = MediaShareDocument(
            media = media.id!!,
            sharedWith = sharedWith.id!!,
            sharedBy = sharedBy.id!!,
            permission = mediaShareDocument.permission
        )

        return repo.save(newShare)
    }

    override fun findByShareWithUserFullName(userName: String): List<MediaShareDocument?> {
        return repo.findBySharedWithName(userName)
    }

    override fun findByShareByUserFullName(userName: String): List<MediaShareDocument?> {
        return repo.findBySharedByName(userName)
    }
}