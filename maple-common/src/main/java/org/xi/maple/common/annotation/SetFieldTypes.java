package org.xi.maple.common.annotation;

import java.lang.annotation.*;

/**
 * @author xishihao
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SetFieldTypes {
    String[] types() default {};
}
