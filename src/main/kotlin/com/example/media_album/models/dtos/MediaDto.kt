package com.example.media_album.models.dtos

import com.example.media_album.models.documents.MediaDocument
import java.time.Instant

data class MediaDto(
    override val id: String,
    override val name: String, // Ánh xạ từ filename
    override val createdAt: Instant,
    override val updatedAt: Instant?,
    override val isShared: Boolean, // MediaDocument không có isShared, nên cần thêm logic hoặc xem lại
    override val isDeleted: Boolean,

    // Các trường đặc trưng của Media
    val fileType: String,      // "photo" | "video"
    val url: String,           // Đường dẫn truy cập
    val mimeType: String?,
    val size: Long?,           // bytes
    val folderId: String?,     // ID thư mục cha (nếu có)
    val thumbnailUrl: String? = null, // Ảnh thumbnail cho UI (tính toán thêm)

    // Thuộc tính cố định
    override val itemType: String = "media"
) : FileSystemItem {

    companion object {
        fun fromDocument(doc: MediaDocument): MediaDto {
            return MediaDto(
                id = doc.id?.toHexString() ?: "",
                name = doc.filename,
                createdAt = doc.createdAt,
                updatedAt = doc.updatedAt,
                // NOTE: MediaDocument bạn cung cấp KHÔNG CÓ trường isShared,
                // tạm đặt là false hoặc cần xem lại Document
                isShared = false,
                isDeleted = doc.isDeleted,

                fileType = doc.type.toString(),
                url = doc.url.toString(),
                mimeType = doc.mimeType,
                size = doc.size,
                // Lấy ID của FolderDocument (tham chiếu)
                folderId = doc.folder?.toHexString(),
                thumbnailUrl = null // Logic tạo thumbnail sẽ được thêm sau
            )
        }
    }
}