package com.ddd.morningbear.api.search

import com.ddd.morningbear.common.annotation.SkipTokenCheck
import com.ddd.morningbear.search.SearchService
import com.ddd.morningbear.search.dto.SearchDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author yoonho
 * @since 2022.12.14
 */
@RestController
class SearchController(
    private val searchService: SearchService
) {

    @SkipTokenCheck
    @GetMapping("/search")
    fun search(): SearchDto {
        return searchService.search()
    }
}