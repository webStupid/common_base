package com.wwb.commonbase.annotation;

import com.wwb.commonbase.utils.web.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 全局异常捕获
 *
 * @author xxx
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({GlobalExceptionHandler.class})
public @interface EnableGlobalException {

}
