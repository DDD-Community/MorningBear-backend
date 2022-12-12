package com.ddd.morningbear.config

import com.ddd.morningbear.auth.AuthService
import com.ddd.morningbear.badge.BadgeService
import com.ddd.morningbear.badge.dto.MdBadgeInfoDto
import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.context.AuthenticationContext
import com.ddd.morningbear.common.context.AuthenticationContextHolder
import com.ddd.morningbear.common.exception.*
import com.ddd.morningbear.common.utils.AppPropsUtils
import com.ddd.morningbear.common.utils.AppleLoginUtils
import com.ddd.morningbear.common.utils.DateUtils
import com.ddd.morningbear.common.utils.ParseUtils
import com.ddd.morningbear.like.LikeService
import com.ddd.morningbear.like.dto.FiLikeInfoDto
import com.ddd.morningbear.myinfo.dto.MpUserInfoDto
import com.ddd.morningbear.photo.PhotoService
import com.ddd.morningbear.photo.dto.FiPhotoInfoDto
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
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
    private val authService: AuthService,
    private val badgeService: BadgeService,
    private val photoService: PhotoService,
    private val likeService: LikeService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Pointcut("execution(* com.ddd.morningbear.api.*.*Controller.*(..)) && !@annotation(com.ddd.morningbear.common.annotation.SkipTokenCheck)")
    fun onRequest() {}

    @Pointcut("execution(* com.ddd.morningbear.photo.*Service.saveMyPhoto(..))")
    fun onSavePhoto() {}

    @Pointcut("execution(* com.ddd.morningbear.like.*Service.saveLike(..))")
    fun onSaveLike() {}

    @Pointcut("execution(* com.ddd.morningbear.myinfo.*Service.findUserInfo(..))")
    fun onFindUserInfo() {}


    @Around("onRequest()")
    fun checkToken(joinPoint: ProceedingJoinPoint): Any {
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
            var decodedAccountId = ParseUtils.decode(accountId)

            var socialType = when {
                decodedAccountId.lowercase().startsWith(CommCode.Social.KAKAO.prefix) -> CommCode.Social.KAKAO.code
                decodedAccountId.lowercase().startsWith(CommCode.Social.NAVER.prefix) -> CommCode.Social.NAVER.code
                decodedAccountId.lowercase().startsWith(CommCode.Social.APPLE.prefix) -> CommCode.Social.APPLE.code
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
            val accessToken = authorization.substring(7)
            val decodedToken = ParseUtils.decode(accessToken)
            when {
                decodedToken.lowercase().startsWith(CommCode.Social.KAKAO.prefix) -> {
                    val token = ParseUtils.removePrefix(CommCode.Social.KAKAO.code, decodedToken)
                    accountId = ParseUtils.encode(CommCode.Social.KAKAO.code, authService.kakaoAuth(token)!!)
                }
                decodedToken.lowercase().startsWith(CommCode.Social.NAVER.prefix) -> {
                    val token = ParseUtils.removePrefix(CommCode.Social.NAVER.code, decodedToken)
                    accountId = ParseUtils.encode(CommCode.Social.NAVER.code, authService.naverAuth(token)!!)
                }
                decodedToken.lowercase().startsWith(CommCode.Social.APPLE.prefix) -> {
                    val token = ParseUtils.removePrefix(CommCode.Social.APPLE.code, decodedToken)
                    accountId = ParseUtils.encode(CommCode.Social.APPLE.code, AppleLoginUtils.parseToken(token))
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

    @AfterReturning(pointcut = "onSavePhoto()", returning = "response")
    fun checkPhotoBadge(joinPoint: JoinPoint, response: FiPhotoInfoDto) {
        val accountId = response.accountId!!
        val photoInfo = photoService.findAllPhoto(accountId)
        if(!photoInfo.isNullOrEmpty()){

            val updatedBadgeList = mutableListOf<MdBadgeInfoDto>()

            // 미라클모닝 첫시작
            if(photoInfo.size == 1){
                val badgeInfo = badgeService.saveMyBadge(accountId, "B2")
                if(badgeInfo != null){
                    updatedBadgeList.add(badgeInfo)
                }
            }

            if(photoInfo.size >= 3) {
                // 운동카테고리 3회 이상
                if(photoInfo.filter { it.categoryId.equals("C1") }.size == 3) {
                    val badgeInfo = badgeService.saveMyBadge(accountId, "B6")
                    if(badgeInfo != null){
                        updatedBadgeList.add(badgeInfo)
                    }
                }

                // 미라클모닝 3일 연속
                var period = DateUtils.findPeriod(photoInfo.last().createdAt!!, photoInfo[photoInfo.size-3].createdAt!!)
                if(period.days == 3){
                    val badgeInfo = badgeService.saveMyBadge(accountId, "B3")
                    if(badgeInfo != null){
                        updatedBadgeList.add(badgeInfo)
                    }
                }
            }


            if(photoInfo.size >= 10) {
                // 운동카테고리 10회 이상
                if(photoInfo.filter { it.categoryId.equals("C1") }.size == 10) {
                    val badgeInfo = badgeService.saveMyBadge(accountId, "B7")
                    if(badgeInfo != null){
                        updatedBadgeList.add(badgeInfo)
                    }
                }

                // 미라클모닝 10일 연속
                var period = DateUtils.findPeriod(photoInfo.last().createdAt!!, photoInfo[photoInfo.size-10].createdAt!!)
                if(period.days == 10){
                    val badgeInfo = badgeService.saveMyBadge(accountId, "B4")
                    if(badgeInfo != null){
                        updatedBadgeList.add(badgeInfo)
                    }
                }
            }

            // 미라클모닝 전체 100회
            if(photoInfo.size == 10){
                val badgeInfo = badgeService.saveMyBadge(accountId, "B5")
                if(badgeInfo != null){
                    updatedBadgeList.add(badgeInfo)
                }
            }

            response.updatedBadge = updatedBadgeList
        }
    }

    @AfterReturning("onSaveLike()", returning = "response")
    fun checkLikeBadge(joinPoint: JoinPoint, response: FiLikeInfoDto) {

        val updatedBadgeList = mutableListOf<MdBadgeInfoDto>()
        val accountId = joinPoint.args[0].toString()
        val likeInfo = likeService.findGivenInfo(accountId)

        if(!likeInfo.isNullOrEmpty()){
            // 응원하기 1회(최초)
            if(likeInfo.size == 1){
                val badgeInfo = badgeService.saveMyBadge(accountId, "B9")
                if(badgeInfo != null){
                    updatedBadgeList.add(badgeInfo)
                }
            }

            // 응원하기 5회이상
            if(likeInfo.size == 5){
                val badgeInfo = badgeService.saveMyBadge(accountId, "B8")
                if(badgeInfo != null){
                    updatedBadgeList.add(badgeInfo)
                }
            }

            response.updatedBadge = updatedBadgeList
        }
    }

    @AfterReturning("onFindUserInfo()", returning = "response")
    fun checkTakenLikeBadge(joinPoint: JoinPoint, response: MpUserInfoDto){

        val updatedBadgeList = mutableListOf<MdBadgeInfoDto>()
        val accountId = AuthenticationContextHolder.getAuthenticationContext().getAccountId()

        val likeInfo = likeService.findTakenInfo(accountId)

        if(!likeInfo.isNullOrEmpty()){
            // 응원받기 1회(최초)
            if(likeInfo.size == 1){
                val badgeInfo = badgeService.saveMyBadge(accountId, "B10")
                if(badgeInfo != null){
                    updatedBadgeList.add(badgeInfo)
                }
            }

            // 응원기 5회이상
            if(likeInfo.size == 5){
                val badgeInfo = badgeService.saveMyBadge(accountId, "B11")
                if(badgeInfo != null){
                    updatedBadgeList.add(badgeInfo)
                }
            }

            response.updatedBadge = updatedBadgeList
            response.badgeList?.addAll(updatedBadgeList)
        }
    }
}