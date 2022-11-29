package com.ddd.morningbear.api.auth

import com.ddd.morningbear.api.auth.dto.AuthResult
import com.ddd.morningbear.common.annotation.SkipTokenCheck
import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.utils.AppPropsUtils
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController {

    @SkipTokenCheck
    @QueryMapping
    fun findLoginInfo(): List<AuthResult> {
        return  listOf(
            AuthResult(redirectUri = "/login/token", appKey = AppPropsUtils.findJsKeyByType(CommCode.Social.KAKAO.code)!!, state = CommCode.Social.KAKAO.code),
            AuthResult(redirectUri = "/login/token", appKey = AppPropsUtils.findJsKeyByType(CommCode.Social.NAVER.code)!!, state = CommCode.Social.NAVER.code),
        )
    }
}