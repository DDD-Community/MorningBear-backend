package com.ddd.morningbear.api.feed

import com.ddd.morningbear.common.BaseController
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import com.ddd.morningbear.feed.FeedService
import com.ddd.morningbear.feed.dto.FiFeedInfoDto
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author yoonho
 * @since 2022.12.04
 */
@RestController
class FeedController(
    private val feedService: FeedService
): BaseController() {

    /**
     * 내 피드 조회
     *
     * @return result [FiFeedInfoDto]
     * @author yoonho
     * @since 2022.12.05
     */
    @QueryMapping
    fun findMyFeed(): FiFeedInfoDto {
        val accountId = getAuthenticationContextAccountId()
        return feedService.findFeed(accountId)
    }

    @QueryMapping
    fun findUserFeed(@Argument input: String): FiFeedInfoDto {
        return feedService.findFeed(input)
    }

//    /**
//     * 내 피드 저장
//     *
//     * @return result [FiFeedInfoDto]
//     * @author yoonho
//     * @since 2022.12.05
//     */
//    @MutationMapping
//    fun saveMyFeed(): FiFeedInfoDto {
//        val accountId = getAuthenticationContextAccountId()
//        return feedService.saveMyFeed(accountId)
//    }
}