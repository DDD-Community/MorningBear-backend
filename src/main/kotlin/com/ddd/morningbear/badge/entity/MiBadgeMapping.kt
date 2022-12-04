package com.ddd.morningbear.badge.entity

import com.ddd.morningbear.badge.dto.MiBadgeMappingDto
import com.ddd.morningbear.badge.entity.pk.MiBadgeMappingPk
import java.time.LocalDateTime
import javax.persistence.*

/**
 * @author yoonho
 * @since 2022.11.19
 */
@Entity
@Table(name = "MI_BADGE_MAPPING")
class MiBadgeMapping(
    @EmbeddedId
    val miBadgeMappingPk: MiBadgeMappingPk,
    @Column(name = "UPDATED_AT", nullable = true)
    val updatedAt: LocalDateTime,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now()
) {
    fun toDto() = MiBadgeMappingDto(
        accountId = miBadgeMappingPk.accountId,
        badgeId = miBadgeMappingPk.badgeId,
    )
}