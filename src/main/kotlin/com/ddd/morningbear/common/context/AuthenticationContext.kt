package com.ddd.morningbear.common.context

/**
 * @author yoonho
 * @since 2022.12.04
 */
object AuthenticationContext {

    private var accountId: String = ""

    fun setAccountId(accountId: String) {
        this.accountId = accountId
    }

    fun getAccountId() = this.accountId
}