package com.ddd.morningbear.category.dto

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column

/**
 * @author yoonho
 * @since 2022.12.04
 */
data class MdCategoryInfoDto(
    val categoryId: String,
    val categoryDesc: String
): Serializable
