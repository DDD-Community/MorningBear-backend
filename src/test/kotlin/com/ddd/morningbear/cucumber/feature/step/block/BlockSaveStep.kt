package com.ddd.morningbear.cucumber.feature.step.block

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

/**
 * @author yoonho
 * @since 2022.12.04
 */
class BlockSaveStep {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    protected lateinit var graphQlTester: GraphQlTester

    @Autowired
    private lateinit var authService: AuthService

    private lateinit var docs: String
    private lateinit var input: String
    private lateinit var accountId: String
    private lateinit var result: GraphQlTester.Response

    @Before(value = "@block-save")
    fun setup() {
        var token = TokenUtils.decodeToken(Constants.token, Constants.state)
        accountId = TokenUtils.encodeToken(authService.kakaoAuth(token), Constants.state)

        var context = AuthenticationContext
        context.setAccountId(accountId)
        AuthenticationContextHolder.setAuthenticationContext(context)
    }

    @먼저("사용자차단을 위한 {string}{string} 가 있다")
    fun 사용자차단을_위한_가_있다(docs: String, blockAccountId: String) {
        this.docs = docs
        this.input = blockAccountId
    }

    @만약("사용자차단API를 요청하면")
    fun 사용자차단API를_요청하면() {
        result = this.graphQlTester.documentName(docs)
            .variable("input", input)
            .execute()
    }

    @그러면("사용자차단API 호출결과를 확인한다")
    fun 사용자차단API_호출결과를_확인한다() {
        var idx = input.substring(input.length-1).toInt()
        result.path("saveBlock[" + (idx - 1) + "].accountId").entity(String::class.java).isEqualTo(accountId)
        result.path("saveBlock[" + (idx - 1) + "].blockAccountId").entity(String::class.java).isEqualTo(input)
    }
}