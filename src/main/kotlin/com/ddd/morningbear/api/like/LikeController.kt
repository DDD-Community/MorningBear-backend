package com.ddd.morningbear.api.like

import com.ddd.morningbear.common.BaseController
import com.ddd.morningbear.like.LikeService
import com.ddd.morningbear.like.dto.FiLikeInfoDto
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author yoonho
 * @since 2022.12.05
 */
@RestController
class LikeController(
    private val likeService: LikeService
): BaseController() {

    /**
     * 응원하기 등록
     *
     * @param takenAccountId [String]
     * @return result [Boolean]
     * @author yoonho
     * @since 2022.12.05
     */
    @MutationMapping
    fun saveLike(@Argument takenAccountId: String): FiLikeInfoDto {
        val accountId = getAuthenticationContextAccountId()
        return likeService.saveLike(accountId, takenAccountId)
    }

    /**
     * 응원하기 취소
     *
     * @param takenAccountId [String]
     * @return result [Boolean]
     * @author yoonho
     * @since 2022.12.05
     */
    @MutationMapping
    fun deleteLike(@Argument takenAccountId: String): Boolean {
        val accountId = getAuthenticationContextAccountId()
        return likeService.deleteLike(accountId, takenAccountId)
    }
}