package com.ddd.morningbear.login

import antlr.Token
import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.exception.BadRequestException
import com.ddd.morningbear.common.exception.ThirdPartyServerException
import com.ddd.morningbear.common.exception.TokenInvalidException
import com.ddd.morningbear.common.utils.AppPropsUtils
import com.ddd.morningbear.common.utils.AppleLoginUtils
import com.ddd.morningbear.common.utils.ParseUtils
import com.ddd.morningbear.login.dto.ApplePublicKey
import com.ddd.morningbear.login.dto.ApplePublicKeys
import com.ddd.morningbear.login.dto.TokenInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.ECDSASigner
import com.nimbusds.jose.shaded.gson.JsonObject
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.io.StringReader
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.annotation.PostConstruct
import kotlin.collections.HashMap


/**
 * @author yoonho
 * @since 2022.11.29
 */
@Service
class AppleService (
    private val webClientBuilder: WebClient.Builder
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private lateinit var webClient: WebClient

    var testKey = "thisistestusersecretkeyprojectnameismologaaaaaaaaaaaaaaaa"

    @PostConstruct
    fun initWebClient() {
        this.webClient = webClientBuilder.build()
    }

    fun findAppleKeys(): ApplePublicKeys {
        val uriComponents = UriComponentsBuilder
            .fromHttpUrl(AppPropsUtils.findUrl("apple") + "/auth/keys")
            .build(false)

        logger.info(" >>> [appleKey] request - url: {}", uriComponents.toString())
        var responseEntity = webClient.get()
            .uri(uriComponents.toUri())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError) { Mono.error(BadRequestException()) }
            .onStatus(HttpStatus::is5xxServerError) { Mono.error(ThirdPartyServerException("apple internal server error")) }
            .toEntity(ApplePublicKeys::class.java)
            .block()
        logger.info(" >>> [appleKey] response - statusCode: {}, body: {}", responseEntity?.statusCodeValue, responseEntity?.body)

        return responseEntity?.body!!
    }


    fun appleToken(idToken: String): String {
        var keys = this.findAppleKeys()
        var key = AppleLoginUtils.findMatchedKey(keys, idToken)

        var expiredDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant())

        return Jwts.builder()
            .setHeaderParams(mapOf(
                "kid" to key?.kid,
                "alg" to key?.alg
            ))
            .setIssuer("morning-bear")
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(expiredDate)
            .setAudience("https://appleid.apple.com")
            .setSubject("auth")
            .signWith(SignatureAlgorithm.HS256, testKey)
            .compact()
    }


//    fun appleToken(code: String?, identityToken: String?, type: String): TokenInfo {
//        try{
//            var appconfig = AppPropsUtils.findClientInfoByType(type)
//            var clientId = appconfig?.get("clientId")
//
//            var keys = this.findAppleKeys()
////            var key = this.findMatchedKey(keys, this.findIdentityTokenInfo(identityToken))
//            var key = keys.keys[0]
//            var t = AppleLoginUtils.getClientSecret(key, clientId)
//
//            var c = Jwts.parser().setSigningKey(testKey).parseClaimsJws(t)
//
//            var s = String(Base64.getDecoder().decode(t), Charsets.UTF_8)
////            var header = ObjectMapper().readValue(String(Base64.getDecoder().decode(t), Charsets.UTF_8), Map::class.java)
//
//            val params: MultiValueMap<String, String> = LinkedMultiValueMap()
//            params.add("grant_type", "authorization_code")
//            params.add("client_id", clientId)
////            params.add("client_secret", this.getClientSecret(key, clientId))
//            params.add("code", code)
//
//            val uriComponents = UriComponentsBuilder
//                .fromHttpUrl(AppPropsUtils.findUrl("apple") + "/auth/token")
//                .build(false)
//
//            logger.info(" >>> [appleToken] request - url: {}", uriComponents.toString())
//            var responseEntity = webClient.post()
//                .uri(uriComponents.toUri())
//                .body(BodyInserters.fromValue(params.toString()))
////                .bodyValue(params)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .onStatus(HttpStatus::is4xxClientError) { Mono.error(TokenInvalidException("apple token invalid")) }
//                .onStatus(HttpStatus::is5xxServerError) { Mono.error(ThirdPartyServerException("apple internal server error")) }
//                .toEntity(TokenInfo::class.java)
//                .block()
//            logger.info(" >>> [appleToken] response - statusCode: {}, body: {}", responseEntity?.statusCodeValue, responseEntity?.body)
//
//            var userNo = AppleLoginUtils.decodeAppleToken(responseEntity?.body!!.idToken)
//
//            var tokenInfo = responseEntity?.body!!
//            tokenInfo.accessToken = ParseUtils.encode(type, tokenInfo.accessToken)
//            tokenInfo.refreshToken = ParseUtils.encode(type, tokenInfo.refreshToken)
//
//            return tokenInfo
//        }catch (e: Throwable){
//            throw e
//        }
//    }


}