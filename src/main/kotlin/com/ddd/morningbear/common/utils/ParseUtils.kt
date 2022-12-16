package com.ddd.morningbear.common.utils

import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import com.ddd.morningbear.common.exception.GraphQLTokenInvalidException
import com.ddd.morningbear.login.dto.TokenInfo
import org.apache.tomcat.util.codec.binary.Base64
import org.slf4j.LoggerFactory
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * @author yoonho
 * @since 2022.12.04
 */
object ParseUtils {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val ciphger: Cipher = Cipher.getInstance("AES")
    private val keySpec: SecretKeySpec = SecretKeySpec(AppPropsUtils.findSecretKey("cipher").toByteArray(), "AES")

    fun encode(type: String, data: String?): String {
        try{
            ciphger.init(Cipher.ENCRYPT_MODE, keySpec)
            var prefixedData = CommCode.findPrefix(type) + data
            return Base64.encodeBase64(ciphger.doFinal(prefixedData.toByteArray()))
                .toString(Charsets.UTF_8)
        }catch (e: Exception) {
            throw GraphQLBadRequestException("요청하신 정보를 암호화할 수 없습니다.")
        }
    }

    fun decode(data: String?): String {
        try{
            ciphger.init(Cipher.DECRYPT_MODE, keySpec)
            return ciphger.doFinal(Base64.decodeBase64(data?.toByteArray()))
                .toString(Charsets.UTF_8)
        }catch (e: Exception) {
            throw GraphQLBadRequestException("요청하신 정보를 복호화할 수 없습니다.")
        }
    }

    fun removePrefix(type: String, data: String): String {
        return data.replace(CommCode.findPrefix(type), "")
    }
}