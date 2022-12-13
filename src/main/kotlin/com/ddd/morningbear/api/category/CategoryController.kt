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

/**
 * @author yoonho
 * @since 2022.12.04
 */
@RestController
class CategoryController(
    private val categoryService: CategoryService
): BaseController() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 전체 카테고리 조회
     *
     * @return List [MdCategoryInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    @QueryMapping
    fun findAllCategory(): List<MdCategoryInfoDto> {
        return categoryService.findAllCategory()
    }

//    /**
//     * 내 카테고리 조회(매핑테이블) - 개발자용
//     *
//     * @return List [MiCategoryMappingDto]
//     * @author yoonho
//     * @since 2022.12.04
//     */
//    @QueryMapping
//    fun findMyCategoryMapping(): List<MiCategoryMappingDto> {
//        val accountId = getAuthenticationContextAccountId()
//        return categoryService.findMyCategoryMapping(accountId)
//    }

//    /**
//     * 내 카테고리 저장
//     *
//     * @param input [List][String]
//     * @return List [MdCategoryInfoDto]
//     * @author yoonho
//     * @since 2022.12.04
//     */
//    @MutationMapping
//    fun saveMyCategory(@Argument categoryIdList: List<String>): List<MdCategoryInfoDto> {
//        val accountId = getAuthenticationContextAccountId()
//        return categoryService.saveMyCategory(accountId, categoryIdList)
//    }

    /**
     * 카테고리 메타정보 저장 - 개발자용
     *
     * @param categoryInputList [List][CategoryInput]
     * @return List [MdCategoryInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    @MutationMapping
    fun saveCategory(@Argument categoryInputList: List<CategoryInput>): List<MdCategoryInfoDto> {
        return categoryService.saveCategory(categoryInputList)
    }
}