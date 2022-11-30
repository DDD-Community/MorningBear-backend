package com.ddd.morningbear.common.exception

import graphql.GraphQLException

class GraphQLBadRequestException : GraphQLException {
    constructor(msg: String?) : super(msg)
    constructor(): super()
}