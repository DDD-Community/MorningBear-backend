package com.ddd.morningbear.category.dto

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column

data class MiCategoryMappingDto(
    val accountId: String,
    val categoryId: String,
    val updatedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime? = null
): Serializable