package com.ddd.morningbear.common.exception

import graphql.GraphQLException

/**
 * @author yoonho
 * @since 2022.12.04
 */
class GraphQLBadRequestException : GraphQLException {
    constructor(msg: String?) : super(msg)
    constructor(): super()
}