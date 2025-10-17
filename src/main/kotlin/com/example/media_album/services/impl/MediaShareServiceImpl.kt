package com.example.media_album.services.impl

import com.example.media_album.codegen.types.MediaShareInput
import com.example.media_album.enums.PermissionType
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
class MediaShareServiceImpl(
    repo: MediaShareRepository,
    private val mediaRepo: MediaRepository,
    private val userRepo: UserRepository,
    private val permissionService: PermissionService
) : CommonServiceImpl<MediaShareDocument, ObjectId, MediaShareRepository>(repo),
    MediaShareService {

    // ==================== CREATE SHARE ====================
    override fun createMediaShare(mediaShareDocument: MediaShareInput): MediaShareDocument {
        val mediaId = mediaShareDocument.mediaId?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("mediaId cannot be null")
        val sharedById = mediaShareDocument.sharedById?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("sharedById cannot be null")
        val sharedWithId = mediaShareDocument.sharedWithId?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("sharedWithId cannot be null")

        val media = mediaRepo.getByIdOrThrow(mediaShareDocument.mediaId, "Media")

        // 🔹 Chủ sở hữu media luôn có quyền SHARE
        val isOwner = media.user == sharedById
        val admin = permissionService.checkPermission(sharedById,mediaId, PermissionType.ALL.value)
        val canShare = isOwner ||  permissionService.checkPermission(sharedById, mediaId, PermissionType.SHARE.value )
        if (!canShare && !admin) {
            throw AccessDeniedException("You do not have permission to share this media.")
        }

        val sharedWith = userRepo.getByIdOrThrow(mediaShareDocument.sharedWithId, "Shared user")
        val sharedBy = userRepo.getByIdOrThrow(mediaShareDocument.sharedById, "Owner")

        val newShare = MediaShareDocument(
            media = media.id!!,
            sharedWith = sharedWith.id!!,
            sharedBy = sharedBy.id!!,
            permission = mediaShareDocument.permission ?: listOf(PermissionType.VIEW.value),
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        return repo.save(newShare)
    }

    // ==================== UPDATE SHARE ====================
    override fun updateMediaShare(mediaShareDocument: MediaShareInput): MediaShareDocument {
        val id = mediaShareDocument.id ?: throw IllegalArgumentException("MediaShare ID is required!")
        val mediaId = mediaShareDocument.mediaId?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("mediaId cannot be null")
        val sharedById = mediaShareDocument.sharedById?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("sharedById cannot be null")

        val media = mediaRepo.getByIdOrThrow(mediaShareDocument.mediaId, "Media")

        // 🔹 Chủ sở hữu media luôn có quyền EDIT
        val isOwner = media.user == sharedById
        val canEdit = isOwner || permissionService.checkPermission(sharedById, mediaId, PermissionType.EDIT.value)
        if (!canEdit) {
            throw AccessDeniedException("You do not have permission to edit this media share.")
        }

        val existingShare = repo.getByIdOrThrow(id, "MediaShare")
        val sharedWith = userRepo.getByIdOrThrow(mediaShareDocument.sharedWithId, "Shared user")
        val sharedBy = userRepo.getByIdOrThrow(mediaShareDocument.sharedById, "Owner")

        val updatedShare = existingShare.copy(
            media = media.id!!,
            sharedWith = sharedWith.id!!,
            sharedBy = sharedBy.id!!,
            permission = mediaShareDocument.permission ?: existingShare.permission,
            updatedAt = Instant.now()
        )

        return repo.save(updatedShare)
    }

    // ==================== FIND ====================
    override fun findByShareWithUserFullName(userName: String): List<MediaShareDocument> =
        repo.findBySharedWithName(userName)

    override fun findByShareByUserFullName(userName: String): List<MediaShareDocument> =
        repo.findBySharedByName(userName)
}
