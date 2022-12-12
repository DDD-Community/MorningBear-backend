package com.ddd.morningbear.badge.entity

import com.ddd.morningbear.badge.dto.MdBadgeInfoDto
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Size

/**
 * @author yoonho
 * @since 2022.11.19
 */
@Entity
@Table(name = "MD_BADGE_INFO")
class MdBadgeInfo(
    @Id
    @Column(name = "BADGE_ID", nullable = false)
    val badgeId: String,
    @Column(name = "BADGE_TITLE", nullable = false)
    val badgeTitle: String,
    @Column(name = "BADGE_DESC", nullable = false)
    val badgeDesc: String,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now()
) {
    fun toDto() = MdBadgeInfoDto(
        badgeId = this.badgeId,
        badgeTitle = this.badgeTitle,
        badgeDesc = this.badgeDesc
    )
}