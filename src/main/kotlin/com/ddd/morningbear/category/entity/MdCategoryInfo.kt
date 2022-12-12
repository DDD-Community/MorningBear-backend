package com.ddd.morningbear.category.entity

import com.ddd.morningbear.category.dto.MdCategoryInfoDto
import com.ddd.morningbear.photo.entity.FiPhotoInfo
import java.time.LocalDateTime
import javax.persistence.*

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
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @OneToMany(mappedBy = "categoryInfo", cascade = [CascadeType.REMOVE])
    val photoInfo: List<FiPhotoInfo>? = null,
) {
    fun toDto() = MdCategoryInfoDto(
        categoryId = this.categoryId,
        categoryDesc = this.categoryDesc
    )
}