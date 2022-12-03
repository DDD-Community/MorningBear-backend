package com.ddd.morningbear.category.repository

import com.ddd.morningbear.category.entity.MdCategoryInfo
import org.springframework.data.jpa.repository.JpaRepository

interface MdCategoryInfoRepository: JpaRepository<MdCategoryInfo, String>