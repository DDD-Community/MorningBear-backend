package com.ddd.morningbear.common

import com.ddd.morningbear.common.context.AuthenticationContextHolder

open class BaseController {
    fun getAuthenticationContextAccountId(): String = AuthenticationContextHolder.getAuthenticationContext().getAccountId()
}