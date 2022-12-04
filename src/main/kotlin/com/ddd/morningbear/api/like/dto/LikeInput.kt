package com.ddd.morningbear.api.like.dto

import java.io.Serializable

/**
 * @author yoonho
 * @since 2022.12.05
 */
data class LikeInput(
    var takenAccountId: String,
    var likeCode: String
): Serializable
