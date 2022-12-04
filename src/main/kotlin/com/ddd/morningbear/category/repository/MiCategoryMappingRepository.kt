package com.ddd.morningbear.category.repository

import com.ddd.morningbear.category.entity.MiCategoryMapping
import com.ddd.morningbear.category.entity.pk.MiCategoryMappingPk
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author yoonho
 * @since 2022.12.04
 */
interface MiCategoryMappingRepository: JpaRepository<MiCategoryMapping, String> {
    fun findAllByMiCategoryMappingPkAccountId(accountId: String): List<MiCategoryMapping>
    fun deleteAllByMiCategoryMappingPk(miCategoryMappingPk: MiCategoryMappingPk)
}