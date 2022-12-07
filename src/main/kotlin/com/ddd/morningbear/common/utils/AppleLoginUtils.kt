package com.ddd.morningbear.common.utils

import com.ddd.morningbear.common.exception.BadRequestException
import com.ddd.morningbear.common.exception.ThirdPartyServerException
import com.ddd.morningbear.login.dto.ApplePublicKey
import com.ddd.morningbear.login.dto.ApplePublicKeys
import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.shaded.gson.JsonObject
import com.nimbusds.jwt.SignedJWT
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object AppleLoginUtils {

    fun findIdentityTokenInfo(identityToken: String?): Map<String, String> {
        var headerIdentityToken = identityToken?.substring(0, identityToken.indexOf("."))
        var header = ObjectMapper().readValue(String(Base64.getDecoder().decode(headerIdentityToken), Charsets.UTF_8), Map::class.java)
        return mapOf(
            "kid" to header["kid"].toString(),
            "alg" to header["alg"].toString()
        )
    }

    fun findMatchedKey(keys: ApplePublicKeys, idToken: String): ApplePublicKey? {
        var identityTokenMap = this.findIdentityTokenInfo(idToken)
        var key = keys.keys.stream().filter { x -> x.kid.equals(identityTokenMap["kid"]) && x.alg.equals(identityTokenMap["alg"]) }.findFirst().orElseGet(null)
        if(key != null){
            return key
        }
        return null
    }

    fun decodeAppleToken(token: String?): JsonObject? {
        var jwt = SignedJWT.parse(token)
        var claimsSet = jwt.jwtClaimsSet

        var payload = ObjectMapper().readValue(claimsSet.toJSONObject().toString(), JsonObject::class.java)

        if(!payload.isJsonNull){
            return payload
        }
        return null
    }




    /**
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */


//    fun getClientSecret(key: ApplePublicKey?, clientId: String?): String {
//        var expiredDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant())
//
//        return Jwts.builder()
//            .setHeaderParams(mapOf(
//                "kid" to key?.kid,
//                "alg" to key?.alg
//            ))
//            .setIssuer("morning-bear")
//            .setIssuedAt(Date(System.currentTimeMillis()))
//            .setExpiration(expiredDate)
//            .setAudience("https://appleid.apple.com")
//            .setSubject(clientId)
////            .signWith(SignatureAlgorithm.ES256, this.getPrivateKey())
//            .signWith(SignatureAlgorithm.HS256, testKey)
//            .compact()
//    }
//
//    fun getPrivateKey(): PrivateKey {
//        var resource = ClassPathResource("p8파일 경로")
//
//        var privateKey = String(Files.readAllBytes(Paths.get(resource.uri)))
//        privateKey.replace("-----BEGIN PRIVATE KEY-----\n", "")
//        privateKey.replace("-----END PRIVATE KEY-----", "")
//        var encoded = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(privateKey)
//
//        var keyFactory = KeyFactory.getInstance("RSA")
//        var keySpec = PKCS8EncodedKeySpec(encoded)
//
//        return keyFactory.generatePrivate(keySpec)
//    }


    // v3.0
//    fun getClientSecret(key: ApplePublicKey?, clientId: String?): String {
//        var expiredDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant())
//
//        var header = JWSHeader.Builder(JWSAlgorithm.ES256).keyID(key?.kid).build()
//        var now = Date()
//        var claimsSet = JWTClaimsSet.Builder()
//                            .issuer("morning-bear")
//                            .issueTime(now)
//                            .expirationTime(Date(now.time * 36000000))
//                            .audience("https://appleid.apple.com")
//                            .subject(clientId)
//                            .build()
//
//        var jwt = SignedJWT(header, claimsSet)
//
//        try{
//
//            var ecPrivateKey = ECPrivateKeyImpl(readPrivateKey())
//            var jwsSigner = ECDSASigner(ecPrivateKey.s)
//
//            jwt.sign(jwsSigner)
//        }catch(e: Exception) {
//
//        }
//
//        return jwt.serialize()
//    }
//
//    fun readPrivateKey(): ByteArray {
//        var resource = ClassPathResource("")
//
//        try(var reader = FileReader(resource.uri.path)) {
//
//        }
//    }

    // v1.0
//    fun getClientSecret(key: ApplePublicKey?, clientId: String?): String {
//        var expiredDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant())
//
//        return Jwts.builder()
//            .setHeaderParams(mapOf(
//                "kid" to key?.kid,
//                "alg" to key?.alg
//            ))
//            .setIssuer("")
//            .setIssuedAt(Date(System.currentTimeMillis()))
//            .setExpiration(expiredDate)
//            .setAudience("https://appleid.apple.com")
//            .setSubject(clientId)
//            .signWith(SignatureAlgorithm.ES256, this.getPrivateKey())
//            .compact()
//    }
//
//    fun getPrivateKey(): PrivateKey {
//
//    }
}