package com.ddd.morningbear.myinfo.repository

import com.ddd.morningbear.myinfo.entity.MpUserInfo
import org.springframework.data.jpa.repository.JpaRepository

interface MpUserInfoRepository: JpaRepository<MpUserInfo, String>, MpUserInfoRepositoryDsl

interface MpUserInfoRepositoryDsl {

}