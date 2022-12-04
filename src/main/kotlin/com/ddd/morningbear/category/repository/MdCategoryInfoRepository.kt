package com.ddd.morningbear.category.repository

import com.ddd.morningbear.category.entity.MdCategoryInfo
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author yoonho
 * @since 2022.12.04
 */
interface MdCategoryInfoRepository: JpaRepository<MdCategoryInfo, String>