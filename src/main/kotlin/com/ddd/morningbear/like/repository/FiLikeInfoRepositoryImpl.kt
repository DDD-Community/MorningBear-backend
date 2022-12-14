package com.ddd.morningbear.like.repository

import com.ddd.morningbear.like.dto.PopularLikeDto
import com.ddd.morningbear.like.entity.QFiLikeInfo
import com.ddd.morningbear.like.entity.pk.QFiLikeInfoPk
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

/**
 * @author yoonho
 * @since 2022.12.13
 */
@Repository
class FiLikeInfoRepositoryImpl(
    private val jpaQuery: JPAQueryFactory
): FiLikeInfoRepositoryDsl {

//    override fun findMostPopularUser(): List<String> {
//        return  jpaQuery
//                    .select(QFiLikeInfoPk.fiLikeInfoPk.takenAccountId.`as`("accountId"))
//                    .from(QFiLikeInfo.fiLikeInfo)
//                    .groupBy(QFiLikeInfoPk.fiLikeInfoPk.takenAccountId)
//                    .orderBy(QFiLikeInfoPk.fiLikeInfoPk.takenAccountId.count().desc())
//                    .fetch()
//    }

    override fun findPopularUser(size: Int): List<PopularLikeDto> {
        val likeInfo = jpaQuery
                        .select(QFiLikeInfoPk.fiLikeInfoPk.takenAccountId.`as`("accountId"), QFiLikeInfoPk.fiLikeInfoPk.takenAccountId.count().`as`("likeCnt"))
                        .from(QFiLikeInfo.fiLikeInfo)
                        .groupBy(QFiLikeInfoPk.fiLikeInfoPk.takenAccountId)
                        .orderBy(QFiLikeInfoPk.fiLikeInfoPk.takenAccountId.count().desc())
                        .limit(size.toLong())
                        .fetch()

        val result: MutableList<PopularLikeDto> = mutableListOf()
        likeInfo.map {
            result.add(
                PopularLikeDto(
                    accountId = it.get(0, String::class.java)!!,
                    likeCnt = it.get(1, Long::class.java)!!.toInt()
                )
            )
        }

        return result
    }
}