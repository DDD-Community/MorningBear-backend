package com.ddd.morningbear.common.handler

import com.ddd.morningbear.common.dto.BaseResponse
import com.ddd.morningbear.common.exception.BadRequestException
import com.ddd.morningbear.common.exception.ThirdPartyServerException
import com.ddd.morningbear.common.exception.TokenInvalidException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * @author yoonho
 * @since 2022.11.29
 */
@RestControllerAdvice
class ExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(TokenInvalidException::class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    fun tokenInvalidException(e: TokenInvalidException): BaseResponse {
        return BaseResponse(e.message, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(ThirdPartyServerException::class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    fun kakaoServerException(e: ThirdPartyServerException): BaseResponse{
        return BaseResponse(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun badRequestException(e: BadRequestException): BaseResponse{
        return BaseResponse(e.message, HttpStatus.BAD_REQUEST)
    }
}