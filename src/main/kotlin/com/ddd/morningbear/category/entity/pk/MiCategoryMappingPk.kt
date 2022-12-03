package com.ddd.morningbear.category.entity.pk

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class MiCategoryMappingPk(
    @Column(name = "ACCOUNT_ID")
    val accountId: String,
    @Column(name = "CATEGORY_ID")
    val categoryId: String,
): Serializable