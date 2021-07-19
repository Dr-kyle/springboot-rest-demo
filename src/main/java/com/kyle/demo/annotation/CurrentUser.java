package com.kyle.demo.annotation;

import java.lang.annotation.*;

/**
 * @author kz37
 * 可以 添加到类上或者方法上或者 参数 getTest3(@CurrentUser UserInfo userInfo) {
 */
@Target({ElementType.TYPE, ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
}
