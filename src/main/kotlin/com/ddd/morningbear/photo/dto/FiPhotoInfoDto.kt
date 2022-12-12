package com.ddd.morningbear.photo.dto

import com.ddd.morningbear.badge.dto.MdBadgeInfoDto
import java.io.Serializable

/**
 * @author yoonho
 * @since 2022.12.06
 */
data class FiPhotoInfoDto(
    val photoId: String?,
    val photoLink: String?,
    val photoDesc: String?,
    val accountId: String?,
    val categoryId: String?,
    val endAt: String?,
    val startAt: String?,
    var updatedBadge: List<MdBadgeInfoDto>? = null,
    val updatedAt: String?,
    var createdAt: String?
): Serializable