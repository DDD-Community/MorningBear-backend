package com.ddd.morningbear.common.constants

/**
 * @author yoonho
 * @since 2022.11.29
 */
class CommCode {
    enum class Social(val code: String){
        KAKAO("kakao"),
        NAVER("naver"),
        APPLE("apple")
    }

    enum class Result(val code: String, val message: String) {
        K000("K000", "Not authorized, Bad Request"),
        K001("K001", "Not authorized, Token invalid"),
        K002("K002", "Not authorized, Not found target data"),
        K005("K005", "Invalid authorization grant, grant invalid, grant expired, or grant revoked")
    }
}