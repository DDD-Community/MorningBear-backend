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
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.stream.Collectors

@Service
class CategoryService(
    private val mdCategoryInfoRepository: MdCategoryInfoRepository,
    private val miCategoryMappingRepository: MiCategoryMappingRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun findAllCategory(): List<MdCategoryInfoDto> = mdCategoryInfoRepository.findAll().map { it.toDto() }

    fun findMyCategoryMapping(accountId: String): List<MiCategoryMappingDto> = miCategoryMappingRepository.findAllByMiCategoryMappingPkAccountId(accountId).map { it.toDto() }

    fun findMyCategory(accountId: String): List<MdCategoryInfoDto> {
        var categoryIdList = miCategoryMappingRepository.findAllByMiCategoryMappingPkAccountId(accountId).map { it.miCategoryMappingPk.categoryId }.stream().collect(Collectors.toList())
        return mdCategoryInfoRepository.findAllById(categoryIdList).map { it.toDto() }
    }

    fun saveMyCategory(accountId: String, input: List<String>): List<MdCategoryInfoDto> {
        var categoryList = mdCategoryInfoRepository.findAllById(input)
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
                        updatedAt = LocalDateTime.now()
                    )
                )
        }

        return this.findMyCategory(accountId)
    }

    fun saveCategory(input: List<CategoryInput>): List<MdCategoryInfoDto> {
        input.stream().forEach {
            x -> mdCategoryInfoRepository.save(
                    MdCategoryInfo(
                        categoryId = x.categoryId,
                        categoryDesc = x.categoryDesc,
                        updatedAt = LocalDateTime.now()
                    )
                )
        }

        return this.findAllCategory()
    }

    fun deleteMyCategory(accountId: String) {
        val categoryList = miCategoryMappingRepository.findAllByMiCategoryMappingPkAccountId(accountId)
        categoryList.stream().forEach {
                x -> miCategoryMappingRepository.deleteAllByMiCategoryMappingPk(x.miCategoryMappingPk)
        }
    }
}