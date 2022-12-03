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

@RestController
class BadgeController(
    private val badgeService: BadgeService
): BaseController() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @QueryMapping
    fun findAllBadge(): List<MdBadgeInfoDto> {
        return badgeService.findAllBadge()
    }

    @QueryMapping
    fun findMyBadgeMapping(): List<MiBadgeMappingDto> {
        val accountId = getAuthenticationContextAccountId()
        return badgeService.findMyBadgeMapping(accountId)
    }

    @MutationMapping
    fun saveMyBadge(@Argument input: List<String>): List<MdBadgeInfoDto> {
        val accountId = getAuthenticationContextAccountId()
        return badgeService.saveMyBadge(accountId, input)
    }

    @MutationMapping
    fun saveBadge(@Argument input: List<BadgeInput>): List<MdBadgeInfoDto> {
        return badgeService.saveBadge(input)
    }
}