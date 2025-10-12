package com.example.media_album.repositories

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface CommonRepository<E, ID> : MongoRepository<E, ID> {
    fun deleteByIdIn(ids: MutableList<ID?>?)

    fun findByName(name: String) : E
}