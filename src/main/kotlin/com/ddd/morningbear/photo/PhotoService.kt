package com.ddd.morningbear.photo

import com.ddd.morningbear.api.photo.dto.PhotoInput
import com.ddd.morningbear.category.repository.MdCategoryInfoRepository
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import com.ddd.morningbear.common.exception.GraphQLNotFoundException
import com.ddd.morningbear.myinfo.repository.MpUserInfoRepository
import com.ddd.morningbear.photo.dto.FiPhotoInfoDto
import com.ddd.morningbear.photo.entity.FiPhotoInfo
import com.ddd.morningbear.photo.repository.FiPhotoInfoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

/**
 * @author yoonho
 * @since 2022.12.06
 */
@Service
class PhotoService(
    private val fiPhotoInfoRepository: FiPhotoInfoRepository,
    private val mdCategoryInfoRepository: MdCategoryInfoRepository,
    private val mpUserInfoRepository: MpUserInfoRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 사진 조회 (특정사진)
     *
     * @param accountId [String]
     * @param photoId [String]
     * @return result [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    fun findPhoto(accountId: String?, photoId: String?): FiPhotoInfoDto {
        if(accountId.isNullOrBlank() || photoId.isNullOrBlank()){
            throw GraphQLBadRequestException()
        }

        return fiPhotoInfoRepository.findByPhotoIdAndUserInfoAccountId(photoId, accountId).orElseThrow {
            throw GraphQLNotFoundException("사진 조회에 실패하였습니다.")
        }.toDto()
    }

    /**
     * 사진 조회 (카테고리별)
     *
     * @param accountId [String]
     * @param categoryId [String]
     * @return List [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    fun findPhotoByCategory(accountId: String?, categoryId: String?): List<FiPhotoInfoDto> {
        if(accountId.isNullOrBlank() || categoryId.isNullOrBlank()){
            throw GraphQLBadRequestException()
        }

        if(categoryId.equals("ALL")){
            return fiPhotoInfoRepository.findAllByUserInfoAccountId(accountId).orElseThrow {
                throw GraphQLNotFoundException("전체 카테고리에 대한 사진 조회에 실패하였습니다.")
            }.map { it.toDto() }
        }

        return fiPhotoInfoRepository.findAllByCategoryInfoCategoryIdAndUserInfoAccountId(categoryId, accountId).orElseThrow {
            throw GraphQLNotFoundException("카테고리별 사진 조회에 실패하였습니다.")
        }.map { it.toDto() }
    }

    /**
     * 사진 저장
     *
     * @param accountId [String]
     * @param input [PhotoInput]
     * @return result [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    @Transactional(rollbackFor = [Exception::class])
    fun saveMyPhoto(accountId: String, input: PhotoInput): FiPhotoInfoDto {
        try{
            lateinit var photoId: String
            if(!input.photoId.isNullOrBlank()){
                photoId = input.photoId!!
                if(!fiPhotoInfoRepository.existsById(photoId)){
                    throw GraphQLNotFoundException("사진ID에 해당하는 사진정보를 찾을 수 없습니다.")
                }
            }else{
                photoId = UUID.randomUUID().toString()
            }

            if(fiPhotoInfoRepository.existsById(photoId)){
                val myPhoto = fiPhotoInfoRepository.findById(photoId).orElseGet(null).toDto()

                if(input.photoLink.isNullOrBlank()) input.photoLink = myPhoto.photoLink
                if(input.photoDesc.isNullOrBlank()) input.photoDesc = myPhoto.photoDesc
                if(input.startAt.isNullOrBlank()) input.startAt = myPhoto.startAt
                if(input.endAt.isNullOrBlank()) input.endAt = myPhoto.endAt
                if(input.categoryId.isNullOrBlank()) input.categoryId = myPhoto.categoryId
            }

            return fiPhotoInfoRepository.save(
                FiPhotoInfo(
                    photoId = photoId,
                    photoLink = input.photoLink!!,
                    photoDesc = input.photoDesc!!,
                    startAt = input.startAt!!,
                    endAt = input.endAt!!,
                    updatedAt = LocalDateTime.now(),

                    userInfo = mpUserInfoRepository.findById(accountId).orElseThrow { throw GraphQLNotFoundException("사용자 조회에 실패하였습니다.") },
                    categoryInfo = mdCategoryInfoRepository.findById(input.categoryId!!).orElseThrow { throw GraphQLNotFoundException("카테고리 조회에 실패하였습니다.") }
                )
            ).toDto()
        }catch (ne: GraphQLNotFoundException){
            throw ne
        }catch (e: Exception) {
            throw GraphQLBadRequestException()
        }
    }
}