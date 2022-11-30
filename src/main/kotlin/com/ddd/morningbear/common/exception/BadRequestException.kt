package com.ddd.morningbear.common.exception

/**
 * @author yoonho
 * @since 2022.11.29
 */
class BadRequestException : RuntimeException {
    constructor(msg: String?): super(msg)
    constructor(): super()
}