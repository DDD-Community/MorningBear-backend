package com.ddd.morningbear.badge.entity.pk

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * @author yoonho
 * @since 2022.11.19
 */
@Embeddable
class MiBadgeMappingPk(
    val accountId: String,
    @Column(name = "BADGE_ID")
    val badgeId: String,
): Serializable