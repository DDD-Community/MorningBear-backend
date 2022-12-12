package com.ddd.morningbear.block

import com.ddd.morningbear.block.dto.MiBlockMappingDto
import com.ddd.morningbear.block.entity.MiBlockMapping
import com.ddd.morningbear.block.entity.pk.MiBlockMappingPk
import com.ddd.morningbear.block.repository.MiBlockMappingRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Service
class BlockService(
    private val miBlockMappingRepository: MiBlockMappingRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 내가 차단한 사용자목록 조회 - 개발자용
     *
     * @param accountId [String]
     * @return List [MiBlockMappingDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun findBlock(accountId: String): List<MiBlockMappingDto> {
        return miBlockMappingRepository.findByMiBlockMappingPkAccountId(accountId).map { it.toDto() }
    }

    /**
     * 사용자 차단하기
     *
     * @param accountId [String]
     * @param input [String]
     * @return List [MiBlockMappingDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun saveBlock(accountId: String, blockAccountId: String): List<MiBlockMappingDto> {
        miBlockMappingRepository.save(
            MiBlockMapping(
                miBlockMappingPk = MiBlockMappingPk(accountId = accountId, blockAccountId = blockAccountId)
            )
        )

        return this.findBlock(accountId)
    }
}