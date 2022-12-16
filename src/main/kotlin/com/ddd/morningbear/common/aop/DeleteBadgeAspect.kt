package com.ddd.morningbear.common.aop

import com.ddd.morningbear.badge.BadgeService
import com.ddd.morningbear.common.context.AuthenticationContextHolder
import com.ddd.morningbear.common.utils.DateUtils
import com.ddd.morningbear.like.LikeService
import com.ddd.morningbear.photo.PhotoService
import com.ddd.morningbear.photo.dto.FiPhotoInfoDto
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

/**
 * @author yoonho
 * @since 2022.12.16
 */
@Aspect
@Component
class DeleteBadgeAspect(
    private val badgeService: BadgeService,
    private val photoService: PhotoService,
    private val likeService: LikeService
) {

    @Pointcut("execution(* com.ddd.morningbear.photo.*Service.deleteMyPhoto(..))")
    fun onDeletePhoto() {}

    @Pointcut("execution(* com.ddd.morningbear.like.*Service.deleteLike(..))")
    fun onDeleteLike() {}

    @AfterReturning(pointcut = "onDeletePhoto()", returning = "response")
    fun checkPhotoBadge(joinPoint: JoinPoint, response: FiPhotoInfoDto) {
        val accountId = AuthenticationContextHolder.getAuthenticationContext().getAccountId()
        val photoInfo = photoService.findAllPhoto(accountId)

        if(photoInfo.isNullOrEmpty()){
            // 미라클모닝 첫시작
            badgeService.deleteMyBadge(accountId, "B1")
        }else{
            // 운동카테고리 3회 이상
            if(photoInfo.filter { it.categoryId.equals("C1") }.size < 3) {
                badgeService.deleteMyBadge(accountId, "B8")
            }
            // 취미카테고리 3회 이상
            if(photoInfo.filter { it.categoryId.equals("C2") }.size < 3) {
                badgeService.deleteMyBadge(accountId, "B9")
            }
            // 생활카테고리 3회 이상
            if(photoInfo.filter { it.categoryId.equals("C3") }.size < 3) {
                badgeService.deleteMyBadge(accountId, "B10")
            }
            // 정서카테고리 3회 이상
            if(photoInfo.filter { it.categoryId.equals("C4") }.size < 3) {
                badgeService.deleteMyBadge(accountId, "B11")
            }
            // 공부카테고리 3회 이상
            if(photoInfo.filter { it.categoryId.equals("C5") }.size < 3) {
                badgeService.deleteMyBadge(accountId, "B12")
            }

            val period = DateUtils.findPeriod(photoInfo.first().createdAt!!, response.createdAt!!)
            if(photoInfo.size >= 9 && period.days == 1) {
                // 미라클모닝 10일 연속
                val period = DateUtils.findPhotoSequenceDays(photoInfo)
                if(period == 8){
                    badgeService.deleteMyBadge(accountId, "B3")
                }
            }else if(photoInfo.size >= 2 && period.days == 1) {
                // 미라클모닝 3일 연속
                val period = DateUtils.findPhotoSequenceDays(photoInfo)
                if(period == 1){
                    badgeService.deleteMyBadge(accountId, "B2")
                }
            }

            if(photoInfo.size < 100) {
                // 미라클모닝 전체 100회
                badgeService.deleteMyBadge(accountId, "B5")
            }else if(photoInfo.size < 50) {
                // 미라클모닝 전체 50회
                badgeService.deleteMyBadge(accountId, "B4")
            }
        }
    }

    @AfterReturning(pointcut = "onDeleteLike()")
    fun checkLikeBadge(joinPoint: JoinPoint) {
        val accountId = joinPoint.args[0].toString()
        val likeInfo = likeService.findGivenInfo(accountId)

        if(likeInfo.isNullOrEmpty()) {
            // 응원하기 1회(최초)
            badgeService.deleteMyBadge(accountId, "B6")
        }
    }
}