package com.ddd.morningbear.photo.repository

import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.like.entity.QFiLikeInfo
import com.ddd.morningbear.like.entity.pk.QFiLikeInfoPk
import com.ddd.morningbear.photo.entity.FiPhotoInfo
import com.ddd.morningbear.photo.entity.QFiPhotoInfo
import com.querydsl.core.types.dsl.NumberExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.util.*

/**
 * @author yoonho
 * @since 2022.12.13
 */
@Repository
class FiPhotoInfoRepositoryImpl(
    private val jpaQuery: JPAQueryFactory
): FiPhotoInfoRepositoryDsl {

    override fun findPhotoByOrderType(size: Int, orderType: String): List<FiPhotoInfo> =
        jpaQuery
            .selectFrom(QFiPhotoInfo.fiPhotoInfo)
            .orderBy(
                when(orderType) {
                    CommCode.OrderType.CREATE_ORDER_ASC.code -> QFiPhotoInfo.fiPhotoInfo.createdAt.asc()
                    CommCode.OrderType.CREATE_ORDER_DESC.code -> QFiPhotoInfo.fiPhotoInfo.createdAt.desc()
                    else -> QFiPhotoInfo.fiPhotoInfo.createdAt.asc()
                }
            )
            .limit(size.toLong())
            .fetch()

    override fun findPhotoByAccountIdList(size: Int, orderType: String): MutableList<FiPhotoInfo> {

        val likeInfo = jpaQuery
                .select(QFiLikeInfoPk.fiLikeInfoPk.takenAccountId.`as`("accountId"), QFiLikeInfoPk.fiLikeInfoPk.takenAccountId.count().`as`("likeCnt"))
                .from(QFiLikeInfo.fiLikeInfo)
                .groupBy(QFiLikeInfoPk.fiLikeInfoPk.takenAccountId)
                .orderBy(
                    when(orderType) {
                        CommCode.OrderType.LIKE_ORDER_ASC.code -> QFiLikeInfoPk.fiLikeInfoPk.takenAccountId.count().asc()
                        CommCode.OrderType.LIKE_ORDER_DESC.code -> QFiLikeInfoPk.fiLikeInfoPk.takenAccountId.count().desc()
                        else -> QFiLikeInfoPk.fiLikeInfoPk.takenAccountId.count().desc()
                    }

                )
                .limit(size.toLong())
                .fetch()

        val photoInfo = jpaQuery
                    .selectFrom(QFiPhotoInfo.fiPhotoInfo)
                    .where(QFiPhotoInfo.fiPhotoInfo.userInfo.accountId.`in`(likeInfo.map { it.get(0, String::class.java) }))
                    .limit(size.toLong())
                    .fetch()

        val result: MutableList<FiPhotoInfo> = mutableListOf()
        likeInfo.forEach { x ->
            result.add(photoInfo.filter { it.userInfo.accountId.equals(x.get(0, String::class.java)) }.first())
        }

        return result
    }

    override fun findPhotoByRandomOrder(): Optional<FiPhotoInfo> =
        Optional.ofNullable(
            jpaQuery
            .selectFrom(QFiPhotoInfo.fiPhotoInfo)
            .orderBy(NumberExpression.random().asc())
            .limit(1)
            .fetchOne()
        )
}