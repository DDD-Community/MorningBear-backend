package com.ddd.morningbear.api.auth.dto

import java.io.Serializable

/**
 * @author yoonho
 * @since 2022.12.04
 */
data class AuthResult(
    val redirectUri: String,
    val jsKey: String,
    val nativeKey: String,
    val state: String
): Serializable
