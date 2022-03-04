package com.wwb.commonbase.feign;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否启用自定义Feign拦截器
 *
 * @author weibo
 * */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomFeignRequestInterceptor.class)
public @interface EnableCustomFeignRequestInterceptor {

}
