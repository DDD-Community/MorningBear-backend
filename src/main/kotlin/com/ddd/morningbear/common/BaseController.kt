package com.ddd.morningbear.common

import com.ddd.morningbear.api.myinfo.dto.PhotoSizeInput
import com.ddd.morningbear.common.context.AuthenticationContextHolder
import java.util.*

/**
 * @author yoonho
 * @since 2022.12.04
 */
open class BaseController {
    fun getAuthenticationContextAccountId(): String = AuthenticationContextHolder.getAuthenticationContext().getAccountId()
    fun findPhotoSize(sizeInput: Optional<PhotoSizeInput>): PhotoSizeInput {
        var size = PhotoSizeInput()
        if(!sizeInput.isEmpty){
            size = sizeInput.get()
        }
        return size
    }
}