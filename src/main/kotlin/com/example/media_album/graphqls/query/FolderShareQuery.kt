package com.example.media_album.graphqls.query

import com.example.media_album.services.FolderShareService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery


@DgsComponent
class FolderShareQuery(
    private val folderShareService: FolderShareService
) {
}