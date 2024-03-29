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

//    /**
//     * 내사진 조회 (특정사진)
//     *
//     * @param photoId [String]
//     * @return result [FiPhotoInfoDto]
//     * @author yoonho
//     * @since 2022.12.06
//     */
//    @QueryMapping
//    fun findMyPhoto(@Argument photoId: String): FiPhotoInfoDto {
//        val accountId = getAuthenticationContextAccountId()
//        return photoService.findPhoto(accountId, photoId)
//    }
//
//    /**
//     * 사용자 사진 조회 (특정사진)
//     *
//     * @param photoUserInput [PhotoInput]
//     * @return result [FiPhotoInfoDto]
//     * @author yoonho
//     * @since 2022.12.06
//     */
//    @QueryMapping
//    fun findUserPhoto(@Argument photoUserInput: PhotoInput): FiPhotoInfoDto {
//        return photoService.findPhoto(photoUserInput.accountId, photoUserInput.photoId)
//    }
//
//    /**
//     * 내사진 조회 (카테고리별)
//     *
//     * @param photoId [String]
//     * @return List [FiPhotoInfoDto]
//     * @author yoonho
//     * @since 2022.12.06
//     */
//    @QueryMapping
//    fun findMyPhotoByCategory(@Argument categoryId: String): List<FiPhotoInfoDto> {
//        val accountId = getAuthenticationContextAccountId()
//        return photoService.findPhotoByCategory(accountId, categoryId)
//    }
//
//    /**
//     * 사용자 사진 조회 (카테고리별)
//     *
//     * @param photoCategoryInput [PhotoInput]
//     * @return List [FiPhotoInfoDto]
//     * @author yoonho
//     * @since 2022.12.06
//     */
//    @QueryMapping
//    fun findUserPhotoByCategory(@Argument photoCategoryInput: PhotoInput): List<FiPhotoInfoDto> {
//        return photoService.findPhotoByCategory(photoCategoryInput.accountId, photoCategoryInput.categoryId)
//    }

    /**
     * 오늘의 미라클모닝러 조회
     *
     * @author yoonho
     * @since 2023.01.16
     */
    @QueryMapping
    fun findTodayPhoto(): FiPhotoInfoDto =
        photoService.findTodayPhoto()

    /**
     * 순서별 사진리스트 조회
     *
     * @param orderInput [OrderInput]
     * @return List [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.13
     */
    @QueryMapping
    fun findPhotoByOrderType(@Argument orderInput: OrderInput): List<FiPhotoInfoDto> =
        photoService.findPhotoByOrderType(orderInput.size, orderInput.orderType)

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

    /**
     * 사진 삭제
     *
     * @param photoId [String]
     * @return result [Boolean]
     * @author yoonho
     * @since 2022.12.16
     */
    @MutationMapping
    fun deleteMyPhoto(@Argument photoId: String): FiPhotoInfoDto {
        val accountId = getAuthenticationContextAccountId()
        return photoService.deleteMyPhoto(photoId)
    }
}