package com.ddd.morningbear.category.entity

import com.ddd.morningbear.category.dto.MdCategoryInfoDto
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Entity
@Table(name = "MD_CATEGORY_INFO")
class MdCategoryInfo(
    @Id
    @Column(name = "CATEGORY_ID", nullable = false)
    val categoryId: String,
    @Column(name = "CATEGORY_DESC", nullable = false)
    val categoryDesc: String,
    @Column(name = "UPDATED_AT", nullable = true)
    val updatedAt: LocalDateTime,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now()
) {
    fun toDto() = MdCategoryInfoDto(
        categoryId = this.categoryId,
        categoryDesc = this.categoryDesc
    )
}