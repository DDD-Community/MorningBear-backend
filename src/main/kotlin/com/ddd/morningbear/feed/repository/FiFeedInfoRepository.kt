package com.ddd.morningbear.feed.repository

import com.ddd.morningbear.feed.entity.FiFeedInfo
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author yoonho
 * @since 2022.12.04
 */
interface FiFeedInfoRepository: JpaRepository<FiFeedInfo, String>