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

    // TODO:
    //  - (Search) graphql방식으로 변경
    //  - (Search) 파라미터로 어떤 조건으로 검색할지(블로그,웹문서 등의 docsType / 결과갯수 : display)
    //  - "완료" (Photo) 사진삭제시 뱃지이벤트 미달성시 삭제 (AOP)
    //  - (Badge) 뱃지 달성을 위한 퍼센테이지 리턴
    //  - (MyInfo) 주로하는 카테고리 추가작
    //  - "완료" (Badge) 뱃지이벤트 최종안으로 수정

    @SkipTokenCheck
    @GetMapping("/search")
    fun search(): SearchDto {
        return searchService.search()
    }
}