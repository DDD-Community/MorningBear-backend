package com.ddd.morningbear.myinfo.entity

import com.ddd.morningbear.badge.entity.MiBadgeMapping
import com.ddd.morningbear.category.entity.MiCategoryMapping
import com.ddd.morningbear.feed.entity.FiFeedInfo
import com.ddd.morningbear.myinfo.dto.MpUserInfoDto
import java.time.LocalDateTime
import javax.persistence.*

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Entity
@Table(name = "MP_USER_INFO")
class MpUserInfo(
    @Id
    @Column(name = "ACCOUNT_ID", nullable = false)
    val accountId: String,
    @Column(name = "NICKNAME", nullable = true)
    val nickName: String?,
    @Column(name = "PROFILE_PHOTO_LINK", nullable = true)
    val photoLink: String?,
    @Column(name = "MEMO", nullable = true)
    val memo: String?,
    @Column(name = "WAKE_UP_AT", nullable = true)
    val wakeUpAt: String?,
    @Column(name = "UPDATED_AT", nullable = true)
    val updatedAt: LocalDateTime? = null,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "ACCOUNT_ID")
    val fiFeedInfo: FiFeedInfo? = null,

    @OneToMany(mappedBy = "userInfo", cascade = [CascadeType.REMOVE])
    val categoryInfo: List<MiCategoryMapping>? = null,

    @OneToMany(mappedBy = "userInfo", cascade = [CascadeType.REMOVE])
    val badgeInfo: List<MiBadgeMapping>? = null
) {
    fun toDto() = MpUserInfoDto(
        accountId = this.accountId,
        nickName = this.nickName,
        photoLink = this.photoLink,
        memo = this.memo,
        wakeUpAt = this.wakeUpAt,
    )
}