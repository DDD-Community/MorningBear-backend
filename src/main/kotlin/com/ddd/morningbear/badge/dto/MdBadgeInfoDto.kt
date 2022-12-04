package com.ddd.morningbear.badge.dto

import java.io.Serializable
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.11.19
 */
data class MdBadgeInfoDto(
    val badgeId: String,
    val badgeDesc: String,
    val badgeTier: Int?,
    val updatedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime? = null
): Serializable
