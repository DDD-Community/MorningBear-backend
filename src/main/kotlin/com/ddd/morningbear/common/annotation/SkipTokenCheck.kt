package com.ddd.morningbear.common.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * @author yoonho
 * @since 2022.12.04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
annotation class SkipTokenCheck
