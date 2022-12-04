package com.ddd.morningbear.api.auth

import com.ddd.morningbear.api.auth.dto.AuthResult
import com.ddd.morningbear.common.annotation.SkipTokenCheck
import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.utils.AppPropsUtils
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author yoonho
 * @since 2022.12.04
 */
@RestController
class AuthController {

    /**
     * 로그인 기본정보 조회
     *
     * @return List [AuthResult]
     * @author yoonho
     * @since 2022.12.04
     */
    @SkipTokenCheck
    @QueryMapping
    fun findLoginInfo(): List<AuthResult> {
        return  listOf(
            AuthResult(
                redirectUri = "/login/token",
                jsKey = AppPropsUtils.findJsKeyByType(CommCode.Social.KAKAO.code)!!,
                nativeKey = AppPropsUtils.findNativeKeyByType(CommCode.Social.KAKAO.code)!!,
                state = CommCode.Social.KAKAO.code
            ),
            AuthResult(
                redirectUri = "/login/token",
                jsKey = AppPropsUtils.findJsKeyByType(CommCode.Social.NAVER.code)!!,
                nativeKey = AppPropsUtils.findNativeKeyByType(CommCode.Social.NAVER.code)!!,
                state = CommCode.Social.NAVER.code
            )
        )
    }
}