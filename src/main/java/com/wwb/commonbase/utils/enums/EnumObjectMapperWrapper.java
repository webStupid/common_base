package com.wwb.commonbase.utils.enums;

import com.wwb.commonbase.annotation.EnableEnumMapper;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Primary;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weibo
 */
@Primary
public class EnumObjectMapperWrapper implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {

        Map<String, Object> defaultAttrs = importingClassMetadata.getAnnotationAttributes(EnableEnumMapper.class.getName(), true);
        List<String> packages = new ArrayList<>();
        if (defaultAttrs != null && defaultAttrs.containsKey("basePackages")) {
            String[] backPackages = (String[]) defaultAttrs.get("basePackages");
            if (backPackages != null) {
                for (String item : backPackages) {
                    packages.add(item);
                }
            }
        }
        packages.add("com.wwb");
        packages = packages.stream().distinct().collect(Collectors.toList());

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CustomEnumObjectMapper.class);
        builder.addConstructorArgValue(packages);
        registry.registerBeanDefinition(CustomEnumObjectMapper.class.getSimpleName(), builder.getBeanDefinition());
    }

}
