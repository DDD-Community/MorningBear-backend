package com.ddd.morningbear.common.constants

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * @author yoonho
 * @since 2022.11.29
 */
@Component
@ConfigurationProperties(prefix = "login")
data class AppProps(
    val keys: Map<String, String>,
    val apis: Map<String, String>,
    val appconfigs: List<Map<String, String>>
)
