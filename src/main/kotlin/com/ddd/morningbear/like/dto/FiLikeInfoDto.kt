package com.ddd.morningbear.like.dto

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
    var updatedAt: String,
    var createdAt: LocalDateTime? = null
): Serializable
