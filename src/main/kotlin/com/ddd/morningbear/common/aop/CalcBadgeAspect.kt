package com.ddd.morningbear.common.aop

import com.ddd.morningbear.badge.dto.UserBadgeDetailDto
import com.ddd.morningbear.common.utils.DateUtils
import com.ddd.morningbear.photo.PhotoService
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
class CalcBadgeAspect(
    private val photoService: PhotoService,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Pointcut("execution(* com.ddd.morningbear.badge.*Service.findMyAllBadge(..))")
    fun onFindMyAllBadge() {}

    @AfterReturning(pointcut = "onFindMyAllBadge()", returning = "response")
    fun calcMyBadge(joinPoint: JoinPoint, response: MutableList<UserBadgeDetailDto>) {
        val accountId = joinPoint.args[0].toString()
        val photoInfos = photoService.findAllPhoto(accountId)

        if(!photoInfos.isNullOrEmpty()){
            val photoInfoSize = photoInfos.size

            var period = DateUtils.findPhotoSequenceDays(photoInfos)
            if(photoInfoSize == 1){
                period = 1
            }else{
                // 퍼센트계산을 위해 +1
                period++
            }

            // 미획득 뱃지리스트만 조회
            val notAcquiredBadgeInfo = response.filter { !it.isAcquired }
            notAcquiredBadgeInfo.forEach {
                it.acquirePercent = when(it.badgeId) {
                    "B1" -> 0
                    "B2" -> (period / 3.0) * 100
                    "B3" -> (period / 10.0) * 100
                    "B4" -> (photoInfoSize / 50.0) * 100
                    "B5" -> (photoInfoSize / 100.0) * 100
                    "B6" -> 0
                    "B7" -> 0
                    "B8" -> (photoInfos.filter { x -> x.categoryId.equals("C1") }.size / 3.0) * 100
                    "B9" -> (photoInfos.filter { x -> x.categoryId.equals("C2") }.size / 3.0) * 100
                    "B10" -> (photoInfos.filter { x -> x.categoryId.equals("C3") }.size / 3.0) * 100
                    "B11" -> (photoInfos.filter { x -> x.categoryId.equals("C4") }.size / 3.0) * 100
                    "B12" -> (photoInfos.filter { x -> x.categoryId.equals("C5") }.size / 3.0) * 100
                    else -> 0
                }.toInt()
            }
        }
    }
}