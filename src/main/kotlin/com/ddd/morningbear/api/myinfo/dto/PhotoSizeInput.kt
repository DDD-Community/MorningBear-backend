package com.ddd.morningbear.api.myinfo.dto

import com.ddd.morningbear.common.constants.CommCode
import java.io.Serializable

/**
 * @author yoonho
 * @since 2022.12.14
 */
data class PhotoSizeInput(
    var totalSize: Int = CommCode.photoSize,
    var categorySize: Int = CommCode.photoSize,
): Serializable
