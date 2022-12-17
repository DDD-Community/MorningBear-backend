package com.ddd.morningbear.common.aop

import com.ddd.morningbear.badge.BadgeService
import com.ddd.morningbear.badge.dto.MdBadgeInfoDto
import com.ddd.morningbear.badge.dto.UserBadgeDetailDto
import com.ddd.morningbear.common.context.AuthenticationContextHolder
import com.ddd.morningbear.common.utils.DateUtils
import com.ddd.morningbear.like.LikeService
import com.ddd.morningbear.like.dto.FiLikeInfoDto
import com.ddd.morningbear.myinfo.dto.MpUserInfoDto
import com.ddd.morningbear.photo.PhotoService
import com.ddd.morningbear.photo.dto.FiPhotoInfoDto
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * @author yoonho
 * @since 2022.12.16
 */
@Aspect
@Component
class SaveBadgeAspect(
    private val badgeService: BadgeService,
    private val photoService: PhotoService,
    private val likeService: LikeService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Pointcut("execution(* com.ddd.morningbear.photo.*Service.saveMyPhoto(..))")
    fun onSavePhoto() {}

    @Pointcut("execution(* com.ddd.morningbear.like.*Service.saveLike(..))")
    fun onSaveLike() {}

    @AfterReturning(pointcut = "onSavePhoto()", returning = "response")
    fun checkPhotoBadge(joinPoint: JoinPoint, response: FiPhotoInfoDto) {
        val accountId = response.accountId!!
        val photoInfo = photoService.findAllPhoto(accountId)
        if(!photoInfo.isNullOrEmpty()){

            val updatedBadgeList = mutableListOf<MdBadgeInfoDto>()

            // 미라클모닝 첫시작
            if(photoInfo.size == 1){
                val badgeInfo = badgeService.saveMyBadge(accountId, "B1")
                if(badgeInfo != null){
                    updatedBadgeList.add(badgeInfo)
                }
            }

            // 미라클모닝 전체 50회
            if(photoInfo.size == 50){
                val badgeInfo = badgeService.saveMyBadge(accountId, "B4")
                if(badgeInfo != null){
                    updatedBadgeList.add(badgeInfo)
                }
            }

            // 미라클모닝 전체 100회
            if(photoInfo.size == 100){
                val badgeInfo = badgeService.saveMyBadge(accountId, "B5")
                if(badgeInfo != null){
                    updatedBadgeList.add(badgeInfo)
                }
            }

            if(photoInfo.size >= 10) {
                // 미라클모닝 10일 연속
                val period = DateUtils.findPhotoSequenceDays(photoInfo)
                if(period == 9) {
                    val badgeInfo = badgeService.saveMyBadge(accountId, "B3")
                    if(badgeInfo != null){
                        updatedBadgeList.add(badgeInfo)
                    }
                }
            }else if(photoInfo.size >= 3) {
                // 미라클모닝 3일 연속
                val period = DateUtils.findPhotoSequenceDays(photoInfo)
                if(period == 2) {
                    val badgeInfo = badgeService.saveMyBadge(accountId, "B2")
                    if(badgeInfo != null){
                        updatedBadgeList.add(badgeInfo)
                    }
                }

                // 운동카테고리 3회 이상
                if(photoInfo.filter { it.categoryId.equals("C1") }.size == 3) {
                    val badgeInfo = badgeService.saveMyBadge(accountId, "B8")
                    if(badgeInfo != null){
                        updatedBadgeList.add(badgeInfo)
                    }
                }

                // 취미카테고리 3회 이상
                if(photoInfo.filter { it.categoryId.equals("C2") }.size == 3) {
                    val badgeInfo = badgeService.saveMyBadge(accountId, "B9")
                    if(badgeInfo != null){
                        updatedBadgeList.add(badgeInfo)
                    }
                }

                // 생활카테고리 3회 이상
                if(photoInfo.filter { it.categoryId.equals("C3") }.size == 3) {
                    val badgeInfo = badgeService.saveMyBadge(accountId, "B10")
                    if(badgeInfo != null){
                        updatedBadgeList.add(badgeInfo)
                    }
                }

                // 정서카테고리 3회 이상
                if(photoInfo.filter { it.categoryId.equals("C4") }.size == 3) {
                    val badgeInfo = badgeService.saveMyBadge(accountId, "B11")
                    if(badgeInfo != null){
                        updatedBadgeList.add(badgeInfo)
                    }
                }

                // 공부카테고리 3회 이상
                if(photoInfo.filter { it.categoryId.equals("C5") }.size == 3) {
                    val badgeInfo = badgeService.saveMyBadge(accountId, "B12")
                    if(badgeInfo != null){
                        updatedBadgeList.add(badgeInfo)
                    }
                }
            }

            response.updatedBadge = updatedBadgeList
        }
    }

    @AfterReturning(pointcut = "onSaveLike()", returning = "response")
    fun checkLikeBadge(joinPoint: JoinPoint, response: FiLikeInfoDto) {

        val updatedBadgeList = mutableListOf<MdBadgeInfoDto>()
        val accountId = joinPoint.args[0].toString()
        val likeInfo = likeService.findGivenInfo(accountId)

        if(!likeInfo.isNullOrEmpty()){
            // 응원하기 1회(최초)
            if(likeInfo.size == 1){
                val badgeInfo = badgeService.saveMyBadge(accountId, "B6")
                if(badgeInfo != null){
                    updatedBadgeList.add(badgeInfo)
                }
            }
            response.updatedBadge = updatedBadgeList
        }
    }
}