package com.ddd.morningbear.badge.repository

import com.ddd.morningbear.badge.entity.MdBadgeInfo
import org.springframework.data.jpa.repository.JpaRepository

interface MdBadgeInfoRepository: JpaRepository<MdBadgeInfo, String>