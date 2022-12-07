package com.ddd.morningbear.api.auth

import com.ddd.morningbear.api.auth.dto.AuthResult
import com.ddd.morningbear.common.annotation.SkipTokenCheck
import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.utils.AppPropsUtils
import com.ddd.morningbear.common.utils.ParseUtils
import org.springframework.graphql.data.method.annotation.Argument
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
            ),
            AuthResult(
                redirectUri = "/login/token",
                jsKey = AppPropsUtils.findJsKeyByType(CommCode.Social.APPLE.code)!!,
                nativeKey = AppPropsUtils.findNativeKeyByType(CommCode.Social.APPLE.code)!!,
                state = CommCode.Social.APPLE.code
            )
        )
    }

    /**
     * 토큰정보 인코딩
     *
     * @param state [String]
     * @param token [String]
     * @return result [String]
     * @author yoonho
     * @since 2022.12.07
     */
    @SkipTokenCheck
    @QueryMapping
    fun encode(@Argument state: String, @Argument token: String): String {
        return ParseUtils.encode(state, token)
    }

    /**
     * 토큰정보 디코딩 - 개발자용
     *
     * @param token [String]
     * @return result [String]
     * @author yoonho
     * @since 2022.12.07
     */
    @SkipTokenCheck
    @QueryMapping
    fun decode(@Argument token: String): String {
        return ParseUtils.decode(token)
    }
}