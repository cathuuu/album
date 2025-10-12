package com.example.media_album.models.dtos

import com.example.media_album.models.documents.FolderDocument
import java.time.Instant

data class FolderDto(

    override val id: String,
    override val name: String,
    override val createdAt: Instant,
    override val updatedAt: Instant?,
    override val isShared: Boolean,
    override val isDeleted: Boolean,

// Các trường đặc trưng của Folder
    val parentFolderId: String?, // ID của thư mục cha (null nếu là Root)
    val coverUrl: String?,
    val itemCount: Int = 0, // Số lượng mục con (cần tính toán)

// Thuộc tính cố định
    override val itemType: String = "folder"
) : FileSystemItem {

    companion object {
        fun fromDocument(doc: FolderDocument, itemCount: Int = 0): FolderDto {
            return FolderDto(
                id = doc.id?.toHexString() ?: "",
                name = doc.name,
                createdAt = doc.createdAt,
                updatedAt = doc.updatedAt,
                isShared = doc.isShared ?: false, // Xử lý null
                isDeleted = doc.isDeleted ?: false, // Xử lý null

                // Lấy ID của Folder cha (tham chiếu)
                parentFolderId = doc.parentFolder?.id?.toHexString(),
                coverUrl = doc.coverUrl,
                itemCount = itemCount
            )
        }
    }
}

