package com.jeeplus.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Filename:    InterfaceAnnotation.java   
 * Copyright:   Copyright (c)2015
 * Company:     creditease
 * @version:    1.0   
 * @since:  JDK 1.6.0_35  
 * Create at:   2015-4-16 13:18
 * Description:  自定义注解,用于接口中注解标识
 * @author    ygq
 */
@Retention(RetentionPolicy.RUNTIME)   
@Target({ElementType.TYPE})  
@Documented
public @interface InterfaceAnnotation {
	  /**
     * 系统标识
     * @return
     */
    String SIGNAGE() default "";//定义系统标识
}
