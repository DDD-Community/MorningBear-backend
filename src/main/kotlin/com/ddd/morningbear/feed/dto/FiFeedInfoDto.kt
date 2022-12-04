package com.ddd.morningbear.feed.dto

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
    val givenLike: List<FiLikeInfoDto>?,
    val photoInfo: List<FiPhotoInfoDto>?,
    var updatedAt: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null
): Serializable
