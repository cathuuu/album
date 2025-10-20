package com.example.media_album.graphqls.dataFetcher

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.models.documents.UserDocument
import com.example.media_album.models.dtos.FolderShareDto
import com.example.media_album.repositories.FolderRepository
import com.example.media_album.repositories.FolderShareRepository
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.FolderShareService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.InputArgument
import org.bson.types.ObjectId

@DgsComponent
class FolderShareDataFetcher (
    private val folderRepo: FolderRepository,
    private val userRepo: UserRepository,
    private val folderShareService: FolderShareService,) {
    @DgsData(parentType = "FolderShareDocument")
    fun folder(dfe: DgsDataFetchingEnvironment): FolderDocument {
        val share = dfe.getSource<FolderShareDto>()
        return folderRepo.findById(share.folder?.id!!)
            .orElseThrow { RuntimeException("Folder not found") }
    }

    @DgsData(parentType = "FolderShareDocument")
    fun sharedWith(dfe: DgsDataFetchingEnvironment): UserDocument {
        val share = dfe.getSource<FolderShareDto>()
        return userRepo.findById(ObjectId(share.sharedWith))
            .orElseThrow { RuntimeException("User not found") }
    }

    @DgsData(parentType = "FolderShareDocument")
    fun sharedBy(dfe: DgsDataFetchingEnvironment): UserDocument {
        val share = dfe.getSource<FolderShareDto>()
        return userRepo.findById(ObjectId(share.sharedBy))
            .orElseThrow { RuntimeException("User not found") }
    }


}