package com.ddd.morningbear.common.exception

import graphql.GraphQLException

/**
 * @author yoonho
 * @since 2022.11.29
 */
class GraphQLThirdPartyServerException(msg: String) : GraphQLException(msg)