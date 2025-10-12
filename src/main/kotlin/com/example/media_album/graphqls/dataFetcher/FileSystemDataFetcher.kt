package com.example.media_album.graphqls.dataFetcher

import com.example.media_album.models.dtos.FileSystemItem
import com.example.media_album.services.FileSystemService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.RequestAttribute

class FileSystemDataFetcher {
    @DgsComponent
    class FileSystemDataFetcher(
        private val fileSystemService: FileSystemService
    ) {

        /**
         * Lấy danh sách các mục cấp cao nhất (Root Items) cho người dùng hiện tại:
         * - Các Folder gốc (parentFolder là NULL).
         * - Các Media chưa được tổ chức (folder là NULL).
         */
        @DgsQuery
        fun rootItems(
            // Lấy ID người dùng đã xác thực từ Request Context
            @RequestAttribute("userId") userId: ObjectId
        ): List<FileSystemItem> {

            // Gọi Service Layer để thực hiện 3 bước:
            // 1. Truy vấn Folders có parentFolderId = NULL
            // 2. Truy vấn Media có folderId = NULL
            // 3. Gộp, ánh xạ sang DTO (FileSystemItem) và sắp xếp
            return fileSystemService.getRootItems(userId)
        }
    }
}