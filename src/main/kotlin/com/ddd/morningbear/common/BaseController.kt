package com.ddd.morningbear.common

import com.ddd.morningbear.common.context.AuthenticationContextHolder

/**
 * @author yoonho
 * @since 2022.12.04
 */
open class BaseController {
    fun getAuthenticationContextAccountId(): String = AuthenticationContextHolder.getAuthenticationContext().getAccountId()
}