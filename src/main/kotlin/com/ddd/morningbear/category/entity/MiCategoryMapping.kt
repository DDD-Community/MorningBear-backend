package com.ddd.morningbear.category.entity

import com.ddd.morningbear.category.dto.MiCategoryMappingDto
import com.ddd.morningbear.category.entity.pk.MiCategoryMappingPk
import com.ddd.morningbear.feed.entity.FiFeedInfo
import com.ddd.morningbear.myinfo.entity.MpUserInfo
import java.time.LocalDateTime
import javax.persistence.*

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
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
    @MapsId("accountId")
    val userInfo: MpUserInfo,

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
    @MapsId("accountId")
    val feedInfo: FiFeedInfo
) {
    fun toDto() = MiCategoryMappingDto(
        accountId = miCategoryMappingPk.accountId,
        categoryId = miCategoryMappingPk.categoryId
    )
}