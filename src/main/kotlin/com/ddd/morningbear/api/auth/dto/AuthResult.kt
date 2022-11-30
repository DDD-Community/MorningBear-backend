package com.ddd.morningbear.api.auth.dto

data class AuthResult(
    val redirectUri: String,
    val jsKey: String,
    val nativeKey: String,
    val state: String
)
