package com.ddd.morningbear.badge.dto

import java.io.Serializable
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.11.19
 */
data class MiBadgeMappingDto(
    val accountId: String,
    val badgeId: String,
    val updatedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime? = null,
): Serializable
