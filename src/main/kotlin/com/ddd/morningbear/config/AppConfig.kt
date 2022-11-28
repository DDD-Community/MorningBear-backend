package com.ddd.morningbear.config

import com.ddd.morningbear.common.constants.AppProps
import com.ddd.morningbear.common.utils.AppPropsUtils
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

/**
 * @author yoonho
 * @since 2022.11.29
 */
@Configuration
class AppConfig(
    private val appProps: AppProps
) {
    @PostConstruct
    fun init() {
        AppPropsUtils.setAppProps(appProps)
    }
}