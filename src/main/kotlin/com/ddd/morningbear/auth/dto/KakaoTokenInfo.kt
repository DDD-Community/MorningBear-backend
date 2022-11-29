package com.ddd.morningbear.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author yoonho
 * @since 2022.11.29
 */
data class KakaoTokenInfo(
    val id: Long,
    @JsonProperty("expires_in")
    var expiresIn: Int?,
    @JsonProperty("expiresInMillis")
    var expiresInMillis: Int?,
    @JsonProperty("appId")
    var appId: Int?,

    val code: Int?,
    val msg: String?,
)