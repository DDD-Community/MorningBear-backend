package com.ddd.morningbear.api.myinfo

import com.ddd.morningbear.myinfo.MyInfoService
import com.ddd.morningbear.myinfo.dto.MpUserInfoDto
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MyInfoController(
    private val myInfoService: MyInfoService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @QueryMapping
    fun findMyInfo(): MpUserInfoDto {
        return myInfoService.findMyInfo("")
    }
}