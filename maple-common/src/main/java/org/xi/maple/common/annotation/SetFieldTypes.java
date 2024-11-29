package org.xi.maple.common.annotation;

import org.xi.maple.common.constant.SetFieldType;

import java.lang.annotation.*;

/**
 * @author xishihao
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SetFieldTypes {
    SetFieldType[] types() default {};
}
