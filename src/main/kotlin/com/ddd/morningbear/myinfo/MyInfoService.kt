package com.ddd.morningbear.myinfo

import com.ddd.morningbear.api.myinfo.dto.MyInfoInput
import com.ddd.morningbear.badge.BadgeService
import com.ddd.morningbear.category.CategoryService
import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import com.ddd.morningbear.common.exception.GraphQLNotFoundException
import com.ddd.morningbear.like.LikeService
import com.ddd.morningbear.myinfo.dto.MpUserInfoDto
import com.ddd.morningbear.myinfo.dto.SearchUserDto
import com.ddd.morningbear.myinfo.entity.MpUserInfo
import com.ddd.morningbear.myinfo.repository.MpUserInfoRepository
import com.ddd.morningbear.myinfo.repository.MpUserInfoRepositoryImpl
import com.ddd.morningbear.photo.PhotoService
import com.ddd.morningbear.report.ReportService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.stream.Collectors

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Service
class MyInfoService(
    private val mpUserInfoRepository: MpUserInfoRepository,
    private val mpUserInfoRepositoryImp: MpUserInfoRepositoryImpl,
    private val categoryService: CategoryService,
    private val badgeService: BadgeService,
    private val reportService: ReportService,
    private val photoService: PhotoService,
    private val likeService: LikeService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 사용자 정보 조회
     *
     * @param accountId [String]
     * @return result [MpUserInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    fun findUserInfo(accountId: String, totalSize: Int, categorySize: Int): MpUserInfoDto {
        val myInfo = mpUserInfoRepositoryImp.findByIdNotInBlockUser(accountId).orElseThrow {
            throw GraphQLNotFoundException("사용자정보 조회에 실패하였습니다.")
        }.toDto()

        // 내 뱃지리스트 조회
        myInfo.badgeList = badgeService.findMyAllBadge(accountId)
        // 내 카테고리리스트 조회
        myInfo.categoryList = categoryService.findMyCategory(accountId)

        // 좋아요 정보 조회
        myInfo.givenLike = likeService.findGivenInfo(accountId)?.toMutableList()
        myInfo.givenLikeCnt = myInfo.givenLike?.size
        myInfo.takenLike = likeService.findTakenInfo(accountId)?.toMutableList()
        myInfo.takenLikeCnt = myInfo.takenLike?.size

        // 리포트 조회
        myInfo.reportInfo = reportService.createReport(accountId)
        // 카테고리별 사진조회
        val categoryList = categoryService.findAllCategory()
        categoryList
            .forEach {
                myInfo.photoInfoByCategory.add(
                    MpUserInfoDto.PhotoInfoByCategory(
                        categoryId = it.categoryId,
                        categoryDesc = it.categoryDesc,
                        photoInfo = photoService.findPhotoByCategory(accountId, it.categoryId, categorySize)
                    )
                )
            }

        // 전체사진 리스트 size개수만큼만 조회
        if(!myInfo.photoInfo.isNullOrEmpty()) {
            myInfo.photoInfo = myInfo.photoInfo!!.stream().limit(totalSize.toLong()).collect(Collectors.toList())
        }

        return myInfo
    }

    /**
     * 사용자 검색
     *
     * @param keyword [String]
     * @return List [MpUserInfoDto]
     * @author yoonho
     * @since 2022.12.07
     */
    fun searchUserInfo(keyword: String): List<SearchUserDto> {
        return mpUserInfoRepositoryImp.findUserInfoByNickName(keyword).map { it.toSearchDto() }
    }

    /**
     * 가장 인기있는 사용자 조회
     *
     * @return result [MpUserInfoDto]
     * @author yoonho
     * @since 2022.12.13
     */
    fun findPopularUserInfo(size: Int): List<MpUserInfoDto> {
        val popularList = likeService.findPopularInfo(size)
        val result: MutableList<MpUserInfoDto> = mutableListOf()
        popularList.forEach {
            result.add(
                this.findUserInfo(it.accountId, CommCode.photoSize, CommCode.photoSize)
            )
        }

        return result
    }

    /**
     * 내정보 저장
     *
     * @param accountId [String]
     * @param input [MyInfoInput]
     * @return result [MpUserInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    @Transactional(rollbackFor = [Exception::class])
    fun saveMyInfo(accountId: String, input: MyInfoInput): MpUserInfoDto {
        if(accountId.isNullOrBlank()){
            throw GraphQLBadRequestException("로그인정보가 존재하지 않습니다.")
        }

        var createdAt: LocalDateTime = LocalDateTime.now()
        var goalUpdatedAt: LocalDateTime = LocalDateTime.now()

        try{
            if(mpUserInfoRepository.existsById(accountId)) {
                /* 회원정보 업데이트 */
                val myInfo = mpUserInfoRepository.findById(accountId).orElseThrow { throw GraphQLBadRequestException() }
                // Patch방식으로 이미 저장된 사용자정보에서 input으로 전달받은 데이터만 update
                if(input.nickName.isNullOrBlank()) input.nickName = myInfo.nickName
                if(input.photoLink.isNullOrBlank()) input.photoLink = myInfo.photoLink
                if(input.memo.isNullOrBlank()) input.memo = myInfo.memo
                if(input.wakeUpAt.isNullOrBlank()) input.wakeUpAt = myInfo.wakeUpAt
                if(input.goal.isNullOrBlank()) {
                    input.goal = myInfo.goal
                    goalUpdatedAt = myInfo.goalUpdatedAt
                }
                createdAt = myInfo.createdAt
            }

            mpUserInfoRepository.save(
                MpUserInfo(
                    accountId = accountId,
                    nickName = input.nickName,
                    photoLink = input.photoLink,
                    memo = input.memo,
                    wakeUpAt = input.wakeUpAt,
                    goal = input.goal,
                    goalUpdatedAt = goalUpdatedAt,
                    updatedAt = LocalDateTime.now(),
                    createdAt = createdAt
                )
            )
            return this.findUserInfo(accountId, CommCode.photoSize, CommCode.photoSize)
        }catch (ne: GraphQLNotFoundException){
            throw ne
        }catch (be: GraphQLBadRequestException){
            throw be
        }catch(e: Exception){
            throw GraphQLBadRequestException()
        }
    }

    /**
     * 탈퇴하기
     *
     * @param accountId [String]
     * @author yoonho
     * @since 2022.12.04
     */
    @Transactional(rollbackFor = [Exception::class])
    fun deleteMyInfo(accountId: String): Boolean {
        try{
            if(!mpUserInfoRepository.existsById(accountId)) {
                throw GraphQLBadRequestException("이미 탈퇴했거나 존재하지 않는 회원입니다.")
            }

            // 회원테이블 메타정보 삭제
            mpUserInfoRepository.deleteById(accountId)
        }catch(be: GraphQLBadRequestException) {
            throw be
        }catch(e: Exception){
            throw GraphQLBadRequestException()
        }

        return true
    }
}