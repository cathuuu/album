package com.example.media_album.models.dtos

import java.time.Instant

interface FileSystemItem {
    val id: String           // ID của Document
    val name: String         // Tên để hiển thị (filename hoặc folder name)
    val itemType: String     // Phân loại: "folder" hoặc "media"
    val createdAt: Instant   // Thời điểm tạo
    val updatedAt: Instant?  // Thời điểm cập nhật
    val isShared: Boolean    // Trạng thái chia sẻ
    val isDeleted: Boolean   // Trạng thái xóa mềm
}