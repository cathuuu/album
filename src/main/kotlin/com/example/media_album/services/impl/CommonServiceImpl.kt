package com.example.media_album.services.impl

import com.example.media_album.exceptions.AppException
import com.example.media_album.repositories.CommonRepository
import com.example.media_album.services.CommonService
import java.io.Serializable


open class CommonServiceImpl<E, ID : Serializable, R : CommonRepository<E, ID>>(
    protected val repo: R
) : CommonService<E, ID> {
    override fun getAll(): MutableList<E?>? {
        return repo.findAll()
    }

    override fun getById(id: ID?): E? {
        id ?: return null

        val entityOptional = repo.findById(id)

        // 2. Ném lỗi CHỈ KHI KHÔNG TÌM THẤY (is not present)
        if (entityOptional.isEmpty) throw AppException.of("Document not found") // Hoặc dùng !entityOptional.isPresent

        // 3. Trả về đối tượng
        return entityOptional.get()
    }

    override fun save(document: E?): E? {
        return repo.save(document!!)
    }

    override fun saveAll(documents: MutableList<E?>?): MutableList<E?>? {
        return repo.saveAll(documents!!)
    }

    override fun existsById(id: ID?): Boolean {
        return repo.existsById(id!!)
    }

    override fun deleteById(id: ID?) {
        repo.deleteById(id!!)
    }

    override fun deleteByIdIn(ids: MutableList<ID?>?) {
        repo.deleteByIdIn(ids)
    }

}