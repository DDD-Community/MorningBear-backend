package com.ddd.morningbear.api.block

import com.ddd.morningbear.block.BlockService
import com.ddd.morningbear.block.dto.MiBlockMappingDto
import com.ddd.morningbear.common.BaseController
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author yoonho
 * @since 2022.12.04
 */
@RestController
class BlockController(
    private val blockService: BlockService
): BaseController() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 내가 차단한 사용자목록 조회 - 개발자용
     *
     * @return List [MiBlockMappingDto]
     * @author yoonho
     * @since 2022.12.04
     */
    @QueryMapping
    fun findBlock(): List<MiBlockMappingDto> {
        val accountId = getAuthenticationContextAccountId()
        return blockService.findBlock(accountId)
    }

    /**
     * 사용자 차단하기
     *
     * @param input [String]
     * @return List [MiBlockMappingDto]
     * @author yoonho
     * @since 2022.12.04
     */
    @MutationMapping
    fun saveBlock(@Argument input: String): List<MiBlockMappingDto> {
        val accountId = getAuthenticationContextAccountId()
        return blockService.saveBlock(accountId, input)
    }
}