package com.ddd.morningbear.myinfo.dto

import java.io.Serializable
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.12.13
 */
data class SearchUserDto (
    val accountId: String,
    val nickName: String?,
    var photoLink: String?,
    var memo: String?,
    var wakeUpAt: String?,
    var updatedAt: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null
): Serializable