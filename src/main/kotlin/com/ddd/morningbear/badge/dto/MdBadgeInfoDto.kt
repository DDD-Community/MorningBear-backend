package com.ddd.morningbear.badge.dto

import java.io.Serializable
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.11.19
 */
data class MdBadgeInfoDto(
    val badgeId: String,
    val badgeTitle: String,
    val badgeDesc: String
): Serializable
