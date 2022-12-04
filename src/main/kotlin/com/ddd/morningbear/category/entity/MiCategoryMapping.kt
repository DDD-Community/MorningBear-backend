package com.ddd.morningbear.category.entity

import com.ddd.morningbear.category.dto.MiCategoryMappingDto
import com.ddd.morningbear.category.entity.pk.MiCategoryMappingPk
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Entity
@Table(name = "MI_CATEGORY_MAPPING")
class MiCategoryMapping(
    @EmbeddedId
    val miCategoryMappingPk: MiCategoryMappingPk,
    @Column(name = "UPDATED_AT", nullable = true)
    val updatedAt: LocalDateTime,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now()
) {
    fun toDto() = MiCategoryMappingDto(
        accountId = miCategoryMappingPk.accountId,
        categoryId = miCategoryMappingPk.categoryId
    )
}