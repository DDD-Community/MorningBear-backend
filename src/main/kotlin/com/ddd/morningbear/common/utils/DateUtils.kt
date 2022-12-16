package com.ddd.morningbear.common.utils

import com.ddd.morningbear.photo.dto.FiPhotoInfoDto
import org.slf4j.LoggerFactory
import java.time.*
import java.time.format.DateTimeFormatter

/**
 * @author yoonho
 * @since 2022.12.10
 */
object DateUtils {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 현재기준 지난주 일요일 계산
     *
     * @return result [LocalDate]
     * @author yoonho
     * @since 2022.12.10
     */
    fun findSunday(): LocalDate {
        val week = LocalDate.now().dayOfWeek.value
        return LocalDate.now().minusDays(week.toLong())
    }

    /**
     * 두 날짜간 Duration 계산
     *
     * @param startDt [String]
     * @param endDt [String]
     * @return result [Duration]
     * @author yoonho
     * @since 2022.12.10
     */
    fun findDuration(startDt: String, endDt: String): Duration {
        val startTime = this.setStringToTime(startDt)
        val endTime = this.setStringToTime(endDt)

        return Duration.between(startTime, endTime)
    }

    /**
     * 두 날짜간 Period 계산
     *
     * @param startDt [String]
     * @param endDt [String]
     * @return result [Period]
     * @author yoonho
     * @since 2022.12.12
     */
    fun findPeriod(startDt: String, endDt: String): Period {
        val startDate = this.setStringToDate(startDt)
        val endDate = this.setStringToDate(endDt)

        return Period.between(startDate, endDate)
    }

    /**
     * 문자열 시간형식(HHmm)을 시간,분 단위로 절삭
     *
     * @param paramDt [String]
     * @return result [Map]
     * @author yoonho
     * @since 2022.12.10
     */
    fun setStringToTimeMap(paramDt: String): Map<String, Int> {
        return mapOf(
            "hour" to paramDt.substring(0,2).toInt(),
            "min" to paramDt.substring(2,4).toInt()
        )
    }

    /**
     * 문자열 날짜형식(yyyyMMdd)을 연도,월,일 단위로 절삭
     *
     * @param paramDt [String]
     * @return result [Map]
     * @author yoonho
     * @since 2022.12.12
     */
    fun setStringToDateMap(paramDt: String): Map<String, Int> {
        return mapOf(
            "year" to paramDt.substring(0,4).toInt(),
            "month" to paramDt.substring(4,6).toInt(),
            "day" to paramDt.substring(6,8).toInt(),
        )
    }

    /**
     * LocalTime형식을 문자열 시간형식(HHmm)으로 파싱
     *
     * @param time [LocalTime]
     * @return result [String]
     * @author yoonho
     * @since 2022.12.10
     */
    fun setTimeToString(time: LocalTime): String {
        return time.format(DateTimeFormatter.ofPattern("HHmm"))
    }

    /**
     * 문자열 시간형식(HHmm)을 LocalTime으로 파싱
     *
     * @param time [String]
     * @return result [LocalTime]
     * @author yoonho
     * @since 2022.12.10
     */
    fun setStringToTime(time: String): LocalTime {
        val timeMap = this.setStringToTimeMap(time)
        return LocalTime.of(timeMap.getOrDefault("hour", 0), timeMap.getOrDefault("min", 0), 0)
    }

    /**
     * 문자열 날짜형식(yyyyMMdd)을 LocalDate로 파싱
     *
     * @param date [String]
     * @return result [LocalTime]
     * @author yoonho
     * @since 2022.12.12
     */
    fun setStringToDate(date: String): LocalDate {
        val dateMap = this.setStringToDateMap(date)
        return LocalDate.of(dateMap.getOrDefault("year", 0), dateMap.getOrDefault("month", 0), dateMap.getOrDefault("day", 0))
    }

    /**
     * 사진리스트의 연속된 생성일자 갯수 조회
     *
     * @param photoInfos [FiPhotoInfoDto]
     * @return result [Int]
     * @author yoonho
     * @since 2022.12.16
     */
    fun findPhotoSequenceDays(photoInfos: List<FiPhotoInfoDto>): Int {
        var cnt = 0
        for((idx) in photoInfos.withIndex()) {
            if(idx+1 >= photoInfos.size) return cnt

            if(this.findPeriod(photoInfos.get(idx+1).createdAt!!, photoInfos.get(idx).createdAt!!).days == 1) {
                cnt++
            }else{
                return cnt
            }
        }
        return cnt
    }
}