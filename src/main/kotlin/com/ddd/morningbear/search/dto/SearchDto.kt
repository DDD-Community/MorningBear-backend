package com.ddd.morningbear.search.dto

import java.io.Serializable

/**
 * @author yoonho
 * @since 2022.12.14
 */
data class SearchDto(
    val lastBuildDate: String?,
    val total: String?,
    val start: String?,
    val display: String?,
    val items: List<SearchItem>?,
): Serializable {
    data class SearchItem(
        var title: String?,
        val link: String?,
        var description: String?,
        val bloggername: String?,
        val bloggerlink: String?,
        val postdate: String?
    )
}