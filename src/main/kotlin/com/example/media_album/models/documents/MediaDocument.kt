package com.example.media_album.models.documents


import com.example.media_album.enums.MediaType
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant


@Document(collection = "medias")
data class MediaDocument(
    @Id
    val id: ObjectId? = null,

    @Field("user_id")
    val user: ObjectId,

    @Field("folder_id")
    val folder: ObjectId? = null,

    @Field("type")
    val type: MediaType = MediaType.OTHER,

    val url: String? = null,

    val filename: String,

    @Field("stored_filename")
    val storedFilename: String,

    @Field("mime_type")
    val mimeType: String? = null,

    val size: Long? = null, // bytes

    @Field("is_deleted")
    val isDeleted: Boolean = false,

    // 🔹 Thêm trường này để lưu đường dẫn thực tế (vật lý)
    @Field("path")
    val path: String? = null,


    @CreatedDate
    @Field("created_at")
    val createdAt: Instant = Instant.now(),

    @LastModifiedDate
    @Field("updated_at")
    val updatedAt: Instant? = null,

    @Field("photo_meta")
    val photoMeta: PhotoMeta? = null,

    @Field("video_meta")
    val videoMeta: VideoMeta? = null
)

data class PhotoMeta(
    @Field("camera_model")
    val cameraModel: String?,
    val iso: Int?,
    val aperture: String?
)

data class VideoMeta(
    val duration: Int?, // seconds
    val resolution: String?,
    @Field("frame_rate")
    val frameRate: Int?
)
