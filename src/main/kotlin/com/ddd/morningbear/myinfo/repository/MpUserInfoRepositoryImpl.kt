package com.ddd.morningbear.myinfo.repository

import com.ddd.morningbear.myinfo.entity.MpUserInfo
import com.ddd.morningbear.myinfo.entity.QMpUserInfo
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

/**
 * @author yoonho
 * @since 2022.12.07
 */
@Repository
class MpUserInfoRepositoryImpl(
    private val jpaQuery: JPAQueryFactory
): MpUserInfoRepositoryDsl {

    override fun findUserInfoByNickName(nickName: String): List<MpUserInfo> {
        return jpaQuery
                    .selectFrom(QMpUserInfo.mpUserInfo)
                    .where(QMpUserInfo.mpUserInfo.nickName.containsIgnoreCase(nickName))
                    .fetch()
    }
}