package com.ddd.morningbear.myinfo.dto

import com.ddd.morningbear.badge.dto.MdBadgeInfoDto
import com.ddd.morningbear.category.dto.MdCategoryInfoDto
import java.io.Serializable
import java.time.LocalDateTime

data class MpUserInfoDto (
    val accountId: String,
    val nickName: String?,
    var photoLink: String?,
    var memo: String?,
    var wakeUpAt: String?,
    var badgeList: List<MdBadgeInfoDto>? = null,
    var categoryList: List<MdCategoryInfoDto>? = null,
    var updatedAt: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null
): Serializable