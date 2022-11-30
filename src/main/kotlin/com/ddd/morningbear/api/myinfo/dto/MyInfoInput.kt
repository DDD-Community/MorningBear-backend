package com.ddd.morningbear.api.myinfo.dto

import java.io.Serializable
import java.time.LocalDateTime

data class MyInfoInput(
    val accountId: String,
    val nickName: String,
    var photoLink: String?,
    var memo: String?,
    var wakeUpAt: String?
): Serializable
