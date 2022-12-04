package com.ddd.morningbear.common.utils

import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.login.dto.TokenInfo
import org.slf4j.LoggerFactory

/**
 * @author yoonho
 * @since 2022.12.04
 */
object TokenUtils {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun encodeToken(token: String?, type: String): String {
        var prefix = when(type) {
            CommCode.Social.KAKAO.code -> "k::"
            CommCode.Social.NAVER.code -> "n::"
            CommCode.Social.NAVER.code -> "n::"
            else -> ""
        }

        return prefix + token
    }

    fun encodeToken(tokenInfo: TokenInfo, type: String): TokenInfo {
        var prefix = when(type) {
            CommCode.Social.KAKAO.code -> "k::"
            CommCode.Social.NAVER.code -> "n::"
            CommCode.Social.NAVER.code -> "n::"
            else -> ""
        }

        tokenInfo?.accessToken = prefix + tokenInfo?.accessToken
        tokenInfo?.refreshToken = prefix + tokenInfo?.refreshToken

        return tokenInfo
    }

    fun decodeToken(token: String, type: String): String {
        var prefix = when(type) {
            CommCode.Social.KAKAO.code -> "k::"
            CommCode.Social.NAVER.code -> "n::"
            CommCode.Social.NAVER.code -> "n::"
            else -> ""
        }

        return token.replace(prefix, "")
    }

    fun decodeToken(tokenInfo: TokenInfo?, type: String): TokenInfo? {
        var prefix = when(type) {
            CommCode.Social.KAKAO.code -> "k::"
            CommCode.Social.NAVER.code -> "n::"
            CommCode.Social.NAVER.code -> "n::"
            else -> ""
        }

        tokenInfo?.accessToken = tokenInfo?.accessToken?.replace(prefix, "")
        tokenInfo?.refreshToken = prefix + tokenInfo?.refreshToken?.replace(prefix, "")

        return tokenInfo
    }
}