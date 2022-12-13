package com.ddd.morningbear.api.photo

import com.ddd.morningbear.api.photo.dto.OrderInput
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
     * @param photoId [String]
     * @return result [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    @QueryMapping
    fun findMyPhoto(@Argument photoId: String): FiPhotoInfoDto {
        val accountId = getAuthenticationContextAccountId()
        return photoService.findPhoto(accountId, photoId)
    }

    /**
     * 사용자 사진 조회 (특정사진)
     *
     * @param photoUserInput [PhotoInput]
     * @return result [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    @QueryMapping
    fun findUserPhoto(@Argument photoUserInput: PhotoInput): FiPhotoInfoDto {
        return photoService.findPhoto(photoUserInput.accountId, photoUserInput.photoId)
    }

    /**
     * 내사진 조회 (카테고리별)
     *
     * @param photoId [String]
     * @return List [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    @QueryMapping
    fun findMyPhotoByCategory(@Argument photoId: String): List<FiPhotoInfoDto> {
        val accountId = getAuthenticationContextAccountId()
        return photoService.findPhotoByCategory(accountId, photoId)
    }

    /**
     * 사용자 사진 조회 (카테고리별)
     *
     * @param photoCategoryInput [PhotoInput]
     * @return List [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    @QueryMapping
    fun findUserPhotoByCategory(@Argument photoCategoryInput: PhotoInput): List<FiPhotoInfoDto> {
        return photoService.findPhotoByCategory(photoCategoryInput.accountId, photoCategoryInput.categoryId)
    }

    /**
     * 순서별 사진리스트 조회
     *
     * @param orderInput [OrderInput]
     * @return List [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.13
     */
    @QueryMapping
    fun findPhotoByOrderType(@Argument orderInput: OrderInput): List<FiPhotoInfoDto> {
        return photoService.findPhotoByOrderType(orderInput.size, orderInput.orderType)
    }

    /**
     * 사진 저장
     *
     * @param photoInput [PhotoInput]
     * @return result [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    @MutationMapping
    fun saveMyPhoto(@Argument photoInput: PhotoInput): FiPhotoInfoDto {
        val accountId = getAuthenticationContextAccountId()
        return photoService.saveMyPhoto(accountId, photoInput)
    }
}