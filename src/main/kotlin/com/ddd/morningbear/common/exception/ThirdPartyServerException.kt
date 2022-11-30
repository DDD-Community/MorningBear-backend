package com.ddd.morningbear.common.exception

/**
 * @author yoonho
 * @since 2022.11.29
 */
class ThirdPartyServerException : RuntimeException {
    constructor(msg: String?): super(msg)
    constructor(): super()
}