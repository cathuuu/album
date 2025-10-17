package com.example.media_album.services.impl

import com.example.media_album.codegen.types.PermissionInput
import com.example.media_album.models.documents.PermissionDocument
import com.example.media_album.models.documents.FolderShareDocument
import com.example.media_album.repositories.*
import com.example.media_album.services.PermissionService
import com.example.media_album.utils.getByIdOrThrow
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.collections.emptyList

@Service
class PermissionServiceImpl(
    repo: PermissionRepository,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val folderShareRepository: FolderShareRepository,
    private val mediaShareRepository: MediaShareRepository,
    private val mediaRepository: MediaRepository,
    private val folderRepository: FolderRepository,
    private val permissionRepository: PermissionRepository
) : CommonServiceImpl<PermissionDocument, ObjectId, PermissionRepository>(repo),
    PermissionService {

    // ===================================================
    // 🔹 CRUD cơ bản
    // ===================================================

    override fun updatePermission(permissionDocument: PermissionInput): PermissionDocument {
        val id = permissionDocument.id ?: throw IllegalArgumentException("Permission ID is required!")
        val existing = repo.getByIdOrThrow(id, "Permission")
        val updated = existing.copy(
            permissionName = permissionDocument.permissionName,
            updatedAt = Instant.now()
        )
        return repo.save(updated)
    }

    override fun createPermission(permissionDocument: PermissionInput): PermissionDocument {
        val newPermission = PermissionDocument(
            permissionName = permissionDocument.permissionName
        )
        return repo.save(newPermission)
    }

    // ===================================================
    // 🔹 PHÂN QUYỀN NÂNG CAO
    // ===================================================

    /**
     * ✅ Check tổng hợp quyền:
     * 1️⃣ ADMIN hoặc ROLE có quyền ALL → true
     * 2️⃣ Owner của media → true
     * 3️⃣ MediaShare trực tiếp → true
     * 4️⃣ FolderShare (có kế thừa, có deny) → true/false
     * 5️⃣ Mặc định → false
     */
    override fun checkPermission(
        userId: ObjectId,
        mediaId: ObjectId,
        requiredPermission: String
    ): Boolean {
        val user = userRepository.findById(userId).orElse(null) ?: return false
        val roles = if (user.roleIds.isNotEmpty()) roleRepository.findAllById(user.roleIds) else emptyList()

        // Gom tất cả permissionId từ các role
        val permissionIds = roles.flatMap { it.permissions }
            .filter { it.isNotBlank() }
            .map { ObjectId(it) } // Chuyển từ String sang ObjectId

        // Truy vấn thực tế để lấy danh sách quyền
        val permissions = if (permissionIds.isNotEmpty())
            permissionRepository.findAllById(permissionIds)
        else
            emptyList()

        // Lấy danh sách tên quyền (ở đây ví dụ dùng id hoặc field name)
        val rolePermissions = permissions.map { it.id!!.toHexString().uppercase() }.distinct()

        // Nếu có quyền ALL hoặc đúng quyền yêu cầu thì pass
        if ("ALL" in rolePermissions || requiredPermission.uppercase() in rolePermissions)
            return true

        // ===== 2️⃣ Kiểm tra quyền Owner =====
        val media = mediaRepository.findById(mediaId).orElse(null) ?: return false
        if (media.user == userId) return true

        // ===== 3️⃣ Kiểm tra quyền chia sẻ trực tiếp trên media =====
        val directShare = mediaShareRepository.findByMediaAndSharedWith(mediaId, userId)
        if (directShare != null) {
            // Nếu có DENY trước thì chặn luôn
            if (directShare.permission.any { it.equals("DENY_${requiredPermission.uppercase()}", true) })
                return false
            if (directShare.permission.any { it.equals(requiredPermission, true) })
                return true
        }

        // ===== 4️⃣ Kiểm tra quyền kế thừa từ folder =====
        val folderId = media.folder ?: return false
        return hasFolderInheritedPermission(userId, folderId, requiredPermission)
    }

    /**
     * ✅ Kiểm tra quyền kế thừa theo cây folder
     * - Dừng khi gặp quyền trực tiếp (inherited = false)
     * - Nếu có DENY → chặn luôn
     * - Nếu không có → tiếp tục kiểm tra cha
     */
    override fun hasFolderInheritedPermission(
        userId: ObjectId,
        folderId: ObjectId?,
        permission: String
    ): Boolean {
        val visited = mutableSetOf<ObjectId>() // tránh vòng lặp
        var currentFolderId = folderId

        while (currentFolderId != null) {
            // Ngăn vòng tham chiếu
            if (!visited.add(currentFolderId)) {
                throw IllegalStateException("Circular folder reference detected at folder: $currentFolderId")
            }

            val share = folderShareRepository.findByFolderAndSharedWith(currentFolderId, userId)

            // Nếu có quyền DENY → chặn ngay
            if (share?.permission?.any { it.equals("DENY_${permission.uppercase()}", true) } == true)
                return false

            // Nếu có quyền trực tiếp (inherited = false)
            if (share != null && !share.inherited) {
                return share.permission.any { it.equals(permission, true) }
            }

            // Nếu có quyền kế thừa (inherited = true)
            if (share?.permission?.any { it.equals(permission, true) } == true)
                return true

            // Truy lên folder cha
            val parent = folderRepository.findById(currentFolderId).orElse(null)
            currentFolderId = parent?.parentId
        }

        return false
    }


    @Transactional
    override fun inheritPermissionsFromFolder(
        parentId: ObjectId,
        sharedBy: ObjectId,
        sharedWith: ObjectId,
        permissions: List<String>
    ) {
        val subFolders = folderRepository.findByParentId(parentId)

        for (subFolder in subFolders) {
            // 2️⃣ Nếu folder con chưa có quyền riêng thì tạo bản ghi kế thừa
            val existingShare = folderShareRepository.findByFolderAndSharedWith(subFolder.id!!, sharedWith)

            if (existingShare == null) {
                val inheritedShare = FolderShareDocument(
                    folder = subFolder.id,
                    sharedBy = sharedBy,
                    sharedWith = sharedWith,
                    permission = permissions,
                    inherited = true // ✅ Đánh dấu là quyền kế thừa
                )
                folderShareRepository.save(inheritedShare)
            }

            // 3️⃣ Đệ quy xuống tầng con
            inheritPermissionsFromFolder(subFolder.id, sharedBy, sharedWith, permissions)
        }
    }

    /**
     * ✅ Áp dụng quyền kế thừa cho các folder con (đệ quy an toàn)
     * - Chỉ tạo nếu folder con chưa có share riêng
     * - Có kiểm tra vòng lặp
     */
    override fun applyInheritedPermission(
        folderId: ObjectId,
        sharedBy: ObjectId,
        sharedWith: ObjectId,
        permissions: List<String>,
        visited: MutableSet<ObjectId>
    ) {
        if (!visited.add(folderId)) return // tránh vòng lặp

        val subFolders = folderRepository.findByParentId(folderId)
        for (sub in subFolders) {
            val exists = folderShareRepository.findByFolderAndSharedWith(sub.id!!, sharedWith)
            if (exists == null) {
                val newShare = FolderShareDocument(
                    folder = sub.id,
                    sharedBy = sharedBy,
                    sharedWith = sharedWith,
                    inherited = true,
                    permission = permissions
                )
                folderShareRepository.save(newShare)
            }
            // Đệ quy xuống tầng con
            applyInheritedPermission(sub.id, sharedBy, sharedWith, permissions, visited)
        }
    }

    /**
     * ✅ Thu hồi quyền kế thừa khi folder cha bị thu hồi
     * - Chỉ xóa quyền có inherited = true
     * - Đệ quy xuống tầng con
     */
    override fun revokeInheritedPermission(folderId: ObjectId, sharedWith: ObjectId, visited: MutableSet<ObjectId>) {
        if (!visited.add(folderId)) return

        val subFolders = folderRepository.findByParentId(folderId)
        for (sub in subFolders) {
            val share = folderShareRepository.findByFolderAndSharedWith(sub.id!!, sharedWith)
            if (share != null && share.inherited) {
                folderShareRepository.delete(share)
            }
            revokeInheritedPermission(sub.id, sharedWith, visited)
        }
    }
}
