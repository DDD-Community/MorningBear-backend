package com.ddd.morningbear.badge.repository

import com.ddd.morningbear.badge.entity.MdBadgeInfo
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author yoonho
 * @since 2022.11.19
 */
interface MdBadgeInfoRepository: JpaRepository<MdBadgeInfo, String>