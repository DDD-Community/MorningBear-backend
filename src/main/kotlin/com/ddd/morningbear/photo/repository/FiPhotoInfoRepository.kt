package com.ddd.morningbear.photo.repository

import com.ddd.morningbear.photo.entity.FiPhotoInfo
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * @author yoonho
 * @since 2022.12.06
 */
interface FiPhotoInfoRepository: JpaRepository<FiPhotoInfo, String> {

    fun findAllByCategoryInfoCategoryIdAndFeedInfoAccountId(categoryId: String, accountId: String): Optional<List<FiPhotoInfo>>
    fun findAllByFeedInfoAccountId(accountId: String): Optional<List<FiPhotoInfo>>
    fun findByPhotoIdAndFeedInfoAccountId(photoId: String, accountId: String): Optional<FiPhotoInfo>
}