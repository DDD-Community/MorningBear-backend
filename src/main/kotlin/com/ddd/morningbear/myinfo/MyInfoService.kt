package com.ddd.morningbear.myinfo

import com.ddd.morningbear.api.myinfo.dto.MyInfoInput
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import com.ddd.morningbear.common.exception.GraphQLInternalServerException
import com.ddd.morningbear.common.exception.GraphQLNotFoundException
import com.ddd.morningbear.myinfo.dto.MpUserInfoDto
import com.ddd.morningbear.myinfo.entity.MpUserInfo
import com.ddd.morningbear.myinfo.repository.MpUserInfoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MyInfoService(
    private val mpUserInfoRepository: MpUserInfoRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun findMyInfo(accountId: String): MpUserInfoDto = mpUserInfoRepository.findById(accountId).orElseThrow {
        throw GraphQLNotFoundException("내 정보 조회에 실패하였습니다.")
    }.toDto()

    fun saveMyInfo(input: MyInfoInput): MpUserInfoDto {
        if(input.accountId.isNullOrBlank()){
            throw GraphQLBadRequestException("로그인정보가 존재하지 않습니다.")
        }

        try{
            return mpUserInfoRepository.save(
                    MpUserInfo(
                        accountId = input.accountId,
                        nickName = input.nickName,
                        photoLink = input.photoLink,
                        memo = input.memo,
                        wakeUpAt = input.wakeUpAt,
                        updatedAt = LocalDateTime.now()
                    )
                ).toDto()
        }catch(e: Exception){
            throw GraphQLInternalServerException()
        }
    }
}