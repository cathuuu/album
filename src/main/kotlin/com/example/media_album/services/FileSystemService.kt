package com.example.media_album.services

import com.example.media_album.models.dtos.FileSystemItem
import com.example.media_album.models.dtos.FolderDto
import com.example.media_album.models.dtos.MediaDto
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class FileSystemService(
    private val folderService: FolderService,
    private val mediaService: MediaService
) {

    /**
     * Lấy các mục cấp cao nhất: Folder gốc và Media chưa được tổ chức.
     * Đây là hàm được gọi bởi DgsQuery 'rootItems'.
     */
    fun getRootItems(userId: ObjectId): List<FileSystemItem> {

        // 1. Lấy tất cả các Folder gốc
        val rootFolders = folderService.findRootFoldersByOwnerId(userId)
            // Ánh xạ sang DTO. itemCount tạm đặt là 0 (hoặc cần truy vấn count)
            .map { FolderDto.fromDocument(it, itemCount = 0) }

        // 2. Lấy tất cả Media không thuộc Folder nào
        val unorganizedMedia = mediaService.findUnorganizedMediaByUserId(userId)
            .map { MediaDto.fromDocument(it) } // Ánh xạ sang DTO

        // 3. Gộp và Sắp xếp: Folder trước, sau đó là Media, sắp xếp theo Tên
        val combinedList = rootFolders + unorganizedMedia

        return combinedList.sortedWith(
            compareBy<FileSystemItem> { it.itemType } // "folder" trước "media"
                .thenBy { it.name }
        )
    }

    /**
     * Lấy nội dung của một Folder cụ thể (Media và Sub-Folders).
     * Hàm này sẽ phục vụ cho query 'getFolderContents(folderId)'.
     */
    fun getFolderContents(folderId: ObjectId): List<FileSystemItem>? {
        // Đảm bảo Folder tồn tại
        folderService.getById(folderId) ?: return null

        // 1. Lấy tất cả Folder con
        val subFolders = folderService.findSubFoldersByParentId(folderId)
            .map { FolderDto.fromDocument(it, itemCount = 0) }

        // 2. Lấy tất cả Media trong Folder đó
        val mediaItems = mediaService.findMediaByFolderId(folderId)
            .map { MediaDto.fromDocument(it) }

        // 3. Gộp và Sắp xếp
        return (subFolders + mediaItems).sortedWith(
            compareBy<FileSystemItem> { it.itemType }.thenBy { it.name }
        )
    }
    fun getDeletedItems(userId: ObjectId): List<FileSystemItem> {
        // 1. Lấy tất cả Folder đã bị xóa và map sang DTO
        val deletedFolders: List<FileSystemItem> = folderService
            .findByUserIdAndIsDeletedTrue(userId)
            .map { FolderDto.fromDocument(it, itemCount = 0) }

        // 2. Lấy tất cả Media đã bị xóa và map sang DTO
        val deletedMedia: List<FileSystemItem> = mediaService
            .findByUserIdAndIsDeletedTrue(userId)
            .map { MediaDto.fromDocument(it) }

        // 3. Gộp danh sách
        val combinedList = deletedFolders + deletedMedia

        // 4. Sắp xếp theo deletedAt (mới nhất trước)
        return combinedList.sortedByDescending { it.updatedAt ?: Instant.EPOCH }
    }
}