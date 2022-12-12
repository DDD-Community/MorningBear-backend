package com.ddd.morningbear.api.badge.dto

import java.io.Serializable

/**
 * @author yoonho
 * @since 2022.12.04
 */
data class BadgeInput(
    var badgeId: String,
    var badgeTitle: String,
    var badgeDesc: String
): Serializable
