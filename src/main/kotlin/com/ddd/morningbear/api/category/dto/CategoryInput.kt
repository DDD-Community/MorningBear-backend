package com.ddd.morningbear.api.category.dto

import java.io.Serializable

/**
 * @author yoonho
 * @since 2022.12.04
 */
data class CategoryInput(
    var categoryId: String,
    var categoryDesc: String
): Serializable
