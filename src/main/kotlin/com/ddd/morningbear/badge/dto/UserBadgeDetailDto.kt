package com.ddd.morningbear.badge.dto

import java.io.Serializable

/**
 * @author yoonho
 * @since 2022.12.16
 */
data class UserBadgeDetailDto(
    val badgeId: String,
    val badgeTitle: String,
    val badgeDesc: String,
    var isAcquired: Boolean,
    var acquirePercent: Int? = 0
): Serializable
