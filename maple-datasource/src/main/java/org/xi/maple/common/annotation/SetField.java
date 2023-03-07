package org.xi.maple.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SetField {
    /**
     * 要设置的字段
     */
    String field();

    /**
     * 设置的条件
     */
    String[] types() default {};
}
