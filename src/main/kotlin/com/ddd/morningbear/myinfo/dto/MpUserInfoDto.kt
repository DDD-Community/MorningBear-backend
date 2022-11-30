package com.ddd.morningbear.myinfo.dto

import java.io.Serializable
import java.time.LocalDateTime

data class MpUserInfoDto (
    val accountId: String,
    val nickName: String,
    var photoLink: String?,
    var memo: String?,
    var wakeUpAt: String?,
    var updatedAt: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null
): Serializable