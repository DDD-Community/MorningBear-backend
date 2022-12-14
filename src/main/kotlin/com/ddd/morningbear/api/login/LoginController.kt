package com.ddd.morningbear.api.login

import com.ddd.morningbear.api.login.dto.LoginInput
import com.ddd.morningbear.common.annotation.SkipTokenCheck
import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.dto.BaseResponse
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import com.ddd.morningbear.common.utils.AppPropsUtils
import com.ddd.morningbear.login.AppleService
import com.ddd.morningbear.login.LoginService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView

/**
 * @author yoonho
 * @since 2022.11.29
 */
@Controller
class LoginController(
    private val loginService: LoginService,
    private val appleService: AppleService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 디버깅용 테스트 로그인페이지 (DEV)
     *
     * @return mv [ModelAndView]
     * @author yoonho
     * @since 2022.11.29
     */
    @SkipTokenCheck
    @GetMapping("/login")
    fun login(): ModelAndView {
        var mv = ModelAndView("login")
        mv.addObject("kakaoJsKey", AppPropsUtils.findJsKeyByType(CommCode.Social.KAKAO.code))
        mv.addObject("naverJsKey", AppPropsUtils.findJsKeyByType(CommCode.Social.NAVER.code))

        return mv
    }

    /**
     * 인가코드를 통한 토큰 획득
     *
     * @param input [LoginInput]
     * @return BaseResponse [BaseResponse]
     * @author yoonho
     * @since 2022.11.29
     */
    @SkipTokenCheck
    @GetMapping("/login/token")
    @ResponseBody
    fun token(input: LoginInput): BaseResponse {
        val socialName = input.state

        lateinit var tokenInfo: Any
        when(socialName) {
            CommCode.Social.KAKAO.code -> tokenInfo = loginService.kakaoToken(input.code, socialName)
            CommCode.Social.NAVER.code -> tokenInfo = loginService.naverToken(input.code, socialName)
            CommCode.Social.APPLE.code -> {
                if(input.identityToken.isNullOrEmpty()){
                    throw GraphQLBadRequestException("애플로그인을 위한 토큰정보가 없습니다.")
                }
                tokenInfo = appleService.appleToken(input.identityToken!!)
            }
        }

        return BaseResponse().success(tokenInfo)
    }
}