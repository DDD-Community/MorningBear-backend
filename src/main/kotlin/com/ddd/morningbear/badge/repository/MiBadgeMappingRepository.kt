package com.ddd.morningbear.badge.repository

import com.ddd.morningbear.badge.entity.MiBadgeMapping
import com.ddd.morningbear.badge.entity.pk.MiBadgeMappingPk
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MiBadgeMappingRepository : JpaRepository<MiBadgeMapping, String> {
    fun findAllByMiBadgeMappingPkAccountId(accountId: String): List<MiBadgeMapping>
    fun deleteAllByMiBadgeMappingPk(miBadgeMappingPk: MiBadgeMappingPk)
}