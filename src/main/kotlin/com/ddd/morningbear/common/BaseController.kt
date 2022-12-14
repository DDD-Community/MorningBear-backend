package com.ddd.morningbear.common

import com.ddd.morningbear.api.myinfo.dto.PhotoSizeInput
import com.ddd.morningbear.common.context.AuthenticationContextHolder
import java.util.*

/**
 * @author yoonho
 * @since 2022.12.04
 */
open class BaseController {

    /**
     * Context에서 accountId 조회
     *
     * @return accountId [String]
     * @author yoonho
     * @since 2022.12.14
     */
    fun getAuthenticationContextAccountId(): String = AuthenticationContextHolder.getAuthenticationContext().getAccountId()

    /**
     * PhotoSizeInput이 null일 경우 default value 응답
     *
     * @param sizeInput [PhotoSizeInput]
     * @return size [PhotoSizeInput]
     * @author yoonho
     * @since 2022.12.14
     */
    fun findPhotoSize(sizeInput: Optional<PhotoSizeInput>): PhotoSizeInput {
        var size = PhotoSizeInput()
        if(!sizeInput.isEmpty){
            size = sizeInput.get()
        }
        return size
    }

    /**
     * size가 null일 경우 default value 응답
     *
     * @param sizeInput [Int]
     * @return size [Int]
     * @author yoonho
     * @since 2022.12.14
     */
    fun findSize(sizeInput: Optional<Int>): Int {
        var size = 1
        if(!sizeInput.isEmpty) {
            size = sizeInput.get()
        }
        return size
    }
}