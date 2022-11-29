package com.ddd.morningbear.common.exception

import graphql.GraphQLException

class GraphQLTokenInvalidException(msg: String) : GraphQLException(msg)