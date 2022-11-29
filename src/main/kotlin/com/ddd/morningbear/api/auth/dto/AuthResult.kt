package com.ddd.morningbear.api.auth.dto

data class AuthResult(
    val redirectUri: String,
    val appKey: String,
    val state: String
)
