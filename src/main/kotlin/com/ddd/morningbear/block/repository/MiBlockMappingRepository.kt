package com.ddd.morningbear.block.repository

import com.ddd.morningbear.block.entity.MiBlockMapping
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author yoonho
 * @since 2022.12.04
 */
interface MiBlockMappingRepository: JpaRepository<MiBlockMapping, String> {
    fun findByMiBlockMappingPkAccountId(accountId: String): List<MiBlockMapping>
}