package com.example.media_album.graphqls.dataFetcher

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.models.documents.UserDocument
import com.example.media_album.repositories.FolderRepository
import com.example.media_album.repositories.UserRepository
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment

@DgsComponent
class FolderShareDataFetcher (
    private val folderRepo: FolderRepository,
    private val userRepo: UserRepository
) {
    @DgsData(parentType = "FolderShareDocument", field = "folder")
    fun folder(dfe: DgsDataFetchingEnvironment): FolderDocument {
        val share = dfe.getSource<FolderShareDocument>()
        return folderRepo.findById(share.folder)
            .orElseThrow { RuntimeException("Folder not found") }
    }

    @DgsData(parentType = "FolderShareDocument", field = "sharedWith")
    fun sharedWith(dfe: DgsDataFetchingEnvironment): UserDocument {
        val share = dfe.getSource<FolderShareDocument>()
        return userRepo.findById(share.sharedWith)
            .orElseThrow { RuntimeException("User not found") }
    }

    @DgsData(parentType = "FolderShareDocument", field = "sharedBy")
    fun sharedBy(dfe: DgsDataFetchingEnvironment): UserDocument {
        val share = dfe.getSource<FolderShareDocument>()
        return userRepo.findById(share.sharedBy)
            .orElseThrow { RuntimeException("User not found") }
    }
}