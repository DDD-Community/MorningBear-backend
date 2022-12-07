package com.ddd.morningbear.like

import com.ddd.morningbear.api.like.dto.LikeInput
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import com.ddd.morningbear.common.exception.GraphQLNotFoundException
import com.ddd.morningbear.like.dto.FiLikeInfoDto
import com.ddd.morningbear.like.entity.FiLikeInfo
import com.ddd.morningbear.like.entity.pk.FiLikeInfoPk
import com.ddd.morningbear.like.repository.FiLikeInfoRepository
import com.ddd.morningbear.myinfo.repository.MpUserInfoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * @author yoonho
 * @since 2022.12.05
 */
@Service
class LikeService(
    private val fiLikeInfoRepository: FiLikeInfoRepository,
    private val mpUserInfoRepository: MpUserInfoRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 내가 받은 좋아요 목록
     *
     * @param accountId [String]
     * @return List [FiLikeInfoDto]
     * @author yoonho
     * @since 2022.12.05
     */
    fun findTakenLike(accountId: String): List<FiLikeInfoDto> = fiLikeInfoRepository.findAllByFiLikeInfoPkTakenAccountId(accountId).orElseThrow {
        throw GraphQLNotFoundException("내가 받은 좋아요목록을 찾을 수 없습니다.")
    }.map { it.toDto() }

    /**
     * 내가 좋아요한 피드 목록
     *
     * @param accountId [String]
     * @return List [FiLikeInfoDto]
     * @author yoonho
     * @since 2022.12.05
     */
    fun findGivenLike(accountId: String): List<FiLikeInfoDto> = fiLikeInfoRepository.findAllByFiLikeInfoPkGivenAccountId(accountId).orElseThrow {
        throw GraphQLNotFoundException("내가 좋아요한 피드목록을 찾을 수 없습니다.")
    }.map { it.toDto() }

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
    fun saveLike(givenAccountId: String, input: LikeInput): Boolean  {
        try{
            // 좋아요시 나의피드와 상대방피드 데이터가 모두 존재해야 한다
            if(!mpUserInfoRepository.existsById(givenAccountId) || !mpUserInfoRepository.existsById(input.takenAccountId)){
                throw GraphQLNotFoundException("피드정보를 찾을 수 없습니다.")
            }

            fiLikeInfoRepository.save(
                FiLikeInfo(
                    fiLikeInfoPk = FiLikeInfoPk(
                        likeCode = input.likeCode,
                        takenAccountId = input.takenAccountId,
                        givenAccountId = givenAccountId
                    ),
                    takenInfo = mpUserInfoRepository.findById(input.takenAccountId).orElseThrow { throw GraphQLNotFoundException("내가 좋아요한 피드목록을 찾을 수 없습니다.") },
                    givenInfo = mpUserInfoRepository.findById(givenAccountId).orElseThrow { throw GraphQLNotFoundException("내가 좋아요한 피드목록을 찾을 수 없습니다.") },
                    updatedAt = LocalDateTime.now()
                )
            )

            logger.info(" >>> [saveLike] likeCode: {}, takenAccountId: {}, givenAccountId: {}", input.likeCode, input.takenAccountId, givenAccountId)
        }catch (nfe: GraphQLNotFoundException) {
            throw nfe
        }catch (e: Exception){
            logger.error(" >>> [saveLike] msg: {}", e.message)
            throw GraphQLBadRequestException()
        }

        return true
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