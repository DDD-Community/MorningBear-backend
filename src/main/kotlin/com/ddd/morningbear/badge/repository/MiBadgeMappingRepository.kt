package com.ddd.morningbear.badge.repository

import com.ddd.morningbear.badge.entity.MiBadgeMapping
import com.ddd.morningbear.badge.entity.pk.MiBadgeMappingPk
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * @author yoonho
 * @since 2022.11.19
 */
interface MiBadgeMappingRepository : JpaRepository<MiBadgeMapping, MiBadgeMappingPk> {
    fun findAllByMiBadgeMappingPkAccountId(accountId: String): List<MiBadgeMapping>
    fun deleteAllByMiBadgeMappingPk(miBadgeMappingPk: MiBadgeMappingPk)
}