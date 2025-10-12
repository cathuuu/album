package com.example.media_album.services

import java.io.Serializable

interface CommonService<E, ID : Serializable> {
    fun getAll(): MutableList<E?>?

    fun getById(id: ID?): E?

    fun save(document: E?): E?

    fun saveAll(documents: MutableList<E?>?): MutableList<E?>?

    fun existsById(id: ID?): Boolean

    fun deleteById(id: ID?)

    fun deleteByIdIn(ids: MutableList<ID?>?)

    fun getByName(name: String): E?
}