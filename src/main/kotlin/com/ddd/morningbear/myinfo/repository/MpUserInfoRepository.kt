package com.ddd.morningbear.myinfo.repository

import com.ddd.morningbear.myinfo.entity.MpUserInfo
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * @author yoonho
 * @since 2022.12.04
 */
interface MpUserInfoRepository: JpaRepository<MpUserInfo, String>, MpUserInfoRepositoryDsl

interface MpUserInfoRepositoryDsl {
    fun findUserInfoByNickName(nickName: String): List<MpUserInfo>
    fun findByIdNotInBlockUser(accountId: String): Optional<MpUserInfo>
}