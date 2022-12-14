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
class CategorySaveStep {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    protected lateinit var graphQlTester: GraphQlTester

    @Autowired
    private lateinit var authService: AuthService

    private lateinit var docs: String
    private lateinit var input: List<String>
    private lateinit var result: GraphQlTester.Response

    @Before(value = "@category-save")
    fun setup() {
        var token = ParseUtils.decode(Constants.token)
        token = ParseUtils.removePrefix(Constants.state, token)
        var accountId = authService.kakaoAuth(token)!!

        var context = AuthenticationContext
        context.setAccountId(accountId)
        AuthenticationContextHolder.setAuthenticationContext(context)
    }

    @먼저("카테고리 정보를 저장하기 위한 {string}{string} 가 있다")
    fun 카테고리_정보를_저장하기_위한_가_있다(docs: String, categoryId: String) {
        this.docs = docs
        this.input = listOf(categoryId)
    }

    @만약("내 카테고리정보 저장API를 요청하면")
    fun 내_카테고리정보_저장API를_요청하면() {
        result = this.graphQlTester.documentName(docs)
            .variable("input", input)
            .execute()
    }

    @그러면("내 카테고리정보 저장API 호출결과를 확인한다")
    fun 내_카테고리정보_저장API_호출결과를_확인한다() {
        var idx = input[0].substring(input[0].length-1).toInt()
        result.path("saveMyCategory[" + (idx - 1) + "].categoryId").entity(String::class.java).isEqualTo(input[0])
        result.path("saveMyCategory[" + (idx - 1) + "].categoryDesc").entity(String::class.java).isEqualTo(input[0])
    }
}