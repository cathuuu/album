package com.example.media_album.graphqls.dataFetcher

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.MediaDocument
import com.example.media_album.models.dtos.FileSystemItem
import com.example.media_album.services.FileSystemService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.DgsRuntimeWiring
import com.netflix.graphql.dgs.DgsTypeResolver
import com.netflix.graphql.dgs.InputArgument
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import org.bson.types.ObjectId

@DgsComponent
class FileSystemItemResolver( private val fileSystemService: FileSystemService
) {

    /**
     * Lấy danh sách các item ở cấp root (Folder + Media chưa có folder)
     */
    @DgsQuery
    fun rootItems(@InputArgument userId: String): List<FileSystemItem> {
        return fileSystemService.getRootItems(ObjectId(userId))
    }

    /**
     * Lấy nội dung bên trong một folder cụ thể
     */
    @DgsQuery
    fun getFolderContents(@InputArgument folderId: String): List<FileSystemItem>? {
        return fileSystemService.getFolderContents(ObjectId(folderId))
    }

    /**
     * Xác định kiểu cụ thể của FileSystemItem trong GraphQL Union
     */
    @DgsTypeResolver(name = "FileSystemItem")
    fun resolveType(item: FileSystemItem): String = when (item.itemType) {
        "folder" -> "FolderDocument"
        "media" -> "MediaDocument"
        else -> "Unknown"
    }

    @DgsQuery
    fun getDeletedItems(@InputArgument userId: String): List<FileSystemItem> {
        // Ủy quyền việc truy vấn logic cho FileSystemService
        return fileSystemService.getDeletedItems(ObjectId(userId))
    }

}