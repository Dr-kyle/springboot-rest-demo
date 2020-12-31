package com.kyle.demo.annotation;

import java.lang.annotation.*;

/**
 * @author kz37
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JwtIgnore {
}
