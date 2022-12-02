package com.ddd.morningbear.common.context

import org.springframework.core.NamedInheritableThreadLocal

object AuthenticationContextHolder {
    /** 인증 정보를 보관하는 ThreadLocal 변수.  */
    private val ALLIANCE_CONTEXT_HOLDER: NamedInheritableThreadLocal<AuthenticationContext> =
        NamedInheritableThreadLocal<AuthenticationContext>(
            AuthenticationContext::class.java
                .toString() + "_CONTEXT_HOLDER"
        )

    fun isExistAuthenticationContext(): Boolean {
        if(ALLIANCE_CONTEXT_HOLDER.get() == null){
            return false;
        }
        return true;
    }
    fun setAuthenticationContext(context: AuthenticationContext) = ALLIANCE_CONTEXT_HOLDER.set(context)
    fun getAuthenticationContext(): AuthenticationContext = ALLIANCE_CONTEXT_HOLDER.get()
    fun removeAuthenticationContext() = ALLIANCE_CONTEXT_HOLDER.remove()
}