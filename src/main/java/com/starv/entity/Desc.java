package com.starv.entity;

import java.lang.annotation.*;

/**
 * 自定义注解
 * Created by m1590 on 2018/8/24.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Desc {
    String value();
}
