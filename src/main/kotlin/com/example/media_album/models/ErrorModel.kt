package com.example.media_album.models

import java.io.Serializable

data class ErrorModel
// Định nghĩa thuộc tính trong constructor chính (Primary Constructor)
private constructor(
    val statusCode: Long? = null, // Giá trị mặc định là null
    val message: String? = null // Giá trị mặc định là null
) : Serializable {

    // Companion Object (chứa các phương thức tĩnh)
    companion object {

        fun of(statusCode: Long?, message: String?): ErrorModel {
            return ErrorModel(statusCode = statusCode, message = message)
        }

        fun of(statusCode: Long?): ErrorModel {
            // Sử dụng giá trị mặc định của message (null)
            return ErrorModel(statusCode = statusCode)
        }


        fun of(message: String?): ErrorModel {
            // Sử dụng giá trị mặc định của statusCode (null)
            return ErrorModel(message = message)
        }
    }
}