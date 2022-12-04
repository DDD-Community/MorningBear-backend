package com.ddd.morningbear.category.entity.pk

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Embeddable
class MiCategoryMappingPk(
    val accountId: String,
    @Column(name = "CATEGORY_ID")
    val categoryId: String,
): Serializable