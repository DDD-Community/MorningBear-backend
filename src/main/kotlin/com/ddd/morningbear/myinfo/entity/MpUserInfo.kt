package com.ddd.morningbear.myinfo.entity

import com.ddd.morningbear.badge.entity.MiBadgeMapping
import com.ddd.morningbear.category.entity.MiCategoryMapping
import com.ddd.morningbear.like.entity.FiLikeInfo
import com.ddd.morningbear.myinfo.dto.MpUserInfoDto
import com.ddd.morningbear.myinfo.dto.SearchUserDto
import com.ddd.morningbear.photo.entity.FiPhotoInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    @Column(name = "GOAL", nullable = true)
    val goal: String?,
    @Column(name = "GOAL_UPDATED_AT", nullable = true)
    val goalUpdatedAt: LocalDateTime,
    @Column(name = "UPDATED_AT", nullable = true)
    val updatedAt: LocalDateTime,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime,

    @OneToMany(mappedBy = "takenInfo", cascade = [CascadeType.REMOVE])
    val takenInfo: List<FiLikeInfo>? = null,

    @OneToMany(mappedBy = "givenInfo", cascade = [CascadeType.REMOVE])
    val givenInfo: List<FiLikeInfo>? = null,

    @OneToMany(mappedBy = "userInfo", cascade = [CascadeType.REMOVE])
    val photoInfo: List<FiPhotoInfo>? = null,

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
        goal = this.goal,
        photoInfo = this.photoInfo?.sortedByDescending { it.createdAt }?.map { it.toDto() }?.toMutableList(),
        goalUpdatedAt = this.goalUpdatedAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
    )

    fun toSearchDto() = SearchUserDto(
        accountId = this.accountId,
        nickName = this.nickName,
        photoLink = this.photoLink,
        memo = this.memo,
        wakeUpAt = this.wakeUpAt,
    )
}