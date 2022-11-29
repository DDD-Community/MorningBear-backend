package com.ddd.morningbear.myinfo

import com.ddd.morningbear.common.exception.GraphQLNotFoundException
import com.ddd.morningbear.myinfo.dto.MpUserInfoDto
import com.ddd.morningbear.myinfo.repository.MpUserInfoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MyInfoService(
    private val mpUserInfoRepository: MpUserInfoRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun findMyInfo(accountId: String): MpUserInfoDto = mpUserInfoRepository.findById(accountId).orElseThrow {
        logger.error(" >>> [findMyInfo] not found my profile")
        throw GraphQLNotFoundException("내 정보 조회에 실패하였습니다.")
    }.toDto()
}