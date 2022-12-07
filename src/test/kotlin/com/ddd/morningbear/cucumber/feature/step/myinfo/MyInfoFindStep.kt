package com.ddd.morningbear.cucumber.feature.step.myinfo

import com.ddd.morningbear.auth.AuthService
import com.ddd.morningbear.common.Constants
import com.ddd.morningbear.common.context.AuthenticationContext
import com.ddd.morningbear.common.context.AuthenticationContextHolder
import com.ddd.morningbear.common.utils.ParseUtils
import io.cucumber.java.Before
import io.cucumber.java.ko.그러면
import io.cucumber.java.ko.만약
import io.cucumber.java.ko.먼저
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.test.tester.GraphQlTester

/**
 * @author yoonho
 * @since 2022.12.04
 */
class MyInfoFindStep {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    protected lateinit var graphQlTester: GraphQlTester

    @Autowired
    private lateinit var authService: AuthService

    private lateinit var docs: String
    private lateinit var result: GraphQlTester.Response

    @Before(value = "@myInfo-find")
    fun setup() {
        var token = ParseUtils.decode(Constants.token)
        token = ParseUtils.removePrefix(Constants.state, token)
        var accountId = authService.kakaoAuth(token)!!

        var context = AuthenticationContext
        context.setAccountId(accountId!!)
        AuthenticationContextHolder.setAuthenticationContext(context)
    }

    @먼저("내정보를 조회하기 위한 {string} 가 있다")
    fun 내정보를_조회하기_위한_가_있다(docs: String) {
        this.docs = docs
    }

    @만약("내정보 조회API를 요청하면")
    fun 내정보_조회API를_요청하면() {
        result = this.graphQlTester.documentName(docs)
            .execute()
    }

    @그러면("호출결과 {string}{string}{string}{string}{string} 를 확인한다")
    fun 호출결과를_확인한다(accountId: String, nickName: String, photoLink: String, memo: String, wakeUpAt: String) {
        result.path("findMyInfo.accountId").entity(String::class.java).isEqualTo(accountId)
        result.path("findMyInfo.nickName").entity(String::class.java).isEqualTo(nickName)
        result.path("findMyInfo.memo").entity(String::class.java).isEqualTo(memo)
        result.path("findMyInfo.photoLink").entity(String::class.java).isEqualTo(photoLink)
        result.path("findMyInfo.wakeUpAt").entity(String::class.java).isEqualTo(wakeUpAt)
    }
}