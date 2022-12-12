package com.ddd.morningbear.category.dto

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column

/**
 * @author yoonho
 * @since 2022.12.04
 */
data class MiCategoryMappingDto(
    val accountId: String,
    val categoryId: String
): Serializable