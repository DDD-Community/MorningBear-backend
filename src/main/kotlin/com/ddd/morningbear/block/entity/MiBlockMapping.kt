package com.ddd.morningbear.block.entity

import com.ddd.morningbear.block.dto.MiBlockMappingDto
import com.ddd.morningbear.block.entity.pk.MiBlockMappingPk
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Entity
@Table(name = "MI_BLOCK_MAPPING")
class MiBlockMapping(
    @EmbeddedId
    val miBlockMappingPk: MiBlockMappingPk,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now()
) {
    fun toDto() = MiBlockMappingDto(
        accountId = miBlockMappingPk.accountId,
        blockAccountId = miBlockMappingPk.blockAccountId
    )
}