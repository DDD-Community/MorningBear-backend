package com.ddd.morningbear.report

import com.ddd.morningbear.category.repository.MdCategoryInfoRepository
import com.ddd.morningbear.common.utils.DateUtils
import com.ddd.morningbear.myinfo.repository.MpUserInfoRepository
import com.ddd.morningbear.photo.entity.FiPhotoInfo
import com.ddd.morningbear.report.dto.ReportDto
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.math.roundToLong

/**
 * @author yoonho
 * @since 2022.12.10
 */
@Service
class ReportService(
    private val mpUserInfoRepository: MpUserInfoRepository,
    private val mpCategoryInfoRepository: MdCategoryInfoRepository
) {

    lateinit var photoInfoList: List<FiPhotoInfo>
    val sunday: LocalDate = DateUtils.findSunday()

    /**
     * 리포트 계산
     *
     * @param accountId [String]
     * @return result [ReportDto]
     * @author yoonho
     * @since 2022.12.10
     */
    fun createReport(accountId: String): ReportDto{
        var photoInfoList = mpUserInfoRepository.findById(accountId)
            .map { it.photoInfo }
            .orElseGet(null)

        if(!photoInfoList.isNullOrEmpty()){
            this.photoInfoList = photoInfoList.filter { it.createdAt.toLocalDate().isAfter(sunday) }
        }

        if(photoInfoList.isNullOrEmpty()){
            return ReportDto()
        }

        return ReportDto(
            totalTime = this.totalTime(),
            totalTimeByCategory = this.totalTimeByCategory(),
            avgStartDt = this.avgStartDt(),
            countSucc = this.countSucc()
        )
    }

    /**
     * Report1) 전체 누적시간
     *
     * @return result [Long]
     * @author yoonho
     * @since 2022.12.10
     */
    private fun totalTime(): Long = photoInfoList.map { DateUtils.findDuration(it.startAt, it.endAt).toMinutes() }.sum()

    /**
     * Report2) 카테고리별 누적시간
     *
     * @return result [String]
     * @author yoonho
     * @since 2022.12.10
     */
    private fun totalTimeByCategory(): List<ReportDto.CategoryTime> {
        var categoryInfo = mpCategoryInfoRepository.findAll()
        var categoryReport = categoryInfo.map {
            ReportDto.CategoryTime(
                categoryId = it.categoryId,
                categoryDesc = it.categoryDesc
            )
        }

        categoryReport.map { x ->
            x.totalTime = photoInfoList.filter { it.categoryInfo.categoryId.equals(x.categoryId) }.map { DateUtils.findDuration(it.startAt, it.endAt).toMinutes() }.sum()
        }

        return categoryReport
    }

    /**
     * Report3) 평균 시작시간
     *
     * @return result [String]
     * @author yoonho
     * @since 2022.12.10
     */
    private fun avgStartDt(): String {
        var timeMap = photoInfoList.map { DateUtils.setStringToTime(it.startAt) }
        var avgTime = LocalTime.ofSecondOfDay(Arrays.stream(timeMap.toTypedArray()).mapToInt(LocalTime::toSecondOfDay).average().asDouble.roundToLong())
        return DateUtils.setTimeToString(avgTime)
    }

    /**
     * Report4) 성공횟수
     *
     * @return result [Long]
     * @author yoonho
     * @since 2022.12.10
     */
    private fun countSucc(): Long = photoInfoList.size.toLong()
}