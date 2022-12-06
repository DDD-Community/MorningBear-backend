package com.ddd.morningbear.feed.entity

import com.ddd.morningbear.category.entity.MiCategoryMapping
import com.ddd.morningbear.feed.dto.FiFeedInfoDto
import com.ddd.morningbear.like.entity.FiLikeInfo
import com.ddd.morningbear.photo.entity.FiPhotoInfo
import java.time.LocalDateTime
import javax.persistence.*

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Entity
@Table(name = "FI_FEED_INFO")
class FiFeedInfo(
    @Id
    @Column(name = "ACCOUNT_ID", nullable = false)
    val accountId: String,
    @Column(name = "UPDATED_AT", nullable = true)
    val updatedAt: LocalDateTime,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @OneToMany(mappedBy = "takenInfo", cascade = [CascadeType.REMOVE])
    val takenInfo: List<FiLikeInfo>? = null,

    @OneToMany(mappedBy = "givenInfo", cascade = [CascadeType.REMOVE])
    val givenInfo: List<FiLikeInfo>? = null,

    @OneToMany(mappedBy = "feedInfo", cascade = [CascadeType.REMOVE])
    val photoInfo: List<FiPhotoInfo>? = null,

    @OneToMany(mappedBy = "feedInfo", cascade = [CascadeType.REMOVE])
    val categoryInfo: List<MiCategoryMapping>? = null,
) {
    fun toDto() = FiFeedInfoDto(
        accountId = this.accountId,
        takenLike = this.takenInfo?.map { it.toDto() },
        takenLikeCnt = this.takenInfo?.size,
        givenLike = this.givenInfo?.map { it.toDto() },
        givenLikeCnt = this.givenInfo?.size,
        photoInfo = this.photoInfo?.map { it.toDto() }
    )
}