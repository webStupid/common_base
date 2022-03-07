package com.wwb.commonbase.annotation;


import com.wwb.commonbase.utils.SpringContextUtil;
import com.wwb.commonbase.utils.enums.EnumObjectMapperWrapper;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用枚举自定义反序列化
 *
 * @author xxx
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({EnumObjectMapperWrapper.class, SpringContextUtil.class})
public @interface EnableEnumMapper {
    /**
     * 自定义扫描枚举的包
     */
    String[] basePackages() default {};
}
