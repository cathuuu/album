package com.example.media_album.graphqls.mutation

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.dtos.input.FolderInput
import com.example.media_album.services.FolderService
import com.example.media_album.services.UserService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.bson.types.ObjectId

@DgsComponent
class FolderMutation(
    private val folderService: FolderService,
    private val userService: UserService,
) {
    @DgsMutation
    fun createFolder(@InputArgument("folderInput") folderInput: FolderInput): FolderDocument? {
        val user = userService.getById(ObjectId(folderInput.userId))
        val parentFolder = folderInput.parentFolderId?.let { parentId ->
            folderService.getById(ObjectId(parentId))
        }

        val folderToCreate = FolderDocument(
            id = folderInput.id?.let { ObjectId(it) },
            user = user!!,
            name = folderInput.name,
            parentFolder = parentFolder,
            coverUrl = folderInput.coverUrl,
            isShared = folderInput.isShared,
            isDeleted = folderInput.isDeleted
        )

        return folderService.save(folderToCreate)
    }

    @DgsMutation
    fun updateFolder(@InputArgument("folderInput") folderInput: FolderInput): FolderDocument? {
        val id = folderInput.id?.let { ObjectId(it) }
            ?: throw IllegalArgumentException("Folder ID is required for update")

        val user = userService.getById(ObjectId(folderInput.userId))
        val parentFolder = folderInput.parentFolderId?.let { parentId ->
            folderService.getById(ObjectId(parentId))
        }

        val folderToUpdate = FolderDocument(
            id = id,
            user = user!!,
            name = folderInput.name,
            parentFolder = parentFolder,
            coverUrl = folderInput.coverUrl,
            isShared = folderInput.isShared,
            isDeleted = folderInput.isDeleted
        )

        return folderService.updateFolder(folderToUpdate)
    }

    @DgsMutation
    fun deleteFolder(@InputArgument id: String): Boolean {
        folderService.deleteById(ObjectId(id))
        return true
    }
}