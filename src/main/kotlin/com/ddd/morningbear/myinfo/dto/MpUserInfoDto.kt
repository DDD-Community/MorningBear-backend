package com.ddd.morningbear.myinfo.dto

import com.ddd.morningbear.badge.dto.MdBadgeInfoDto
import com.ddd.morningbear.category.dto.MdCategoryInfoDto
import com.ddd.morningbear.like.dto.FiLikeInfoDto
import com.ddd.morningbear.photo.dto.FiPhotoInfoDto
import java.io.Serializable
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.12.04
 */
data class MpUserInfoDto (
    val accountId: String,
    val nickName: String?,
    var photoLink: String?,
    var memo: String?,
    var wakeUpAt: String?,
    var badgeList: List<MdBadgeInfoDto>? = null,
    var categoryList: List<MdCategoryInfoDto>? = null,
    val takenLike: List<FiLikeInfoDto>?,
    val takenLikeCnt: Int?,
    val givenLike: List<FiLikeInfoDto>?,
    val givenLikeCnt: Int?,
    val photoInfo: List<FiPhotoInfoDto>?,
    var updatedAt: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null
): Serializable