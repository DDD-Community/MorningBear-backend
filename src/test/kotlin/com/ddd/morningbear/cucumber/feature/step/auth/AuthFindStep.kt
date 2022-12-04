package com.ddd.morningbear.cucumber.feature.step.auth

import com.ddd.morningbear.common.constants.CommCode
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
class AuthFindStep {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    protected lateinit var graphQlTester: GraphQlTester

    private lateinit var docs: String
    private lateinit var result: GraphQlTester.Response

    @먼저("로그인 기본정보 조회를 위한 {string} 가 있다")
    fun 로그인_기본정보_조회를_위한_가_있다(docs: String) {
        this.docs = docs
    }

    @만약("로그인 기본정보 조회API를 요청하면")
    fun 로그인_기본정보_조회API를_요청하면() {
        result = this.graphQlTester.documentName(docs).execute()
    }

    @그러면("호출결과 {string}{string}{string}{string} 를 확인한다")
    fun 호출결과_를_확인한다(redirectUri: String, state: String, jsKey: String, nativeKey: String){
        var idx = -1
        when(state){
            CommCode.Social.KAKAO.code -> idx = 0
            CommCode.Social.NAVER.code -> idx = 1
        }

        result.path("findLoginInfo[" + idx + "].state").entity(String::class.java).isEqualTo(state)
        result.path("findLoginInfo[" + idx + "].jsKey").entity(String::class.java).isEqualTo(jsKey)
        result.path("findLoginInfo[" + idx + "].nativeKey").entity(String::class.java).isEqualTo(nativeKey)
        result.path("findLoginInfo[" + idx + "].redirectUri").entity(String::class.java).isEqualTo(redirectUri)
    }
}