package com.ddd.morningbear.api.myinfo.dto

import java.io.Serializable

/**
 * @author yoonho
 * @since 2022.12.04
 */
data class MyInfoInput(
    var nickName: String?,
    var photoLink: String?,
    var memo: String?,
    var wakeUpAt: String?
): Serializable
