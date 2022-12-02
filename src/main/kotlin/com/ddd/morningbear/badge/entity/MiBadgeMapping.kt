package com.ddd.morningbear.badge.entity

import com.ddd.morningbear.badge.dto.MiBadgeMappingDto
import com.ddd.morningbear.badge.entity.pk.MiBadgeMappingPk
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "MI_BADGE_MAPPING")
class MiBadgeMapping(
    @EmbeddedId
    val miBadgeMappingPk: MiBadgeMappingPk,
    @Column(name = "UPDATED_AT", nullable = true)
    val updatedAt: LocalDateTime,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime,
) {
    fun toDto() = MiBadgeMappingDto(
        accountId = miBadgeMappingPk.accountId,
        badgeId = miBadgeMappingPk.badgeId,
    )
}