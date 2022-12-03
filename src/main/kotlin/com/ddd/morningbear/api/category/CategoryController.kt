package com.ddd.morningbear.api.category

import com.ddd.morningbear.api.category.dto.CategoryInput
import com.ddd.morningbear.category.CategoryService
import com.ddd.morningbear.category.dto.MdCategoryInfoDto
import com.ddd.morningbear.category.dto.MiCategoryMappingDto
import com.ddd.morningbear.common.BaseController
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CategoryController(
    private val categoryService: CategoryService
): BaseController() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @QueryMapping
    fun findAllCategory(): List<MdCategoryInfoDto> {
        return categoryService.findAllCategory()
    }

    @QueryMapping
    fun findMyCategoryMapping(): List<MiCategoryMappingDto> {
        val accountId = getAuthenticationContextAccountId()
        return categoryService.findMyCategoryMapping(accountId)
    }

    @MutationMapping
    fun saveMyCategory(@Argument input: List<String>): List<MdCategoryInfoDto> {
        val accountId = getAuthenticationContextAccountId()
        return categoryService.saveMyCategory(accountId, input)
    }

    @MutationMapping
    fun saveCategory(@Argument input: List<CategoryInput>): List<MdCategoryInfoDto> {
        return categoryService.saveCategory(input)
    }
}