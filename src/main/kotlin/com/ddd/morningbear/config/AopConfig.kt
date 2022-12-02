package com.ddd.morningbear.config

import com.ddd.morningbear.auth.AuthService
import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.context.AuthenticationContext
import com.ddd.morningbear.common.context.AuthenticationContextHolder
import com.ddd.morningbear.common.exception.*
import com.ddd.morningbear.common.utils.TokenUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
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

    @Around("execution(* com.ddd.morningbear.api.*.*Controller.*(..)) && !@annotation(com.ddd.morningbear.common.annotation.SkipTokenCheck)")
    fun tokenCheck(joinPoint: ProceedingJoinPoint): Any {
        // cucumber 인증용도
        if(AuthenticationContextHolder.isExistAuthenticationContext()){
            return joinPoint.proceed()
        }
        //

        var request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        var authorization = request.getHeader("Authorization")

        if(authorization.isNullOrBlank()){
            throw GraphQLBadRequestException("필수 헤더정보를 입력해주세요")
        }
        if(!authorization.uppercase().startsWith("BEARER") || authorization.length < 7){
            throw GraphQLBadRequestException("헤더 포맷이 올바르지 않습니다")
        }

        lateinit var accountId: String
        try{
            var accessToken = authorization.substring(7)
            when {
                accessToken.uppercase().startsWith("K::") -> {
                    var token = TokenUtils.decodeToken(accessToken, CommCode.Social.KAKAO.code)
                    accountId = TokenUtils.encodeToken(authService.kakaoAuth(token), CommCode.Social.KAKAO.code)
                }
                accessToken.uppercase().startsWith("N::") -> {
                    var token = TokenUtils.decodeToken(accessToken, CommCode.Social.NAVER.code)
                    accountId = TokenUtils.encodeToken(authService.naverAuth(token), CommCode.Social.NAVER.code)
                }
                else -> throw GraphQLTokenInvalidException("토큰정보가 유효하지 않습니다.")
            }
        }catch (te: TokenInvalidException){
            throw GraphQLTokenInvalidException("토큰정보가 유효하지 않습니다.")
        }catch (tse: ThirdPartyServerException){
            throw GraphQLThirdPartyServerException("소셜로그인 서버를 일시적으로 이용할 수 없습니다.")
        }

        // Context 저장
        var context = AuthenticationContext
        context.setAccountId(accountId)
        AuthenticationContextHolder.setAuthenticationContext(context)

        // 타겟객체 실행
        var result = joinPoint.proceed()
        //

        // Context 삭제
        AuthenticationContextHolder.removeAuthenticationContext()

        return result
    }
}