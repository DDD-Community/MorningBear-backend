package com.ddd.morningbear.feed

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
    private val mpUserInfoRepository: MpUserInfoRepository
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
    fun findMyFeed(accountId: String): FiFeedInfoDto {
        return fiFeedInfoRepository.findById(accountId).orElseThrow { throw GraphQLNotFoundException("내 피드 조회에 실패하였습니다.") }.toDto()
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
        return fiFeedInfoRepository.save(
            FiFeedInfo(
                accountId = accountId,
                updatedAt = LocalDateTime.now()
            )
        ).toDto()
    }
}