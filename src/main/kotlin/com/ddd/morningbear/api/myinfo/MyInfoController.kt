package com.ddd.morningbear.api.myinfo

import com.ddd.morningbear.api.myinfo.dto.MyInfoInput
import com.ddd.morningbear.common.BaseController
import com.ddd.morningbear.common.context.AuthenticationContextHolder
import com.ddd.morningbear.myinfo.MyInfoService
import com.ddd.morningbear.myinfo.dto.MpUserInfoDto
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
     * TODO:
     *  - (O) 다른 사용자 검색기능
     *  - (O) 애플로그인 (API호출할수 있게만 짜놓을 것) -> 어차피 목요일날 영빈이한테 물어봐야 됨
     *  - (O) Cipher알고리즘써서 accountId, accessToken 인코딩, 디코딩
     *  - (O) MP_USER_INFO랑 FI_FEED_INFO 테이블 합치기(나눌 필요 없을듯) -> 노션도 수정
     *  - input 파라미터 명칭 및 포함데이터 수정 (필요한 데이터만 포함하도록)
     *  - 사용자 리포트 기능 (기능 나열 먼저할 것)
     */

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

    @QueryMapping
    fun findUserInfo(@Argument input: String): MpUserInfoDto {
        return myInfoService.findUserInfo(input)
    }

    /**
     * 사용자 검색
     *
     * @param input [String]
     * @return List [MpUserInfoDto]
     * @author yoonho
     * @since 2022.12.07
     */
    @QueryMapping
    fun searchUserInfo(@Argument input: String): List<MpUserInfoDto> {
        return myInfoService.searchUserInfo(input)
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
    fun saveMyInfo(@Argument input: MyInfoInput): MpUserInfoDto {
        val accountId = getAuthenticationContextAccountId()
        return myInfoService.saveMyInfo(accountId, input)
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