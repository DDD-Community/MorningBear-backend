package com.ddd.morningbear.common.exception

import graphql.GraphQLException

/**
 * @author yoonho
 * @since 2022.12.02
 */
class GraphQLInternalServerException : GraphQLException {
    constructor(message: String?) : super(message)
    constructor(): super()
}