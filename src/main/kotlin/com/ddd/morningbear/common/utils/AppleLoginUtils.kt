package com.ddd.morningbear.common.utils

import com.ddd.morningbear.login.dto.ApplePublicKey
import com.ddd.morningbear.login.dto.ApplePublicKeys
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import java.util.*

/**
 * @author yoonho
 * @since 2022.12.11
 */
object AppleLoginUtils {

    fun findMatchedKey(keys: ApplePublicKeys, idToken: String): ApplePublicKey? {
        val headerIdentityToken = idToken.substring(0, idToken.indexOf("."))
        val header = ObjectMapper().readValue(String(Base64.getDecoder().decode(headerIdentityToken), Charsets.UTF_8), Map::class.java)
        val identityTokenMap =  mapOf(
                                    "kid" to header["kid"].toString(),
                                    "alg" to header["alg"].toString()
                                )
        val key = keys.keys.stream().filter { x -> x.kid.equals(identityTokenMap["kid"]) && x.alg.equals(identityTokenMap["alg"]) }.findFirst().orElseGet(null)
        if(key != null){
            return key
        }
        return null
    }

    fun parseToken(token: String): String {
        return Jwts.parser()
            .setSigningKey(Base64.getEncoder().encodeToString(AppPropsUtils.findSecretKey("jwt").toByteArray()))
            .parseClaimsJws(token)
            .body["accountId"].toString()
    }
}