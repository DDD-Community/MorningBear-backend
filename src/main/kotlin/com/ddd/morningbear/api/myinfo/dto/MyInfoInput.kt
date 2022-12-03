package com.ddd.morningbear.api.myinfo.dto

import java.io.Serializable

data class MyInfoInput(
    var nickName: String?,
    var photoLink: String?,
    var memo: String?,
    var wakeUpAt: String?
): Serializable
