package com.ddd.morningbear.login.dto

import java.io.Serializable

data class ApplePublicKeys (
    val keys: List<ApplePublicKey>
): Serializable

data class ApplePublicKey (
    val kty: String,
    val kid: String,
    val use: String,
    val alg: String,
    val n: String,
    val e: String
): Serializable


