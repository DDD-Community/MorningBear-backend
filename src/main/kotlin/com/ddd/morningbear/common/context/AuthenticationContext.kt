package com.ddd.morningbear.common.context

object AuthenticationContext {

    private var accountId: String = ""

    fun setAccountId(accountId: String) {
        this.accountId = accountId
    }

    fun getAccountId() = this.accountId
}