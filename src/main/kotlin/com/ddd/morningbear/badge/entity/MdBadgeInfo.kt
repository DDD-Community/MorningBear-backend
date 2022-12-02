package com.ddd.morningbear.badge.entity

import com.ddd.morningbear.badge.dto.MdBadgeInfoDto
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Size

@Entity
@Table(name = "MD_BADGE_INFO")
class MdBadgeInfo(
    @Id
    @Column(name = "BADGE_ID", nullable = false)
    val badgeId: String,
    @Column(name = "BADGE_DESC", nullable = false)
    val badgeDesc: String,
    @Size(max = 10, message = "Wrong BadgeTier Size")
    @Column(name = "BADGE_TIER", nullable = false)
    val badgeTier: Int,
    @Column(name = "UPDATED_AT", nullable = true)
    val updatedAt: LocalDateTime,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime,
) {
    fun toDto() = MdBadgeInfoDto(
        badgeId = this.badgeId,
        badgeDesc = this.badgeDesc,
        badgeTier = this.badgeTier
    )
}