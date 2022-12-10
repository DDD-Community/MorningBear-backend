package com.ddd.morningbear.report.dto

import java.io.Serializable

/**
 * @author yoonho
 * @since 2022.12.10
 */
data class ReportDto(
    val totalTime: Long = 0,
    val totalTimeByCategory: List<CategoryTime>? = null,
    val avgStartDt: String = "",
    val countSucc: Long = 0
): Serializable {
    data class CategoryTime(
        val categoryId: String,
        val categoryDesc: String,
        var totalTime: Long? = 0
    ): Serializable
}
