package com.ddd.morningbear.badge

import com.ddd.morningbear.api.badge.dto.BadgeInput
import com.ddd.morningbear.badge.dto.MdBadgeInfoDto
import com.ddd.morningbear.badge.dto.MiBadgeMappingDto
import com.ddd.morningbear.badge.dto.UserBadgeDetailDto
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
     * 내 뱃지 전체 조회
     *
     * @param accountId [String]
     * @return List [MdBadgeInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun findMyAllBadge(accountId: String): MutableList<UserBadgeDetailDto> {
        val badgeIdList = miBadgeMappingRepository.findAllByMiBadgeMappingPkAccountId(accountId).map { it.miBadgeMappingPk.badgeId }.stream().collect(Collectors.toList())
        val allBadges = this.findAllBadge()

        val result: MutableList<UserBadgeDetailDto> = mutableListOf()
        allBadges.forEach {
            result.add(
                UserBadgeDetailDto(
                    badgeId = it.badgeId,
                    badgeTitle = it.badgeTitle,
                    badgeDesc = it.badgeDesc,
                    isAcquired = badgeIdList.contains(it.badgeId),
                    acquirePercent = if(badgeIdList.contains(it.badgeId)) 100 else 0
                )
            )
        }

        return result
    }

    /**
     * 내 뱃지 저장
     *
     * @param accountId [String]
     * @param badgeId [String]
     * @return List [MdBadgeInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun saveMyBadge(accountId: String, badgeId: String): MdBadgeInfoDto? {
        if(!mdBadgeInfoRepository.existsById(badgeId)){
            throw GraphQLBadRequestException("존재하지 않은 뱃지ID 입니다.")
        }

        if(miBadgeMappingRepository.existsById(MiBadgeMappingPk(accountId = accountId,badgeId = badgeId))) {
            return null
        }

        val badgeMappingInfo = miBadgeMappingRepository.save(
                                    MiBadgeMapping(
                                        miBadgeMappingPk = MiBadgeMappingPk(
                                            accountId = accountId,
                                            badgeId = badgeId
                                        ),
                                        userInfo = mpUserInfoRepository.findById(accountId).orElseThrow { throw GraphQLNotFoundException("사용자정보를 조회할 수 없습니다.") }
                                    )
                                )

        return mdBadgeInfoRepository.findById(badgeMappingInfo.miBadgeMappingPk.badgeId).orElseGet(null).toDto()
    }

    /**
     * 내 뱃지 삭제
     *
     * @param accountId [String]
     * @param badgeId [String]
     * @return result [MdBadgeInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun deleteMyBadge(accountId: String, badgeId: String): Boolean {
        try{
            val deleteBadge = mdBadgeInfoRepository.findById(badgeId).orElseThrow {
                throw GraphQLBadRequestException("존재하지 않은 뱃지ID 입니다.")
            }.toDto()

            if(!miBadgeMappingRepository.existsById(MiBadgeMappingPk(accountId = accountId,badgeId = badgeId))) {
                return false
            }

            miBadgeMappingRepository.deleteById(
                MiBadgeMappingPk(
                    accountId = accountId,
                    badgeId = badgeId
                )
            )
        }catch(be: GraphQLBadRequestException) {
            throw be
        }catch (e: Exception){
            throw GraphQLBadRequestException()
        }

        return true
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
                        badgeTitle = x.badgeTitle,
                        badgeDesc = x.badgeDesc
                    )
                )
        }

        return this.findAllBadge()
    }
}