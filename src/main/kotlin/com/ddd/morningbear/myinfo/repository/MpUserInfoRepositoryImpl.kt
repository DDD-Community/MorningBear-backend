package com.ddd.morningbear.myinfo.repository

import com.ddd.morningbear.block.entity.QMiBlockMapping
import com.ddd.morningbear.myinfo.entity.MpUserInfo
import com.ddd.morningbear.myinfo.entity.QMpUserInfo
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.util.*

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
                    .where(
                        QMpUserInfo.mpUserInfo.nickName.containsIgnoreCase(nickName)
                        .and(
                            // Block사용자 미노출 처리
                            QMpUserInfo.mpUserInfo.accountId.notIn(
                                JPAExpressions
                                    .select(QMiBlockMapping.miBlockMapping.miBlockMappingPk.blockAccountId)
                                    .from(QMiBlockMapping.miBlockMapping)
                            )
                        )
                    )
                    .fetch()
    }

    override fun findByIdNotInBlockUser(accountId: String): Optional<MpUserInfo> {
        return Optional.ofNullable(
            jpaQuery
                .selectFrom(QMpUserInfo.mpUserInfo)
                .where(
                    QMpUserInfo.mpUserInfo.accountId.equalsIgnoreCase(accountId)
                    .and(
                        // Block사용자 미노출 처리
                        QMpUserInfo.mpUserInfo.accountId.notIn(
                            JPAExpressions
                                .select(QMiBlockMapping.miBlockMapping.miBlockMappingPk.blockAccountId)
                                .from(QMiBlockMapping.miBlockMapping)
                        )
                    )
                )
                .fetchFirst()
        )
    }
}