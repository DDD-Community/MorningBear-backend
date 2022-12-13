package com.ddd.morningbear.like.dto

import java.io.Serializable

/**
 * @author yoonho
 * @since 2022.12.04
 */
data class PopularLikeDto(
    val accountId: String,
    var likeCnt: Int? = 0
): Serializable
