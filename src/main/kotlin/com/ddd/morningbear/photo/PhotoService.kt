package com.ddd.morningbear.photo

import com.ddd.morningbear.api.photo.dto.PhotoInput
import com.ddd.morningbear.category.repository.MdCategoryInfoRepository
import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import com.ddd.morningbear.common.exception.GraphQLNotFoundException
import com.ddd.morningbear.common.utils.DateUtils
import com.ddd.morningbear.like.LikeService
import com.ddd.morningbear.like.repository.FiLikeInfoRepository
import com.ddd.morningbear.myinfo.repository.MpUserInfoRepository
import com.ddd.morningbear.photo.dto.FiPhotoInfoDto
import com.ddd.morningbear.photo.entity.FiPhotoInfo
import com.ddd.morningbear.photo.repository.FiPhotoInfoRepository
import com.ddd.morningbear.photo.repository.FiPhotoInfoRepositoryImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import kotlin.streams.toList

/**
 * @author yoonho
 * @since 2022.12.06
 */
@Service
class PhotoService(
    private val fiPhotoInfoRepository: FiPhotoInfoRepository,
    private val fiPhotoInfoRepositoryImpl: FiPhotoInfoRepositoryImpl,
    private val mdCategoryInfoRepository: MdCategoryInfoRepository,
    private val fiLikeInfoRepository: FiLikeInfoRepository,
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
     * @param size [Int]
     * @return List [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.06
     */
    fun findPhotoByCategory(accountId: String?, categoryId: String?, size: Int): List<FiPhotoInfoDto> {
        if(accountId.isNullOrBlank() || categoryId.isNullOrBlank()){
            throw GraphQLBadRequestException()
        }

        if(categoryId.equals("ALL")){
            return fiPhotoInfoRepository.findAllByUserInfoAccountId(accountId).orElseThrow {
                throw GraphQLNotFoundException("전체 카테고리에 대한 사진 조회에 실패하였습니다.")
            }.sortedByDescending { it.createdAt }.map { it.toDto() }.stream().limit(size.toLong()).toList()
        }

        return fiPhotoInfoRepository.findAllByCategoryInfoCategoryIdAndUserInfoAccountId(categoryId, accountId).orElseThrow {
            throw GraphQLNotFoundException("카테고리별 사진 조회에 실패하였습니다.")
        }.sortedByDescending { it.createdAt }.map { it.toDto() }.stream().limit(size.toLong()).toList()
    }

    /**
     * 사진 전체 조회
     *
     * @param accountId [String]
     * @return List [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.12
     */
    fun findAllPhoto(accountId: String): List<FiPhotoInfoDto>? {
        return fiPhotoInfoRepository.findAllByUserInfoAccountId(accountId).orElseGet(null).sortedByDescending { it.createdAt }.map { it.toDto() }
    }

    /**
     * 순서별 사진리스트 조회
     *
     * @param size [Int]
     * @param orderType [String]
     * @param order [String]
     * @return List [FiPhotoInfoDto]
     * @author yoonho
     * @since 2022.12.13
     */
    fun findPhotoByOrderType(size: Int, orderType: String): List<FiPhotoInfoDto> {
        if(fiPhotoInfoRepository.count() < size){
            throw GraphQLBadRequestException("요청하신 size보다 저장된 사진의 개수가 적습니다.")
        }

        when(orderType) {
            CommCode.OrderType.CREATE_ORDER_ASC.code -> return fiPhotoInfoRepositoryImpl.findPhotoByOrderType(size, orderType).map { it.toDto() }
            CommCode.OrderType.CREATE_ORDER_DESC.code -> return fiPhotoInfoRepositoryImpl.findPhotoByOrderType(size, orderType).map { it.toDto() }
            CommCode.OrderType.LIKE_ORDER_ASC.code -> {
                if(fiLikeInfoRepository.count() < size){
                    throw GraphQLBadRequestException("요청하신 size보다 저장된 응원하기의 개수가 적습니다.")
                }
                return fiPhotoInfoRepositoryImpl.findPhotoByAccountIdList(size, orderType).map { it.toDto() }
            }
            CommCode.OrderType.LIKE_ORDER_DESC.code -> {
                if(fiLikeInfoRepository.count() < size){
                    throw GraphQLBadRequestException("요청하신 size보다 저장된 응원하기의 개수가 적습니다.")
                }
                return fiPhotoInfoRepositoryImpl.findPhotoByAccountIdList(size, orderType).map { it.toDto() }
            }
            else -> return fiPhotoInfoRepositoryImpl.findPhotoByOrderType(size, CommCode.OrderType.CREATE_ORDER_DESC.code).map { it.toDto() }
        }
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
            lateinit var createdAt: LocalDateTime
            if(input.photoId.isNullOrBlank()){
                photoId = UUID.randomUUID().toString()
                createdAt = LocalDateTime.now()
            }else{
                photoId = input.photoId!!
                val myPhoto = fiPhotoInfoRepository.findById(photoId).orElseThrow {
                    throw GraphQLNotFoundException("사진ID에 해당하는 사진정보를 찾을 수 없습니다.")
                }

                if(input.photoLink.isNullOrBlank()) input.photoLink = myPhoto.photoLink
                if(input.photoDesc.isNullOrBlank()) input.photoDesc = myPhoto.photoDesc
                if(input.startAt.isNullOrBlank()) input.startAt = myPhoto.startAt
                if(input.endAt.isNullOrBlank()) input.endAt = myPhoto.endAt
                if(input.categoryId.isNullOrBlank()) input.categoryId = myPhoto.categoryInfo.categoryId
                createdAt = myPhoto.createdAt
            }

            val startTime = DateUtils.setStringToTime(input.startAt!!)
            val endTime = DateUtils.setStringToTime(input.endAt!!)
            if(startTime.isAfter(endTime)) {
                throw GraphQLBadRequestException("올바른 시작 및 종료시간이 아닙니다.")
            }

            return fiPhotoInfoRepository.save(
                FiPhotoInfo(
                    photoId = photoId,
                    photoLink = input.photoLink!!,
                    photoDesc = input.photoDesc!!,
                    startAt = input.startAt!!,
                    endAt = input.endAt!!,
                    updatedAt = LocalDateTime.now(),
                    createdAt = createdAt,

                    userInfo = mpUserInfoRepository.findById(accountId).orElseThrow { throw GraphQLNotFoundException("사용자 조회에 실패하였습니다.") },
                    categoryInfo = mdCategoryInfoRepository.findById(input.categoryId!!).orElseThrow { throw GraphQLNotFoundException("카테고리 조회에 실패하였습니다.") }
                )
            ).toDto()
        }catch (be: GraphQLBadRequestException){
            throw be
        }catch (ne: GraphQLNotFoundException){
            throw ne
        }catch (e: Exception) {
            throw GraphQLBadRequestException()
        }
    }

    /**
     * 사진 삭제
     *
     * @param photoId [String]
     * @return result [Boolean]
     * @author yoonho
     * @since 2022.12.16
     */
    fun deleteMyPhoto(photoId: String): FiPhotoInfoDto {
        try{
            val deletePhotoInfo = fiPhotoInfoRepository.findById(photoId).orElseThrow {
                throw GraphQLBadRequestException("사진정보가 존재하지 않습니다.")
            }.toDto()

            fiPhotoInfoRepository.deleteById(photoId)

            return deletePhotoInfo
        }catch(be: GraphQLBadRequestException) {
            throw be
        }catch (e: Exception){
            throw GraphQLBadRequestException()
        }
    }
}