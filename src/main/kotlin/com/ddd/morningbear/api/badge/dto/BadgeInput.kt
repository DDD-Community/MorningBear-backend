package com.ddd.morningbear.api.badge.dto

import java.io.Serializable

data class BadgeInput(
    var badgeId: String,
    var badgeDesc: String,
    var badgeTier: Int
): Serializable
