package com.ddd.morningbear.cucumber.feature.step.myinfo

import com.ddd.morningbear.auth.AuthService
import com.ddd.morningbear.common.Constants
import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.context.AuthenticationContext
import com.ddd.morningbear.common.context.AuthenticationContextHolder
import com.ddd.morningbear.common.utils.TokenUtils
import io.cucumber.java.Before
import io.cucumber.java.BeforeAll
import io.cucumber.java.ko.그러면
import io.cucumber.java.ko.만약
import io.cucumber.java.ko.먼저
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.test.tester.GraphQlTester

class MyInfoSaveStep {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    protected lateinit var graphQlTester: GraphQlTester

    @Autowired
    private lateinit var authService: AuthService

    private lateinit var docs: String
    private lateinit var input: Map<String, String>
    private lateinit var accountId: String
    private lateinit var result: GraphQlTester.Response

    @Before(value = "@myInfo-save")
    fun setup() {
        var token = TokenUtils.decodeToken(Constants.token, Constants.state)
        accountId = TokenUtils.encodeToken(authService.kakaoAuth(token), Constants.state)

        var context = AuthenticationContext
        context.setAccountId(accountId)
        AuthenticationContextHolder.setAuthenticationContext(context)
    }

    @먼저("내정보를 저장하기 위한 {string}{string}{string}{string}{string} 가 있다")
    fun 내정보를_저장하기_위한_가_있다(docs: String, nickName: String, photoLink: String, memo: String, wakeUpAt: String) {
        this.docs = docs

        input = mapOf(
            "nickName" to nickName,
            "photoLink" to photoLink,
            "memo" to memo,
            "wakeUpAt" to wakeUpAt
        )
    }

    @만약("내정보 저장API를 요청하면")
    fun 내정보_저장API를_요청하면() {
        result = this.graphQlTester.documentName(docs)
            .variable("input", input)
            .execute()
    }

    @그러면("내정보 저장API 호출결과를 확인한다")
    fun 내정보_저장API_호출결과를_확인한다() {
        result.path("saveMyInfo.accountId").entity(String::class.java).isEqualTo(accountId)
        result.path("saveMyInfo.nickName").entity(String::class.java).isEqualTo(input["nickName"]!!)
        result.path("saveMyInfo.memo").entity(String::class.java).isEqualTo(input["memo"]!!)
        result.path("saveMyInfo.photoLink").entity(String::class.java).isEqualTo(input["photoLink"]!!)
        result.path("saveMyInfo.wakeUpAt").entity(String::class.java).isEqualTo(input["wakeUpAt"]!!)
    }
}