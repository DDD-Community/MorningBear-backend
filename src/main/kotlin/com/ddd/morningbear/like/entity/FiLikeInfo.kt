package com.ddd.morningbear.like.entity

import com.ddd.morningbear.like.dto.FiLikeInfoDto
import com.ddd.morningbear.like.entity.pk.FiLikeInfoPk
import com.ddd.morningbear.myinfo.entity.MpUserInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.*

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Entity
@Table(name = "FI_LIKE_INFO")
class FiLikeInfo(

    @EmbeddedId
    val fiLikeInfoPk: FiLikeInfoPk,

    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "TAKEN_ACCOUNT_ID", insertable = false, updatable = false)
    @MapsId("takenAccountId")
    val takenInfo: MpUserInfo,

    @ManyToOne
    @JoinColumn(name = "GIVEN_ACCOUNT_ID", insertable = false, updatable = false)
    @MapsId("givenAccountId")
    val givenInfo: MpUserInfo,
) {
    fun toDto() = FiLikeInfoDto(
        likeCode = this.fiLikeInfoPk.likeCode,
        givenAccountId = this.fiLikeInfoPk.givenAccountId,
        takenAccountId = this.fiLikeInfoPk.takenAccountId,
        createdAt = this.createdAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
    )
}