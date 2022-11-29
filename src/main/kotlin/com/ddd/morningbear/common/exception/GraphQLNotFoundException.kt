package com.ddd.morningbear.common.exception

import graphql.GraphQLException

class GraphQLNotFoundException(msg: String) : GraphQLException(msg)