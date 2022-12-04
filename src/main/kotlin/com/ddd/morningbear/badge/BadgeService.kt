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
import com.ddd.morningbear.common.exception.GraphQLNotFoundException
import com.ddd.morningbear.myinfo.repository.MpUserInfoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.stream.Collectors

/**
 * @author yoonho
 * @since 2022.11.19
 */
@Service
class BadgeService(
    private val miBadgeMappingRepository: MiBadgeMappingRepository,
    private val mdBadgeInfoRepository: MdBadgeInfoRepository,
    private val mpUserInfoRepository: MpUserInfoRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 전체 뱃지 조회
     *
     * @return List [MdBadgeInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun findAllBadge(): List<MdBadgeInfoDto> = mdBadgeInfoRepository.findAll().map { it.toDto() }

    /**
     * 내 뱃지 조회(매핑테이블) - 개발자용
     *
     * @param accountId [String]
     * @return List [MiBadgeMappingDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun findMyBadgeMapping(accountId: String): List<MiBadgeMappingDto> = miBadgeMappingRepository.findAllByMiBadgeMappingPkAccountId(accountId).map { it.toDto() }

    /**
     * 내 뱃지 조회
     *
     * @param accountId [String]
     * @return List [MdBadgeInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun findMyBadge(accountId: String): List<MdBadgeInfoDto> {
        var badgeIdList = miBadgeMappingRepository.findAllByMiBadgeMappingPkAccountId(accountId).map { it.miBadgeMappingPk.badgeId }.stream().collect(Collectors.toList())
        return mdBadgeInfoRepository.findAllById(badgeIdList).map { it.toDto() }
    }

    /**
     * 내 뱃지 저장
     *
     * @param accountId [String]
     * @param input [List][String]
     * @return List [MdBadgeInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
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
                    userInfo = mpUserInfoRepository.findById(accountId).orElseThrow { throw GraphQLNotFoundException("사용자정보를 조회할 수 없습니다.") },
                    updatedAt = LocalDateTime.now()
                )
            )
        }

        return this.findMyBadge(accountId)
    }

    /**
     * 뱃지 메타정보 저장 - 개발자용
     *
     * @param input [List][BadgeInput]
     * @return List [MdBadgeInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
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
}