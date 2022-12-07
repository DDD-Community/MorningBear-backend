package com.ddd.morningbear.cucumber.feature.step.category

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
class CategoryFindAllStep {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    protected lateinit var graphQlTester: GraphQlTester

    @Autowired
    private lateinit var authService: AuthService

    private lateinit var docs: String
    private lateinit var result: GraphQlTester.Response

    @Before(value = "@category-findAll")
    fun setup() {
        var token = ParseUtils.decode(Constants.token)
        token = ParseUtils.removePrefix(Constants.state, token)
        var accountId = authService.kakaoAuth(token)!!

        var context = AuthenticationContext
        context.setAccountId(accountId)
        AuthenticationContextHolder.setAuthenticationContext(context)
    }

    @먼저("카테고리 정보를 조회하기 위한 {string} 가 있다")
    fun 카테고리_정보를_조회하기_위한_가_있다(docs: String) {
        this.docs = docs
    }

    @만약("카테고리 조회API를 요청하면")
    fun 카테고리_조회API를_요청하면() {
        result = this.graphQlTester.documentName(docs)
            .execute()
    }

    @그러면("카테고리 조회API 호출결과 {string}{string} 를 확인한다")
    fun 카테고리_조회API_호출결과_를_확인한다(categoryId: String, categoryDesc: String) {
        var idx = categoryId.substring(categoryId.length-1).toInt()
        result.path("findAllCategory[" + (idx - 1) + "].categoryId").entity(String::class.java).isEqualTo(categoryId)
        result.path("findAllCategory[" + (idx - 1) + "].categoryDesc").entity(String::class.java).isEqualTo(categoryDesc)
    }
}