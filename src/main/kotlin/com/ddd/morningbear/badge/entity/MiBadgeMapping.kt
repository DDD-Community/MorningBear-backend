package com.ddd.morningbear.badge.entity

import com.ddd.morningbear.badge.dto.MiBadgeMappingDto
import com.ddd.morningbear.badge.entity.pk.MiBadgeMappingPk
import com.ddd.morningbear.myinfo.entity.MpUserInfo
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
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
    @MapsId("accountId")
    val userInfo: MpUserInfo
) {
    fun toDto() = MiBadgeMappingDto(
        accountId = miBadgeMappingPk.accountId,
        badgeId = miBadgeMappingPk.badgeId,
    )
}