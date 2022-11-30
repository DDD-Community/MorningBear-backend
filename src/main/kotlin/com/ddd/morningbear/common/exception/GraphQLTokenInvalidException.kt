package com.ddd.morningbear.common.exception

import graphql.GraphQLException

class GraphQLTokenInvalidException: GraphQLException {
    constructor(msg: String?): super(msg)
    constructor(): super()
}