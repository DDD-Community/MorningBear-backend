package com.ddd.morningbear.common.utils

import com.ddd.morningbear.common.constants.AppProps
import org.slf4j.LoggerFactory

/**
 * @author yoonho
 * @since 2022.11.29
 */
object AppPropsUtils {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private lateinit var appProps: AppProps

    fun setAppProps(appProps: AppProps) {
        this.appProps = appProps
    }

    fun findJsKeyByType(type: String): String? {
        var appconfig = appProps.appconfigs.filter { x -> x["type"].equals(type) }.firstOrNull()
        if(!appconfig.isNullOrEmpty()){
            return appconfig.get("jsKey")
        }
        return null
    }

    fun findUrl(key: String): String? {
        return appProps.apis.get(key)
    }

    fun findClientInfoByType(type: String): Map<String, String?>? {
        var appconfig = appProps.appconfigs.filter { x -> x["type"].equals(type) }.firstOrNull()
        if(!appconfig.isNullOrEmpty()){
            return mapOf(
                "clientId" to appconfig.get("restKey"),
                "clientSecret" to appconfig.get("secrets"),
            )
        }
        return null
    }
}