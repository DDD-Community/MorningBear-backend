package com.ddd.morningbear.block.dto

import java.io.Serializable
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.12.04
 */
data class MiBlockMappingDto(
    val accountId: String,
    val blockAccountId: String
): Serializable
