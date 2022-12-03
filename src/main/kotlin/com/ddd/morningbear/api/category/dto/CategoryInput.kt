package com.ddd.morningbear.api.category.dto

import java.io.Serializable

data class CategoryInput(
    var categoryId: String,
    var categoryDesc: String
): Serializable
