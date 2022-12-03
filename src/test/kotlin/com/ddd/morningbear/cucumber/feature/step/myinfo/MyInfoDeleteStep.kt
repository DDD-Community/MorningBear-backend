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

class MyInfoDeleteStep {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    protected lateinit var graphQlTester: GraphQlTester

    @Autowired
    private lateinit var authService: AuthService

    private lateinit var docs: String
    private lateinit var input: Map<String, String>
    private lateinit var accountId: String
    private lateinit var result: GraphQlTester.Response

    @Before(value = "@myInfo-delete")
    fun setup() {
        var token = TokenUtils.decodeToken(Constants.token, Constants.state)
        accountId = TokenUtils.encodeToken(authService.kakaoAuth(token), Constants.state)

        var context = AuthenticationContext
        context.setAccountId(accountId!!)
        AuthenticationContextHolder.setAuthenticationContext(context)
    }

    @먼저("내정보 탈퇴를 위한 {string} 가 있다")
    fun 내정보_탈퇴를_위한_가_있다(docs: String) {
        this.docs = docs
    }

    @만약("탈퇴하기 API를 요청하면")
    fun 탈퇴하기_API를_요청하면() {
        result = this.graphQlTester.documentName(docs)
            .execute()
    }

    @그러면("호출결과 {string} 를 확인한다")
    fun 호출결과_를_확인한다(response: String) {
        result.path("deleteMyInfo").entity(String::class.java).isEqualTo(response!!)
    }
}