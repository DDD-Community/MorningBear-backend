package com.ddd.morningbear.api.photo

import com.ddd.morningbear.api.photo.dto.PhotoInput
import com.ddd.morningbear.common.BaseController
import com.ddd.morningbear.photo.PhotoService
import com.ddd.morningbear.photo.dto.FiPhotoInfoDto
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author yoonho
 * @since 2022.12.06
 */
@RestController
class PhotoController(
    private val photoService: PhotoService
): BaseController() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 내사진 조회 (특정사진)
     *
     * @param input [String]
     * @return result [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    @QueryMapping
    fun findMyPhoto(@Argument input: String): FiPhotoInfoDto {
        val accountId = getAuthenticationContextAccountId()
        return photoService.findPhoto(accountId, input)
    }

    /**
     * 사용자 사진 조회 (특정사진)
     *
     * @param input [PhotoInput]
     * @return result [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    @QueryMapping
    fun findUserPhoto(@Argument input: PhotoInput): FiPhotoInfoDto {
        return photoService.findPhoto(input.accountId, input.photoId)
    }

    /**
     * 내사진 조회 (카테고리별)
     *
     * @param input [String]
     * @return List [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    @QueryMapping
    fun findMyPhotoByCategory(@Argument input: String): List<FiPhotoInfoDto> {
        val accountId = getAuthenticationContextAccountId()
        return photoService.findPhotoByCategory(accountId, input)
    }

    /**
     * 사용자 사진 조회 (카테고리별)
     *
     * @param input [PhotoInput]
     * @return List [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    @QueryMapping
    fun findUserPhotoByCategory(@Argument input: PhotoInput): List<FiPhotoInfoDto> {
        return photoService.findPhotoByCategory(input.accountId, input.categoryId)
    }

    /**
     * 사진 저장
     *
     * @param input [PhotoInput]
     * @return result [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    @MutationMapping
    fun saveMyPhoto(@Argument input: PhotoInput): FiPhotoInfoDto {
        val accountId = getAuthenticationContextAccountId()
        return photoService.saveMyPhoto(accountId, input)
    }
}