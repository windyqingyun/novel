package com.jeeplus.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Filename:    ActionAnnotation.java   
 * Copyright:   Copyright (c)2014
 * Company:     creditease
 * @version:    1.0   
 * @since:  JDK 1.6.0_35  
 * Create at:   2014-5-15 下午02:23:09   
 * Description:  自定义注解,用于Action中注解标识
 *   
 * Modification History:   
 * Date    Author      Version     Description   
 * ----------------------------------------------------------------- 
 * 2014-5-15 haibao      1.0     1.0 Version
 */
@Retention(RetentionPolicy.RUNTIME)   
@Target({ElementType.FIELD,ElementType.METHOD})  
@Documented
public @interface ActionAnnotation {

    /**
     * 模块名称
     * @return
     */
    String moduleName() default "";
    /**
     * 菜单名称
     * @return
     */
    String menuName() default "";
    /**
     * 表名称
     * @return
     */
    String tableName() default "";
    /**
     * 列名称
     * @return
     */
    String columnName() default "";
    /**
     * 描述
     * @return
     */
    String description() default "";
    /***
     * 参数名称
     * @return
     */
    String paramName() default "";
}
