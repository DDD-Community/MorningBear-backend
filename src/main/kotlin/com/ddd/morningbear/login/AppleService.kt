package com.ddd.morningbear.login

import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.exception.BadRequestException
import com.ddd.morningbear.common.exception.ThirdPartyServerException
import com.ddd.morningbear.common.utils.AppPropsUtils
import com.ddd.morningbear.common.utils.AppleLoginUtils
import com.ddd.morningbear.common.utils.ParseUtils
import com.ddd.morningbear.login.dto.ApplePublicKeys
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.math.BigInteger
import java.security.KeyFactory
import java.security.spec.RSAPublicKeySpec
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.annotation.PostConstruct


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

    @PostConstruct
    fun initWebClient() {
        this.webClient = webClientBuilder.build()
    }

    fun findAppleKeys(): ApplePublicKeys {
        val uriComponents = UriComponentsBuilder
            .fromHttpUrl(AppPropsUtils.findUrl("apple") + "/auth/keys")
            .build(false)

        return webClient.get()
            .uri(uriComponents.toUri())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError) { Mono.error(BadRequestException()) }
            .onStatus(HttpStatus::is5xxServerError) { Mono.error(ThirdPartyServerException("apple internal server error")) }
            .toEntity(ApplePublicKeys::class.java)
            .block()
            ?.body!!
    }

    fun appleToken(idToken: String): String {
        val keys = this.findAppleKeys()
        val key = AppleLoginUtils.findMatchedKey(keys, idToken)
        val expiredDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant())

        var authId = Jwts.parser()
            .setSigningKey(
                KeyFactory.getInstance("RSA")
                    .generatePublic(
                        RSAPublicKeySpec(
                            BigInteger(1, Base64.getUrlDecoder().decode(key?.n)),
                            BigInteger(1, Base64.getUrlDecoder().decode(key?.e))
                        )
                    )
            )
            .parseClaimsJws(idToken).body.subject

        val token = Jwts.builder()
            .setHeaderParams(mapOf(
                "kid" to key?.kid,
                "alg" to key?.alg
            ))
            .setIssuer("morning-bear")
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(expiredDate)
            .setAudience("https://appleid.apple.com")
            .setSubject("auth")
            .setClaims(mapOf("accountId" to authId))
            .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(AppPropsUtils.findSecretKey("jwt").toByteArray()))
            .compact()

        return ParseUtils.encode(CommCode.Social.APPLE.code, token)
    }
}