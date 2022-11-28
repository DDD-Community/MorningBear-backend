package com.ddd.morningbear.login.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author yoonho
 * @since 2022.11.29
 */
data class TokenInfo(
    @JsonProperty("access_token")
    var accessToken: String?,
    @JsonProperty("expires_in")
    var expiresIn: Int?,
    @JsonProperty("refresh_token")
    var refreshToken: String?,
    @JsonProperty("refresh_token_expires_in")
    var refreshExpiresIn: Int?,
    @JsonProperty("token_type")
    var tokenType: String?,

    var error: String?,
    @JsonProperty("error_description")
    var errorDescription: String?
)