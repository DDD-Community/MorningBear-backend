package com.ddd.morningbear.like

import com.ddd.morningbear.api.like.dto.LikeInput
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import com.ddd.morningbear.common.exception.GraphQLNotFoundException
import com.ddd.morningbear.like.dto.FiLikeInfoDto
import com.ddd.morningbear.like.entity.FiLikeInfo
import com.ddd.morningbear.like.entity.pk.FiLikeInfoPk
import com.ddd.morningbear.like.repository.FiLikeInfoRepository
import com.ddd.morningbear.like.repository.FiLikeInfoRepositoryImpl
import com.ddd.morningbear.myinfo.repository.MpUserInfoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author yoonho
 * @since 2022.12.05
 */
@Service
class LikeService(
    private val fiLikeInfoRepository: FiLikeInfoRepository,
    private val mpUserInfoRepository: MpUserInfoRepository,
    private val fiLikeInfoRepositoryImpl: FiLikeInfoRepositoryImpl
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 내가 받은 좋아요 목록
     *
     * @param accountId [String]
     * @return List [FiLikeInfoDto]
     * @author yoonho
     * @since 2022.12.12
     */
    fun findTakenInfo(accountId: String): List<FiLikeInfoDto>? {
        return fiLikeInfoRepository.findAllByFiLikeInfoPkTakenAccountId(accountId).orElseGet(null).map { it.toDto() }
    }

    /**
     * 내가 좋아요 준 목록
     *
     * @param accountId [String]
     * @return List [FiLikeInfoDto]
     * @author yoonho
     * @since 2022.12.12
     */
    fun findGivenInfo(accountId: String): List<FiLikeInfoDto>? {
        return fiLikeInfoRepository.findAllByFiLikeInfoPkGivenAccountId(accountId).orElseGet(null).map { it.toDto() }
    }

    /**
     * 가장 좋아요를 많이 받은 사용자 조회
     *
     * @return result [String]
     * @author yoonho
     * @since 2022.12.13
     */
    fun findMostPopularInfo(): String {
        if(fiLikeInfoRepository.count() <= 0){
            throw GraphQLNotFoundException("응원하기 데이터가 존재하지 않아 인기있는 사용자를 조회할 수 없습니다.")
        }
        return fiLikeInfoRepositoryImpl.findMostPopularUser()
    }

//    /**
//     * 좋아요를 많이 순서대로 사용자리스트 조회
//     *
//     * @param size [Int]
//     * @return List [String]
//     * @author yoonho
//     * @since 2022.12.13
//     */
//    fun findPopularInfo(size: Int): List<PopularLikeDto> {
//        return fiLikeInfoRepositoryImpl.findPopularUser(size)
//    }

    /**
     * 좋아요 등록
     *
     * @param givenAccountId [String]
     * @param input [LikeInput]
     * @return result [Boolean]
     * @author yoonho
     * @since 2022.12.05
     */
    @Transactional(rollbackFor = [Exception::class])
    fun saveLike(givenAccountId: String, input: LikeInput): FiLikeInfoDto  {
        try{
            // 좋아요시 나의피드와 상대방피드 데이터가 모두 존재해야 한다
            if(!mpUserInfoRepository.existsById(givenAccountId) || !mpUserInfoRepository.existsById(input.takenAccountId)){
                throw GraphQLNotFoundException("피드정보를 찾을 수 없습니다.")
            }

            return fiLikeInfoRepository.save(
                        FiLikeInfo(
                            fiLikeInfoPk = FiLikeInfoPk(
                                likeCode = input.likeCode,
                                takenAccountId = input.takenAccountId,
                                givenAccountId = givenAccountId
                            ),
                            takenInfo = mpUserInfoRepository.findById(input.takenAccountId).orElseThrow { throw GraphQLNotFoundException("내가 좋아요한 피드목록을 찾을 수 없습니다.") },
                            givenInfo = mpUserInfoRepository.findById(givenAccountId).orElseThrow { throw GraphQLNotFoundException("내가 좋아요한 피드목록을 찾을 수 없습니다.") },
                        )
                    ).toDto()
        }catch (nfe: GraphQLNotFoundException) {
            throw nfe
        }catch (e: Exception){
            throw GraphQLBadRequestException()
        }
    }

    /**
     * 좋아요 취소
     *
     * @param givenAccountId [String]
     * @param input [LikeInput]
     * @return result [Boolean]
     * @author yoonho
     * @since 2022.12.05
     */
    @Transactional(rollbackFor = [Exception::class])
    fun deleteLike(givenAccountId: String, input: LikeInput): Boolean {
        try {
            // 좋아요 취소시 나의피드와 상대방피드 데이터가 모두 존재해야 한다
            if(!mpUserInfoRepository.existsById(givenAccountId) || !mpUserInfoRepository.existsById(input.takenAccountId)){
                throw GraphQLNotFoundException("피드정보를 찾을 수 없습니다.")
            }

            fiLikeInfoRepository.deleteById(
                FiLikeInfoPk(
                    likeCode = input.likeCode,
                    givenAccountId = givenAccountId,
                    takenAccountId = input.takenAccountId
                )
            )
        }catch (nfe: GraphQLNotFoundException) {
            throw nfe
        }catch (e: Exception){
            throw GraphQLBadRequestException()
        }

        return true
    }
}