package com.example.media_album.utils

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

fun <T> MongoRepository<T, ObjectId>.getByIdOrThrow(id: String, name: String): T {
    val objId = try {
        ObjectId(id)
    } catch (e: IllegalArgumentException) {
        throw IllegalArgumentException("Invalid ObjectId format for $name: $id")
    }

    return findById(objId)
        .orElseThrow { IllegalArgumentException("$name not found: $id") }
}