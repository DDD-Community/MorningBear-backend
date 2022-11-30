package com.ddd.morningbear.common.exception

import graphql.GraphQLException

class GraphQLNotFoundException : GraphQLException {
    constructor(msg: String?): super(msg)
    constructor(): super()
}