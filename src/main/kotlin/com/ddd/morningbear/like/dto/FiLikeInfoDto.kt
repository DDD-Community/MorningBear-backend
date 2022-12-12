package com.ddd.morningbear.like.dto

import com.ddd.morningbear.badge.dto.MdBadgeInfoDto
import java.io.Serializable
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.12.04
 */
data class FiLikeInfoDto(
    val likeCode: String,
    val takenAccountId: String,
    val givenAccountId: String,
    var createdAt: String,
    var updatedBadge: List<MdBadgeInfoDto>? = null,
): Serializable
