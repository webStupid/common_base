package com.wwb.commonbase.mybatisplus;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否启用MybatisPlus自定义配置
 *
 * @author xxx
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomMyBatisPlusConfig.class)
public @interface EnableCustomMyBatisPlusConfig {

}
