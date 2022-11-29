package com.ddd.morningbear.config

import com.ddd.morningbear.auth.AuthService
import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.exception.GraphQLThirdPartyServerException
import com.ddd.morningbear.common.exception.GraphQLTokenInvalidException
import com.ddd.morningbear.common.exception.ThirdPartyServerException
import com.ddd.morningbear.common.exception.TokenInvalidException
import com.ddd.morningbear.common.utils.TokenUtils
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class AopConfig(
    private val authService: AuthService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Before("execution(* com.ddd.morningbear.api.*.*Controller.*(..)) && !@annotation(com.ddd.morningbear.common.annotation.SkipTokenCheck)")
    fun tokenCheck(joinPoint: JoinPoint) {
        var request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        var authorization = request.getHeader("Authorization")

        if(authorization.isNullOrBlank()){
            throw GraphQLTokenInvalidException("토큰정보가 유효하지 않습니다.")
        }
        if(!authorization.uppercase().startsWith("BEARER") || authorization.length < 7){
            throw GraphQLTokenInvalidException("토큰정보가 유효하지 않습니다.")
        }

        try{
            var accessToken = authorization.substring(7)
            when {
                accessToken.uppercase().startsWith("K::") -> {
                    var token = TokenUtils.decodeToken(accessToken, CommCode.Social.KAKAO.code)
                    authService.kakaoAuth(token)
                }
                accessToken.uppercase().startsWith("N::") -> {
                    var token = TokenUtils.decodeToken(accessToken, CommCode.Social.NAVER.code)
                    authService.naverAuth(token)
                }
                else -> throw GraphQLTokenInvalidException("토큰정보가 유효하지 않습니다.")
            }
        }catch (te: TokenInvalidException){
            throw GraphQLTokenInvalidException("토큰정보가 유효하지 않습니다.")
        }catch (tse: ThirdPartyServerException){
            throw GraphQLThirdPartyServerException("소셜로그인 서버를 일시적으로 이용할 수 없습니다.")
        }
    }
}