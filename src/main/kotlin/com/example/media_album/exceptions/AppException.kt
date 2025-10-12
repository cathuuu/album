package com.example.media_album.exceptions


import com.example.media_album.models.ErrorModel


class AppException(
    val errorModel: ErrorModel? = null
) : RuntimeException(errorModel?.message) {

    companion object {
        // Factory Method chính, tạo từ ErrorModel
        fun of(errorModel: ErrorModel): AppException {
            return AppException(errorModel)
        }


        fun of(message: String?): AppException {
            // Tạo ngoại lệ mà không cần ErrorModel, chỉ dựa vào message.
            // Có thể tạo một ErrorModel tạm thời nếu cần
            if (message == null) {
                return AppException(null) // Hoặc AppException(ErrorModel(message = "Lỗi không xác định"))
            }

            // Giả sử bạn muốn tạo một ErrorModel đơn giản chỉ chứa message
            val tempErrorModel = ErrorModel.of(message)
            return AppException(tempErrorModel)
        }
    }
}