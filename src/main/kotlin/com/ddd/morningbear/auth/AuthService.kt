package com.ddd.morningbear.auth

import com.ddd.morningbear.auth.dto.KakaoTokenInfo
import com.ddd.morningbear.auth.dto.NaverTokenInfo
import com.ddd.morningbear.common.exception.ThirdPartyServerException
import com.ddd.morningbear.common.exception.TokenInvalidException
import com.ddd.morningbear.common.utils.AppPropsUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import javax.annotation.PostConstruct

@Service
class AuthService(
    private val webClientBuilder: WebClient.Builder
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private lateinit var webClient: WebClient

    @PostConstruct
    fun initWebClient() {
        this.webClient = webClientBuilder.build()
    }

    /**
     * 카카오 토큰조회
     *
     * @param accessToken [String]
     * @return KakaoTokenInfo [KakaoTokenInfo]
     * @author yoonho
     * @since 2022.11.29
     */
    fun kakaoAuth(accessToken: String?): KakaoTokenInfo? {
        try{
            val uriComponents = UriComponentsBuilder
                .fromHttpUrl(AppPropsUtils.findUrl("kapi") + "/v1/user/access_token_info")
                .build(false)

            logger.info(" >>> [kakaoAuth] request - url: {}", uriComponents.toString())
            var responseEntity = webClient.get()
                .uri(uriComponents.toUri())
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError) { Mono.error(TokenInvalidException("kakao token invalid")) }
                .onStatus(HttpStatus::is5xxServerError) { Mono.error(ThirdPartyServerException("kapi internal server error")) }
                .toEntity(KakaoTokenInfo::class.java)
                .block()
            logger.info(" >>> [kakaoAuth] response - statusCode: {}, body: {}", responseEntity?.statusCodeValue, responseEntity?.body)

            return responseEntity?.body
        }catch (e: Throwable){
            throw e
        }
    }

    /**
     * 네이버 토큰조회
     *
     * @param accessToken [String]
     * @return NaverTokenInfo [NaverTokenInfo]
     * @author yoonho
     * @since 2022.11.29
     */
    fun naverAuth(accessToken: String?): NaverTokenInfo? {
        try{
            val uriComponents = UriComponentsBuilder
                .fromHttpUrl(AppPropsUtils.findUrl("napi") + "/v1/nid/me")
                .build(false)

            logger.info(" >>> [naverAuth] request - url: {}", uriComponents.toString())
            var responseEntity = webClient.get()
                .uri(uriComponents.toUri())
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError) { Mono.error(TokenInvalidException("naver token invalid")) }
                .onStatus(HttpStatus::is5xxServerError) { Mono.error(ThirdPartyServerException("napi internal server error")) }
                .toEntity(NaverTokenInfo::class.java)
                .block()
            logger.info(" >>> [naverAuth] response - statusCode: {}, body: {}", responseEntity?.statusCodeValue, responseEntity?.body)

            return responseEntity?.body
        }catch (e: Throwable){
            throw e
        }
    }
}