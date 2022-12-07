package com.ddd.morningbear.api.badge

import com.ddd.morningbear.api.badge.dto.BadgeInput
import com.ddd.morningbear.badge.BadgeService
import com.ddd.morningbear.badge.dto.MdBadgeInfoDto
import com.ddd.morningbear.badge.dto.MiBadgeMappingDto
import com.ddd.morningbear.common.BaseController
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author yoonho
 * @since 2022.12.04
 */
@RestController
class BadgeController(
    private val badgeService: BadgeService
): BaseController() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 전체 뱃지 조회
     *
     * @return List [MdBadgeInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    @QueryMapping
    fun findAllBadge(): List<MdBadgeInfoDto> {
        return badgeService.findAllBadge()
    }

    /**
     * 내 뱃지 조회(매핑테이블) - 개발자용
     *
     * @return List [MiBadgeMappingDto]
     * @author yoonho
     * @since 2022.12.04
     */
    @QueryMapping
    fun findMyBadgeMapping(): List<MiBadgeMappingDto> {
        val accountId = getAuthenticationContextAccountId()
        return badgeService.findMyBadgeMapping(accountId)
    }

    /**
     * 내 뱃지 저장
     *
     * @param input [List][String]
     * @return List [MdBadgeInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    @MutationMapping
    fun saveMyBadge(@Argument badgeIdList: List<String>): List<MdBadgeInfoDto> {
        val accountId = getAuthenticationContextAccountId()
        return badgeService.saveMyBadge(accountId, badgeIdList)
    }

    /**
     * 뱃지 메타정보 저장 - 개발자용
     *
     * @param input [List][BadgeInput]
     * @return List [MdBadgeInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    @MutationMapping
    fun saveBadge(@Argument badgeInputList: List<BadgeInput>): List<MdBadgeInfoDto> {
        return badgeService.saveBadge(badgeInputList)
    }
}