package com.ddd.morningbear.api.login.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author yoonho
 * @since 2022.11.29
 */
data class LoginInput(
    var state: String?,
    var code: String?,
    var error: String?,
    @JsonProperty("error_description")
    var errorDescription: String?
)