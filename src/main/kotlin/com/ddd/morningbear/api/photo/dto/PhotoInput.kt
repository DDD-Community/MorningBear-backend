package com.ddd.morningbear.api.photo.dto

/**
 * @author yoonho
 * @since 2022.12.06
 */
data class PhotoInput(
    var accountId: String?,
    var photoId: String?,
    var photoLink: String?,
    var photoDesc: String?,
    var categoryId: String?,
    var startAt: String?,
    var endAt: String?
)
