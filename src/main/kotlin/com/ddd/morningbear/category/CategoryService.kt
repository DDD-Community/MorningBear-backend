package com.ddd.morningbear.category

import com.ddd.morningbear.api.category.dto.CategoryInput
import com.ddd.morningbear.category.dto.MdCategoryInfoDto
import com.ddd.morningbear.category.dto.MiCategoryMappingDto
import com.ddd.morningbear.category.entity.MdCategoryInfo
import com.ddd.morningbear.category.entity.MiCategoryMapping
import com.ddd.morningbear.category.entity.pk.MiCategoryMappingPk
import com.ddd.morningbear.category.repository.MdCategoryInfoRepository
import com.ddd.morningbear.category.repository.MiCategoryMappingRepository
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import com.ddd.morningbear.common.exception.GraphQLNotFoundException
import com.ddd.morningbear.myinfo.repository.MpUserInfoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.stream.Collectors

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Service
class CategoryService(
    private val mdCategoryInfoRepository: MdCategoryInfoRepository,
    private val miCategoryMappingRepository: MiCategoryMappingRepository,
    private val mpUserInfoRepository: MpUserInfoRepository,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 전체 카테고리 조회
     *
     * @return List [MdCategoryInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun findAllCategory(): List<MdCategoryInfoDto> = mdCategoryInfoRepository.findAll().map { it.toDto() }

    /**
     * 내 카테고리 조회(매핑테이블) - 개발자용
     *
     * @param accountId [String]
     * @return List [MiCategoryMappingDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun findMyCategoryMapping(accountId: String): List<MiCategoryMappingDto> = miCategoryMappingRepository.findAllByMiCategoryMappingPkAccountId(accountId).map { it.toDto() }

    /**
     * 내 카테고리 조회
     *
     * @param accountId [String]
     * @return List [MdCategoryInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun findMyCategory(accountId: String): List<MdCategoryInfoDto> {
        var categoryIdList = miCategoryMappingRepository.findAllByMiCategoryMappingPkAccountId(accountId).map { it.miCategoryMappingPk.categoryId }.stream().collect(Collectors.toList())
        return mdCategoryInfoRepository.findAllById(categoryIdList).map { it.toDto() }
    }

    /**
     * 내 카테고리 저장
     *
     * @param accountId [String]
     * @param input [List][String]
     * @return List [MdCategoryInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun saveMyCategory(accountId: String, input: List<String>): List<MdCategoryInfoDto> {
        val categoryList = mdCategoryInfoRepository.findAllById(input)
        if(input.size != categoryList.size){
            throw GraphQLBadRequestException("존재하지 않은 카테고리ID 입니다.")
        }

        categoryList.stream().forEach {
            x -> miCategoryMappingRepository.save(
                    MiCategoryMapping(
                        miCategoryMappingPk = MiCategoryMappingPk(
                            accountId = accountId,
                            categoryId = x.categoryId
                        ),
                        userInfo = mpUserInfoRepository.findById(accountId).orElseThrow { throw GraphQLNotFoundException("사용자 정보를 조회할 수 없습니다.") },
                        createdAt = LocalDateTime.now()
                    )
                )
        }

        return this.findMyCategory(accountId)
    }

    /**
     * 전체 카테고리 저장
     *
     * @param accountId [String]
     * @return List [MdCategoryInfoDto]
     * @author yoonho
     * @since 2022.12.12
     */
    fun saveAllCategory(accountId: String): List<MdCategoryInfoDto> {
        var categoryList = mdCategoryInfoRepository.findAll()

        categoryList.stream().forEach {
                x -> miCategoryMappingRepository.save(
                        MiCategoryMapping(
                            miCategoryMappingPk = MiCategoryMappingPk(
                                accountId = accountId,
                                categoryId = x.categoryId
                            ),
                            userInfo = mpUserInfoRepository.findById(accountId).orElseThrow { throw GraphQLNotFoundException("사용자 정보를 조회할 수 없습니다.") }
                        )
                    )
        }

        return this.findMyCategory(accountId)
    }

    /**
     * 카테고리 메타정보 저장 - 개발자용
     *
     * @param input [List][CategoryInput]
     * @return List [MdCategoryInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun saveCategory(input: List<CategoryInput>): List<MdCategoryInfoDto> {
        input.stream().forEach {
            x -> mdCategoryInfoRepository.save(
                    MdCategoryInfo(
                        categoryId = x.categoryId,
                        categoryDesc = x.categoryDesc
                    )
                )
        }

        return this.findAllCategory()
    }
}