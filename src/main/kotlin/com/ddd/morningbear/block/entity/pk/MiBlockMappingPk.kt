package com.ddd.morningbear.block.entity.pk

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Embeddable
class MiBlockMappingPk(
    @Column(name = "ACCOUNT_ID")
    val accountId: String,
    @Column(name = "B_ACCOUNT_ID")
    val blockAccountId: String,
): Serializable
