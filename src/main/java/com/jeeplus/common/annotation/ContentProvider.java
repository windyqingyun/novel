package com.jeeplus.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 内容提供商注解，用于controller拦截判断
 * 放在提供给内容提供商的接口方法或者类上
 * @author zhangsc
 * @version 2017年11月10日
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ContentProvider {

}
