package com.example.media_album

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing


@EnableMongoAuditing
@SpringBootApplication
class MediaAlbumApplication

fun main(args: Array<String>) {
	runApplication<MediaAlbumApplication>(*args)
}
