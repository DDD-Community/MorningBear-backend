package com.ddd.morningbear.badge.entity.pk

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
class MiBadgeMappingPk(
    @Column(name = "ACCOUNT_ID")
    val accountId: String,
    @Column(name = "BADGE_ID")
    val badgeId: String,
): Serializable