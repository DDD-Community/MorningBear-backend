package com.ddd.morningbear.feed

import com.ddd.morningbear.category.CategoryService
import com.ddd.morningbear.category.repository.MdCategoryInfoRepository
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import com.ddd.morningbear.common.exception.GraphQLNotFoundException
import com.ddd.morningbear.feed.dto.FiFeedInfoDto
import com.ddd.morningbear.feed.entity.FiFeedInfo
import com.ddd.morningbear.feed.repository.FiFeedInfoRepository
import com.ddd.morningbear.myinfo.repository.MpUserInfoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Service
class FeedService(
    private val fiFeedInfoRepository: FiFeedInfoRepository,
    private val mdCategoryInfoRepository: MdCategoryInfoRepository,
    private val categoryService: CategoryService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 내 피드 조회
     *
     * @param accountId [String]
     * @return result [FiFeedInfoDto]
     * @author yoonho
     * @since 2022.12.05
     */
    fun findFeed(accountId: String): FiFeedInfoDto {
        var feedInfo = fiFeedInfoRepository.findById(accountId).orElseThrow {
            throw GraphQLNotFoundException("내 피드 조회에 실패하였습니다.")
        }.toDto()

        // 카테고리리스트 조회
        feedInfo.categoryInfo = categoryService.findMyCategory(accountId)

        return feedInfo
    }

    /**
     * 내 피드 저장
     *
     * @param accountId [String]
     * @return result [FiFeedInfoDto]
     * @author yoonho
     * @since 2022.12.05
     */
    fun saveMyFeed(accountId: String): FiFeedInfoDto {
        try{
            return fiFeedInfoRepository.save(
                FiFeedInfo(
                    accountId = accountId,
                    updatedAt = LocalDateTime.now()
                )
            ).toDto()
        }catch (e: Exception) {
            throw GraphQLBadRequestException()
        }
    }
}