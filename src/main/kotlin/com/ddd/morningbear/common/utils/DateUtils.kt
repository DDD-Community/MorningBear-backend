package com.ddd.morningbear.common.utils

import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
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
        var week = LocalDate.now().dayOfWeek.value
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
        var startTime = this.setStringToTime(startDt)
        var endTime = this.setStringToTime(endDt)

        return Duration.between(startTime, endTime)
    }

    /**
     * 문자열 시간형식(HHmm)을 시간,분 단위로 절삭
     *
     * @param paramDt [String]
     * @return result [Map]
     * @author yoonho
     * @since 2022.12.10
     */
    fun setStringToMap(paramDt: String): Map<String, Int> {
        return mapOf(
            "hour" to paramDt.substring(0,2).toInt(),
            "min" to paramDt.substring(2,4).toInt()
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
        var timeMap = this.setStringToMap(time)
        return LocalTime.of(timeMap.getOrDefault("hour", 0), timeMap.getOrDefault("min", 0), 0)
    }
}