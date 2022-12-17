package com.ddd.morningbear.myinfo.dto

import com.ddd.morningbear.badge.dto.MdBadgeInfoDto
import com.ddd.morningbear.badge.dto.UserBadgeDetailDto
import com.ddd.morningbear.category.dto.MdCategoryInfoDto
import com.ddd.morningbear.like.dto.FiLikeInfoDto
import com.ddd.morningbear.photo.dto.FiPhotoInfoDto
import com.ddd.morningbear.report.dto.ReportDto
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
    val goal: String?,
    var badgeList: MutableList<UserBadgeDetailDto>? = null,
    var categoryList: List<MdCategoryInfoDto>? = null,
    val takenLike: List<FiLikeInfoDto>?,
    val takenLikeCnt: Int?,
    val givenLike: List<FiLikeInfoDto>?,
    val givenLikeCnt: Int?,
    var photoInfo: MutableList<FiPhotoInfoDto>? = mutableListOf(),
    var photoInfoByCategory: MutableList<PhotoInfoByCategory> = mutableListOf(),
    var reportInfo: ReportDto? = null,
    var updatedBadge: List<MdBadgeInfoDto>? = null,
    var updatedAt: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null
): Serializable {
    data class PhotoInfoByCategory(
        val categoryId: String,
        val categoryDesc: String,
        val photoInfo: List<FiPhotoInfoDto>
    ): Serializable
}