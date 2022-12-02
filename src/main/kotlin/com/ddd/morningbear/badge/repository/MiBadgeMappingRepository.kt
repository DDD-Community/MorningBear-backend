package com.ddd.morningbear.badge.repository

import com.ddd.morningbear.badge.entity.MiBadgeMapping
import org.springframework.data.jpa.repository.JpaRepository

interface MiBadgeMappingRepository : JpaRepository<MiBadgeMapping, String>