package com.ddd.morningbear.login

import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.exception.ThirdPartyServerException
import com.ddd.morningbear.common.exception.TokenInvalidException
import com.ddd.morningbear.common.utils.AppPropsUtils
import com.ddd.morningbear.common.utils.TokenUtils
import com.ddd.morningbear.login.dto.TokenInfo
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import javax.annotation.PostConstruct

@Service
class LoginService (
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
     * @param code [String]
     * @param type [String]
     * @return TokenInfo [TokenInfo]
     * @author yoonho
     * @since 2022.11.29
     */
    fun kakaoToken(code: String?, type: String): TokenInfo {
        try{
            var appconfig = AppPropsUtils.findClientInfoByType(type)

            val params: MultiValueMap<String, String> = LinkedMultiValueMap()
            params.add("grant_type", "authorization_code")
            params.add("client_id", appconfig?.get("clientId"))
            params.add("client_secret", appconfig?.get("clientSecret"))
            params.add("code", code)

            val uriComponents = UriComponentsBuilder
                .fromHttpUrl(AppPropsUtils.findUrl("kauth") + "/oauth/token")
                .queryParams(params)
                .build(false)

            logger.info(" >>> [kakaoToken] request - url: {}", uriComponents.toString())
            var responseEntity = webClient.get()
                .uri(uriComponents.toUri())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError) { Mono.error(TokenInvalidException("kakao token invalid")) }
                .onStatus(HttpStatus::is5xxServerError) { Mono.error(ThirdPartyServerException("kauth internal server error")) }
                .toEntity(TokenInfo::class.java)
                .block()
            logger.info(" >>> [kakaoToken] response - statusCode: {}, body: {}", responseEntity?.statusCodeValue, responseEntity?.body)

            return TokenUtils.encodeToken(responseEntity?.body!!, type)
        }catch(e: Throwable){
            throw e
        }
    }

    /**
     * 네이버 토큰조회
     *
     * @param code [String]
     * @param type [String]
     * @return TokenInfo [TokenInfo]
     * @author yoonho
     * @since 2022.11.29
     */
    fun naverToken(code: String?, type: String): TokenInfo {
        try{
            var appconfig = AppPropsUtils.findClientInfoByType(type)

            val params: MultiValueMap<String, String> = LinkedMultiValueMap()
            params.add("grant_type", "authorization_code")
            params.add("client_id", appconfig?.get("clientId"))
            params.add("client_secret", appconfig?.get("clientSecret"))
            params.add("code", code)
            params.add("state", CommCode.Social.NAVER.code)

            val uriComponents = UriComponentsBuilder
                .fromHttpUrl(AppPropsUtils.findUrl("nauth") + "/oauth2.0/token")
                .queryParams(params)
                .build(false)

            logger.info(" >>> [naverToken] request - url: {}", uriComponents.toString())
            var responseEntity = webClient.get()
                .uri(uriComponents.toUri())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError) { Mono.error(TokenInvalidException("naver token invalid")) }
                .onStatus(HttpStatus::is5xxServerError) { Mono.error(ThirdPartyServerException("nauth internal server error")) }
                .toEntity(TokenInfo::class.java)
                .block()
            logger.info(" >>> [naverToken] response - statusCode: {}, body: {}", responseEntity?.statusCodeValue, responseEntity?.body)

            return TokenUtils.encodeToken(responseEntity?.body!!, type)
        }catch(e: Throwable){
            throw e
        }
    }
}