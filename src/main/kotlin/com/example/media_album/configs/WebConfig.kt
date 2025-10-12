package com.example.media_album.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Paths


@Configuration
class WebConfig(
    @Value("\${app.upload.dir}") private val uploadDir: String
) : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize().toUri().toString()
        registry.addResourceHandler("/files/**")
            .addResourceLocations(uploadPath)
            .setCachePeriod(0) // không cache, tiện debug
    }
}