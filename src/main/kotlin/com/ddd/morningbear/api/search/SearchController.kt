package com.ddd.morningbear.api.search

import com.ddd.morningbear.common.BaseController
import com.ddd.morningbear.search.SearchService
import com.ddd.morningbear.search.dto.SearchDto
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * @author yoonho
 * @since 2022.12.14
 */
@RestController
class SearchController(
    private val searchService: SearchService
): BaseController() {

    /**
     * 아티클 조회하기
     *
     * @param sizeInput [Int]
     * @return List [SearchDto.SearchItem]
     * @author yoonho
     * @since 2022.12.16
     */
    @QueryMapping
    fun searchArticle(@Argument sizeInput: Optional<Int>): List<SearchDto.SearchItem>? {
        val size = findSize(sizeInput)
        return searchService.searchArticle(size)
    }
}