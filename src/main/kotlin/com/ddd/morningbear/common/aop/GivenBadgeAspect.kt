package com.ddd.morningbear.common.aop

import com.ddd.morningbear.badge.BadgeService
import com.ddd.morningbear.badge.dto.MdBadgeInfoDto
import com.ddd.morningbear.common.context.AuthenticationContextHolder
import com.ddd.morningbear.like.LikeService
import com.ddd.morningbear.myinfo.dto.MpUserInfoDto
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * @author yoonho
 * @since 2022.12.17
 */
@Aspect
@Component
class GivenBadgeAspect(
    private val likeService: LikeService,
    private val badgeService: BadgeService,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Pointcut("execution(* com.ddd.morningbear.myinfo.*Service.findUserInfo(..))")
    fun onFindUserInfo() {}

    @AfterReturning(pointcut = "onFindUserInfo()", returning = "response")
    fun checkTakenLikeBadge(joinPoint: JoinPoint, response: MpUserInfoDto){
        val updatedBadgeList = mutableListOf<MdBadgeInfoDto>()
        val accountId = AuthenticationContextHolder.getAuthenticationContext().getAccountId()
        val takenLikeInfo = likeService.findTakenInfo(accountId)
        val givenLikeInfo = likeService.findGivenInfo(accountId)

        if(givenLikeInfo.isNullOrEmpty()){
            // 응원하기 1회(최초) 삭제 - Block사용자는 응원하기에서 제외되므로 뱃지이벤트 삭제 Polling 필요
            badgeService.deleteMyBadge(accountId, "B6")
        }

        if(takenLikeInfo.isNullOrEmpty()) {
            // 응원받기 1회(최초) 삭제
            badgeService.deleteMyBadge(accountId, "B7")
            response.badgeList?.filter { it.badgeId.equals("B7") }?.map {
                it.isAcquired = false
                it.acquirePercent = 0
            }
            //
        }else {
            // 응원받기 1회(최초)
            if(takenLikeInfo.size == 1){
                val badgeInfo = badgeService.saveMyBadge(accountId, "B7")
                if(badgeInfo != null){
                    updatedBadgeList.add(badgeInfo)
                    response.badgeList?.filter { it.badgeId.equals("B7") }?.map {
                        it.isAcquired = true
                        it.acquirePercent = 100
                    }
                }
            }
        }

        response.updatedBadge = updatedBadgeList
    }
}