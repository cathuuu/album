package com.example.media_album.graphqls.dataFetcher

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.documents.UserDocument
import com.example.media_album.repositories.UserRepository
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import org.bson.types.ObjectId

@DgsComponent
class FolderDataFetcher(private val userRepo: UserRepository) {

    @DgsData(parentType = "FolderDocument")
    fun ownerId(dfe: DgsDataFetchingEnvironment): UserDocument? {
        val folder = dfe.getSource<FolderDocument>()
        val ownerId = folder.ownerId ?: return null
        return userRepo.findById(ObjectId(ownerId.toString())).orElse(null)
    }
}