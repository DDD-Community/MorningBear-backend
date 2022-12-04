package com.ddd.morningbear.myinfo

import com.ddd.morningbear.api.myinfo.dto.MyInfoInput
import com.ddd.morningbear.badge.BadgeService
import com.ddd.morningbear.category.CategoryService
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import com.ddd.morningbear.common.exception.GraphQLInternalServerException
import com.ddd.morningbear.common.exception.GraphQLNotFoundException
import com.ddd.morningbear.myinfo.dto.MpUserInfoDto
import com.ddd.morningbear.myinfo.entity.MpUserInfo
import com.ddd.morningbear.myinfo.repository.MpUserInfoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Service
class MyInfoService(
    private val mpUserInfoRepository: MpUserInfoRepository,
    private val categoryService: CategoryService,
    private val badgeService: BadgeService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 내정보 조회
     *
     * @param accountId [String]
     * @return result [MpUserInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun findMyInfo(accountId: String): MpUserInfoDto {
        var myInfo = mpUserInfoRepository.findById(accountId).orElseThrow {
            throw GraphQLNotFoundException("내 정보 조회에 실패하였습니다.")
        }.toDto()

        // 뱃지리스트 조회
        myInfo.badgeList = badgeService.findMyBadge(accountId)
        // 카테고리리스트 조회
        myInfo.categoryList = categoryService.findMyCategory(accountId)

        logger.info(" >>> [findMyInfo] myInfo: {}", myInfo)
        return myInfo
    }

    /**
     * 내정보 저장
     *
     * @param accountId [String]
     * @param input [MyInfoInput]
     * @return result [MpUserInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    @Transactional(rollbackFor = [Exception::class])
    fun saveMyInfo(accountId: String, input: MyInfoInput): MpUserInfoDto {
        if(accountId.isNullOrBlank()){
            throw GraphQLBadRequestException("로그인정보가 존재하지 않습니다.")
        }

        try{
            // Patch방식으로 이미 저장된 사용자정보에서 input으로 전달받은 데이터만 update
            if(mpUserInfoRepository.existsById(accountId)) {
                val myInfo = mpUserInfoRepository.findById(accountId).orElseGet(null).toDto()

                if(input.nickName.isNullOrBlank()) input.nickName = myInfo.nickName
                if(input.photoLink.isNullOrBlank()) input.photoLink = myInfo.photoLink
                if(input.memo.isNullOrBlank()) input.memo = myInfo.memo
                if(input.wakeUpAt.isNullOrBlank()) input.wakeUpAt = myInfo.wakeUpAt
            }

            mpUserInfoRepository.save(
                MpUserInfo(
                    accountId = accountId,
                    nickName = input.nickName,
                    photoLink = input.photoLink,
                    memo = input.memo,
                    wakeUpAt = input.wakeUpAt,
                    updatedAt = LocalDateTime.now()
                )
            ).toDto()

            return this.findMyInfo(accountId)
        }catch(e: Exception){
            throw GraphQLInternalServerException()
        }
    }

    /**
     * 탈퇴하기
     *
     * @param accountId [String]
     * @author yoonho
     * @since 2022.12.04
     */
    @Transactional(rollbackFor = [Exception::class])
    fun deleteMyInfo(accountId: String): Boolean {
        try{
            // 회원테이블 메타정보 삭제
            mpUserInfoRepository.deleteById(accountId)
        }catch(e: Exception){
            throw GraphQLBadRequestException()
        }

        return true
    }
}