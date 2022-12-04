package com.ddd.morningbear.like.entity.pk

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Embeddable
class FiLikeInfoPk (
    @Column(name = "LIKE_CODE")
    val likeCode: String,
    val takenAccountId: String,
    val givenAccountId: String,
): Serializable