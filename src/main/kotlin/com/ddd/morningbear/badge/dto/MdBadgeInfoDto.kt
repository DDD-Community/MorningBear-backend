package com.ddd.morningbear.badge.dto

import java.io.Serializable
import java.time.LocalDateTime

data class MdBadgeInfoDto(
    val badgeId: String,
    val badgeDesc: String,
    val badgeTier: Int?,
    val updatedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime? = null,
): Serializable
