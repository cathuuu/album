package com.example.media_album.services.impl

import com.example.media_album.codegen.types.MediaLikeInput
import com.example.media_album.models.documents.MediaLikeDocument
import com.example.media_album.repositories.MediaLikeRepository
import com.example.media_album.repositories.MediaRepository
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.MediaLikeService
import com.example.media_album.utils.getByIdOrThrow
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class MediaLikeServiceImpl(repo : MediaLikeRepository,
    private val userRepository: UserRepository,
    private val mediaRepository: MediaRepository) : CommonServiceImpl<MediaLikeDocument, ObjectId, MediaLikeRepository>(repo),
    MediaLikeService {
    override fun createMediaLike(mediaLikeDocument: MediaLikeInput): MediaLikeDocument? {
        val media = mediaRepository.getByIdOrThrow(mediaLikeDocument.id!! , "Media")

        // 2. Lấy user document
        val user = userRepository.findById(ObjectId(mediaLikeDocument.userId))
            .orElseThrow { RuntimeException("User not found") }

        // 3. Kiểm tra đã like chưa (chú ý truyền đúng media và user)
        val existingLike = repo.findByMediaAndUser(media, user)
        if (existingLike != null) {
            throw RuntimeException("User has already liked this media")
        }

        // 4. Tạo và lưu MediaLikeDocument
        val newLike = MediaLikeDocument(
            media = media, // ✅ media là MediaDocument
            user = user    // ✅ user là UserDocument
        )

        return repo.save(newLike)
    }

}