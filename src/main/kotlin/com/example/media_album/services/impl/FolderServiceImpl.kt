package com.example.media_album.services.impl

import com.example.media_album.models.documents.FolderDocument
import com.example.media_album.models.dtos.input.FolderInput
import com.example.media_album.repositories.FolderRepository
import com.example.media_album.repositories.MediaRepository
import com.example.media_album.repositories.UserRepository
import com.example.media_album.services.FolderService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.text.Normalizer
import java.time.Instant

@Service
class FolderServiceImpl(
    repo: FolderRepository,
    private val userRepository: UserRepository,
    private val mediaRepository: MediaRepository
) : CommonServiceImpl<FolderDocument, ObjectId, FolderRepository>(repo), FolderService {

    @Value("\${media.upload.base-path}")
    private lateinit var basePath: String

    override fun saveFolder(folderDocument: FolderInput?): FolderDocument {
        if (folderDocument == null) {
            throw IllegalArgumentException("Folder input cannot be null")
        }

        // 🔹 1. Lấy user
        val user = userRepository.findById(ObjectId(folderDocument.ownerId))
            .orElseThrow { RuntimeException("User not found") }

        // 🔹 2. Lấy folder cha (nếu có)
        val parentFolder = folderDocument.parentId?.let {
            repo.findById(ObjectId(it)).orElse(null)
        }

        // 🔹 3. Sinh đường dẫn logic lưu trong DB (dùng tên gốc để hiển thị đẹp)
        val logicalPath = if (parentFolder != null)
            "${parentFolder.path}/${folderDocument.name}"
        else
            "${user.id}/${folderDocument.name}"

        // 🔹 4. Tạo đường dẫn vật lý an toàn (bỏ dấu, ký tự lạ)
        val physicalSafeName = normalizeFolderName(folderDocument.name!!)
        val physicalPath = if (parentFolder != null)
            "$basePath/${parentFolder.path}/$physicalSafeName"
        else
            "$basePath/${user.id}/$physicalSafeName"

        createPhysicalFolder(physicalPath)
        println("Base path: $basePath")

        // 🔹 5. Tạo document mới
        val newFolder = FolderDocument(
            ownerId = user.id,
            name = folderDocument.name, // vẫn giữ tên gốc để hiển thị đẹp
            parentId = parentFolder?.id,
            coverUrl = folderDocument.coverUrl,
            isShared = folderDocument.isShared,
            path = logicalPath,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        // 🔹 6. Lưu vào MongoDB
        return repo.save(newFolder)
    }

    // ✅ Chuẩn hóa tên thư mục: bỏ dấu, bỏ ký tự đặc biệt
    private fun normalizeFolderName(name: String): String {
        val normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
        return normalized
            .replace("[^\\p{ASCII}]".toRegex(), "") // bỏ dấu tiếng Việt
            .replace("[^a-zA-Z0-9-_]".toRegex(), "_") // thay ký tự đặc biệt bằng "_"
            .trim()
    }

    // ✅ Tạo thư mục vật lý, ném lỗi nếu thất bại
    private fun createPhysicalFolder(path: String) {
        val folder = File(path)
        if (!folder.exists() && !folder.mkdirs()) {
            throw RuntimeException("Failed to create physical folder: $path")
        }
    }

    override fun updateFolder(folderDocument: FolderInput): FolderDocument? {
        val existingFolder = repo.findById(ObjectId(folderDocument.id))
            .orElseThrow { RuntimeException("Folder not found") }

        val user = userRepository.findById(ObjectId(folderDocument.ownerId))
            .orElseThrow { RuntimeException("User not found") }

        val parentFolder = folderDocument.parentId?.let {
            repo.findById(ObjectId(it)).orElse(null)
        }

        val newPath = if (parentFolder != null)
            "${parentFolder.path}/${folderDocument.name}"
        else
            "/${folderDocument.name}"

        val updatedFolder = existingFolder.copy(
            name = folderDocument.name!!,
            parentId = parentFolder?.id,
            ownerId = user.id,
            coverUrl = folderDocument.coverUrl,
            isShared = folderDocument.isShared,
            path = newPath,
            updatedAt = Instant.now()
        )

        return repo.save(updatedFolder)
    }

    override fun findRootFoldersByOwnerId(ownerId: ObjectId): List<FolderDocument> {
        return repo.findByOwnerIdAndParentIdIsNull(ownerId)
    }

    override fun findSubFoldersByParentId(parentId: ObjectId): List<FolderDocument> {
        return repo.findByParentId(parentId)
    }

    override fun findByFolderName(folderName: String): List<FolderDocument?> {
        return repo.findByFolderName(folderName)
    }

    override fun findByUserIdAndIsDeletedTrue(userId: ObjectId): List<FolderDocument> {
        return repo.findByOwnerIdAndIsDeletedTrue(userId)
    }
}
