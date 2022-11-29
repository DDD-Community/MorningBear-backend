package com.ddd.morningbear.common.exception

import graphql.GraphQLException

class GraphQLBadRequestException(msg: String) : GraphQLException(msg)