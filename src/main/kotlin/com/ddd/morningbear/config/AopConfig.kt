package com.ddd.morningbear.config

import com.ddd.morningbear.auth.AuthService
import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.context.AuthenticationContext
import com.ddd.morningbear.common.context.AuthenticationContextHolder
import com.ddd.morningbear.common.exception.*
import com.ddd.morningbear.common.utils.AppPropsUtils
import com.ddd.morningbear.common.utils.ParseUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * @author yoonho
 * @since 2022.12.04
 */
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

        // 비로그인 처리
        if(authorization.uppercase().startsWith("AK")){
            var appKey = authorization.substring(3)
            var accountId = request.getHeader("accountId")

            var socialType = when {
                accountId.lowercase().startsWith(CommCode.Social.KAKAO.prefix) -> CommCode.Social.KAKAO.code
                accountId.lowercase().startsWith(CommCode.Social.NAVER.prefix) -> CommCode.Social.NAVER.code
                accountId.lowercase().startsWith(CommCode.Social.APPLE.prefix) -> CommCode.Social.APPLE.code
                else -> ""
            }

            if(AppPropsUtils.isExistRestKey(appKey, socialType)){
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
            }else{
                throw GraphQLBadRequestException("앱키 정보가 올바르지 않습니다")
            }
        }
        //

        if(!authorization.uppercase().startsWith("BEARER") || authorization.length < 7){
            throw GraphQLBadRequestException("헤더 포맷이 올바르지 않습니다")
        }

        lateinit var accountId: String
        try{
            var accessToken = authorization.substring(7)
            var decodedToken = ParseUtils.decode(accessToken)
            when {
                decodedToken.lowercase().startsWith(CommCode.Social.KAKAO.prefix) -> {
                    var token = ParseUtils.removePrefix(CommCode.Social.KAKAO.code, decodedToken)
                    accountId = authService.kakaoAuth(token)!!
                }
                decodedToken.lowercase().startsWith(CommCode.Social.NAVER.prefix) -> {
                    var token = ParseUtils.removePrefix(CommCode.Social.NAVER.code, decodedToken)
                    accountId = authService.naverAuth(token)!!
                }
                decodedToken.lowercase().startsWith(CommCode.Social.APPLE.prefix) -> {
//                    var token = ParseUtils.removePrefix(CommCode.Social.APPLE.code, decodedToken)
//                    accountId = authService.naverAuth(token)!!
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