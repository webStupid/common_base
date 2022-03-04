package com.wwb.commonbase.feign;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否启用Feign响应结果Decoder
 * @author xxx
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomFeignDecoder.class)
public @interface EnableCustomFeignDecoder {
}
