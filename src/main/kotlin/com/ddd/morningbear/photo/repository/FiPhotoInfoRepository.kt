package com.ddd.morningbear.photo.repository

import com.ddd.morningbear.photo.entity.FiPhotoInfo
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * @author yoonho
 * @since 2022.12.06
 */
interface FiPhotoInfoRepository: JpaRepository<FiPhotoInfo, String> {
    fun findByPhotoIdAndUserInfoAccountId(photoId: String, accountId: String): Optional<FiPhotoInfo>
    fun findAllByUserInfoAccountId(accountId: String): Optional<List<FiPhotoInfo>>
    fun findAllByCategoryInfoCategoryIdAndUserInfoAccountId(categoryId: String, accountId: String): Optional<List<FiPhotoInfo>>
}