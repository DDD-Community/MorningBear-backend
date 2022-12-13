package com.ddd.morningbear.api.myinfo

import com.ddd.morningbear.api.myinfo.dto.MyInfoInput
import com.ddd.morningbear.common.BaseController
import com.ddd.morningbear.common.context.AuthenticationContextHolder
import com.ddd.morningbear.myinfo.MyInfoService
import com.ddd.morningbear.myinfo.dto.MpUserInfoDto
import com.ddd.morningbear.myinfo.dto.SearchUserDto
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
class MyInfoController(
    private val myInfoService: MyInfoService
): BaseController() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 내정보 조회
     *
     * @return result [MpUserInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    @QueryMapping
    fun findMyInfo(): MpUserInfoDto {
        val accountId = getAuthenticationContextAccountId()
        return myInfoService.findUserInfo(accountId)
    }

    /**
     * 사용자정보 조회
     *
     * @param accountId [String]
     * @return result [MpUserInfoDto]
     * @author yoonho
     * @since 2022.12.10
     */
    @QueryMapping
    fun findUserInfo(@Argument accountId: String): MpUserInfoDto {
        return myInfoService.findUserInfo(accountId)
    }

    /**
     * 사용자 검색
     *
     * @param keyword [String]
     * @return List [MpUserInfoDto]
     * @author yoonho
     * @since 2022.12.07
     */
    @QueryMapping
    fun searchUserInfo(@Argument keyword: String): List<SearchUserDto> {
        return myInfoService.searchUserInfo(keyword)
    }

    /**
     * 내정보 저장
     *
     * @param input [MyInfoInput]
     * @return result [MpUserInfoDto]
     * @author yoonho
     * @since 2022.12.04
     */
    @MutationMapping
    fun saveMyInfo(@Argument userInput: MyInfoInput): MpUserInfoDto {
        val accountId = getAuthenticationContextAccountId()
        return myInfoService.saveMyInfo(accountId, userInput)
    }

    /**
     * 탈퇴하기
     *
     * @return result [Boolean]
     * @author yoonho
     * @since 2022.12.04
     */
    @MutationMapping
    fun deleteMyInfo(): Boolean {
        val accountId = getAuthenticationContextAccountId()
        return myInfoService.deleteMyInfo(accountId)
    }
}