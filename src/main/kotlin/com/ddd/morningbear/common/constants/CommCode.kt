package com.ddd.morningbear.common.constants

/**
 * @author yoonho
 * @since 2022.11.29
 */
class CommCode {

    companion object {
        fun findPrefix(type: String): String {
            for(item in CommCode.Social.values()) {
                if(item.code == type) {
                    return item.prefix
                }
            }
            return ""
        }
    }

    enum class Social(val code: String, val prefix: String){
        KAKAO("kakao", "k::"),
        NAVER("naver", "n::"),
        APPLE("apple", "a::")
    }

    enum class Result(val code: String, val message: String) {
        K000("K000", "잘못된 요청입니다."),
        K001("K001", "토큰정보가 올바르지 않습니다."),
        K002("K002", "대상 데이터를 조회할 수 없습니다."),
        K005("K005", "일시적으로 서버를 이용할 수 없습니다. 잠시후에 다시 시도해주세요.")
    }

    enum class OrderType(val code: String) {
        // 생성일자 순서
        CREATE_ORDER_ASC("1"),
        CREATE_ORDER_DESC("2"),
        // 좋아요 받은 개수별 순서
        LIKE_ORDER_ASC("3"),
        LIKE_ORDER_DESC("4"),
    }
}