package com.ddd.morningbear.feed.dto

import com.ddd.morningbear.category.dto.MdCategoryInfoDto
import com.ddd.morningbear.category.dto.MiCategoryMappingDto
import com.ddd.morningbear.category.entity.MdCategoryInfo
import com.ddd.morningbear.like.dto.FiLikeInfoDto
import com.ddd.morningbear.photo.dto.FiPhotoInfoDto
import java.io.Serializable
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.12.04
 */
data class FiFeedInfoDto(
    val accountId: String,
    val takenLike: List<FiLikeInfoDto>?,
    val takenLikeCnt: Int?,
    val givenLike: List<FiLikeInfoDto>?,
    val givenLikeCnt: Int?,
    val photoInfo: List<FiPhotoInfoDto>?,
    var categoryInfo: List<MdCategoryInfoDto>? = null,
    var updatedAt: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null
): Serializable
