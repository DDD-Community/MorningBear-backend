package com.ddd.morningbear.common.exception

import graphql.GraphQLException

/**
 * @author yoonho
 * @since 2022.12.04
 */
class GraphQLNotFoundException : GraphQLException {
    constructor(msg: String?): super(msg)
    constructor(): super()
}