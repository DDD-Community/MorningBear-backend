package com.ddd.morningbear.like.repository

import com.ddd.morningbear.like.dto.PopularLikeDto
import com.ddd.morningbear.like.entity.FiLikeInfo
import com.ddd.morningbear.like.entity.pk.FiLikeInfoPk
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * @author yoonho
 * @since 2022.12.04
 */
interface FiLikeInfoRepository: JpaRepository<FiLikeInfo, FiLikeInfoPk>, FiLikeInfoRepositoryDsl

interface FiLikeInfoRepositoryDsl {
//    fun findMostPopularUser(): List<String>
    fun findPopularUser(size: Int): List<PopularLikeDto>
    fun findAllLikeInfoByTakenAccountId(accountId: String): Optional<List<FiLikeInfo>>
    fun findAllLikeInfoByGivenAccountId(accountId: String): Optional<List<FiLikeInfo>>
}