package com.ddd.morningbear.badge

import com.ddd.morningbear.api.badge.dto.BadgeInput
import com.ddd.morningbear.badge.dto.MdBadgeInfoDto
import com.ddd.morningbear.badge.dto.MiBadgeMappingDto
import com.ddd.morningbear.badge.entity.MdBadgeInfo
import com.ddd.morningbear.badge.entity.MiBadgeMapping
import com.ddd.morningbear.badge.entity.pk.MiBadgeMappingPk
import com.ddd.morningbear.badge.repository.MdBadgeInfoRepository
import com.ddd.morningbear.badge.repository.MiBadgeMappingRepository
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.stream.Collectors

@Service
class BadgeService(
    private val miBadgeMappingRepository: MiBadgeMappingRepository,
    private val mdBadgeInfoRepository: MdBadgeInfoRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun findAllBadge(): List<MdBadgeInfoDto> = mdBadgeInfoRepository.findAll().map { it.toDto() }

    fun findMyBadgeMapping(accountId: String): List<MiBadgeMappingDto> = miBadgeMappingRepository.findAllByMiBadgeMappingPkAccountId(accountId).map { it.toDto() }

    fun findMyBadge(accountId: String): List<MdBadgeInfoDto> {
        var badgeIdList = miBadgeMappingRepository.findAllByMiBadgeMappingPkAccountId(accountId).map { it.miBadgeMappingPk.badgeId }.stream().collect(Collectors.toList())
        return mdBadgeInfoRepository.findAllById(badgeIdList).map { it.toDto() }
    }

    fun saveMyBadge(accountId: String, input: List<String>): List<MdBadgeInfoDto> {
        var badgeList = mdBadgeInfoRepository.findAllById(input)
        if(input.size != badgeList.size){
            throw GraphQLBadRequestException("존재하지 않은 카테고리ID 입니다.")
        }

        badgeList.stream().forEach {
            x -> miBadgeMappingRepository.save(
                MiBadgeMapping(
                    miBadgeMappingPk = MiBadgeMappingPk(
                        accountId = accountId,
                        badgeId = x.badgeId
                    ),
                    updatedAt = LocalDateTime.now()
                )
            )
        }

        return this.findMyBadge(accountId)
    }

    fun saveBadge(input: List<BadgeInput>): List<MdBadgeInfoDto> {
        input.stream().forEach {
            x -> mdBadgeInfoRepository.save(
                    MdBadgeInfo(
                        badgeId = x.badgeId,
                        badgeDesc = x.badgeDesc,
                        badgeTier = x.badgeTier,
                        updatedAt = LocalDateTime.now()
                    )
                )
        }

        return this.findAllBadge()
    }

    fun deleteMyBadge(accountId: String) {
        val badgeList = miBadgeMappingRepository.findAllByMiBadgeMappingPkAccountId(accountId)
        badgeList.stream().forEach {
                x -> miBadgeMappingRepository.deleteAllByMiBadgeMappingPk(x.miBadgeMappingPk)
        }
    }
}