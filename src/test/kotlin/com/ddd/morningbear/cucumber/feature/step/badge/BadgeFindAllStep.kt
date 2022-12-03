package com.ddd.morningbear.cucumber.feature.step.badge

import com.ddd.morningbear.auth.AuthService
import com.ddd.morningbear.common.Constants
import com.ddd.morningbear.common.context.AuthenticationContext
import com.ddd.morningbear.common.context.AuthenticationContextHolder
import com.ddd.morningbear.common.utils.TokenUtils
import io.cucumber.java.Before
import io.cucumber.java.ko.그러면
import io.cucumber.java.ko.만약
import io.cucumber.java.ko.먼저
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.test.tester.GraphQlTester

class BadgeFindAllStep {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    protected lateinit var graphQlTester: GraphQlTester

    @Autowired
    private lateinit var authService: AuthService

    private lateinit var docs: String
    private lateinit var result: GraphQlTester.Response

    @Before(value = "@badge-findAll")
    fun setup() {
        var token = TokenUtils.decodeToken(Constants.token, Constants.state)
        var accountId = TokenUtils.encodeToken(authService.kakaoAuth(token), Constants.state)

        var context = AuthenticationContext
        context.setAccountId(accountId!!)
        AuthenticationContextHolder.setAuthenticationContext(context)
    }

    @먼저("뱃지 정보를 조회하기 위한 {string} 가 있다")
    fun 뱃지_정보를_조회하기_위한_가_있다(docs: String) {
        this.docs = docs
    }

    @만약("뱃지 조회API를 요청하면")
    fun 뱃지_조회API를_요청하면() {
        result = this.graphQlTester.documentName(docs)
            .execute()
    }

    @그러면("뱃지 조회API 호출결과를 확인한다")
    fun 뱃지_조회API_호출결과를_확인한다() {

    }
}