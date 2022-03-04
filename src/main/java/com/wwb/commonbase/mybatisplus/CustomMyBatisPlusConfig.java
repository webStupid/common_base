package com.wwb.commonbase.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class CustomMyBatisPlusConfig
        implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {

        log.debug("初始化MybatisPlusInterceptor");
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MybatisPlusInterceptor.class);
        List<InnerInterceptor> interceptors = new ArrayList();

        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.H2);
        interceptors.add(paginationInnerInterceptor);

        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        interceptors.add(blockAttackInnerInterceptor);

        builder.addPropertyValue("interceptors", interceptors);
        registry.registerBeanDefinition("mybatisPlusInterceptor", builder.getBeanDefinition());

        builder = BeanDefinitionBuilder.genericBeanDefinition(CustomConfigurationCustomizer.class);
        registry.registerBeanDefinition("configurationCustomizer", builder.getBeanDefinition());
    }
}
