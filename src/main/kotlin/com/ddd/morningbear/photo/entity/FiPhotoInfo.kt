package com.ddd.morningbear.photo.entity

import com.ddd.morningbear.category.entity.MdCategoryInfo
import com.ddd.morningbear.photo.dto.FiPhotoInfoDto
import com.ddd.morningbear.feed.entity.FiFeedInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.*

/**
 * @author yoonho
 * @since 2022.12.06
 */
@Entity
@Table(name = "FI_PHOTO_INFO")
class FiPhotoInfo(
    @Id
    @Column(name = "PHOTO_ID", nullable = true)
    val photoId: String,

    @Column(name = "PHOTO_LINK", nullable = true)
    val photoLink: String,
    @Column(name = "PHOTO_DESC", nullable = true)
    val photoDesc: String,

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    val feedInfo: FiFeedInfo,

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    val categoryInfo: MdCategoryInfo,

    @Column(name = "END_AT", nullable = true)
    val endAt: String,
    @Column(name = "START_AT", nullable = true)
    val startAt: String,
    @Column(name = "UPDATED_AT", nullable = true)
    val updatedAt: LocalDateTime,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(),
) {
    fun toDto() = FiPhotoInfoDto(
        photoId = this.photoId,
        photoLink = this.photoLink,
        photoDesc = this.photoDesc,
        accountId = this.feedInfo.accountId,
        categoryId = this.categoryInfo.categoryId,
        startAt = this.startAt,
        endAt = this.endAt,
        updatedAt = this.updatedAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
    )
}