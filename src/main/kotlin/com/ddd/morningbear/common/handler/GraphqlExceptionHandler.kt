package com.ddd.morningbear.common.handler

import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.exception.*
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Component
class GraphqlExceptionHandler: DataFetcherExceptionResolverAdapter() {

    override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        // Not Found
        if(ex is GraphQLNotFoundException){
            return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.NOT_FOUND)
                .extensions(mapOf(
                    "code" to CommCode.Result.K002.code,
                    "message" to if(ex.message.isNullOrBlank()) CommCode.Result.K002.message else ex.message
                ))
                .message("")
                .build()
        }

        // Not Authorized
        if(ex is GraphQLTokenInvalidException){
            return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.UNAUTHORIZED)
                .extensions(mapOf(
                    "code" to CommCode.Result.K001.code,
                    "message" to if(ex.message.isNullOrBlank()) CommCode.Result.K001.message else ex.message
                ))
                .message("")
                .build()
        }

        // Third-party Server Internal Error
        if(ex is GraphQLThirdPartyServerException){
            return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.INTERNAL_ERROR)
                .extensions(mapOf(
                    "code" to CommCode.Result.K005.code,
                    "message" to if(ex.message.isNullOrBlank()) CommCode.Result.K005.message else ex.message
                ))
                .message("")
                .build()
        }

        // Internal Server Internal Error
        if(ex is GraphQLInternalServerException){
            return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.INTERNAL_ERROR)
                .extensions(mapOf(
                    "code" to CommCode.Result.K005.code,
                    "message" to if(ex.message.isNullOrBlank()) CommCode.Result.K005.message else ex.message
                ))
                .message("")
                .build()
        }

        // Bad Request
        if(ex is GraphQLBadRequestException){
            return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .extensions(mapOf(
                    "code" to CommCode.Result.K000.code,
                    "message" to if(ex.message.isNullOrBlank()) CommCode.Result.K000.message else ex.message
                ))
                .message("")
                .build()
        }

        return null
    }
}