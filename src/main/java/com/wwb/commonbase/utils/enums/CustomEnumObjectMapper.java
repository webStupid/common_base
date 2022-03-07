package com.wwb.commonbase.utils.enums;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wwb.commonbase.utils.ClassUtils;
import com.wwb.commonbase.utils.DateJsonDeserializer;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * 自定义ObjectMapper,实现枚举类型反序列化的多样性兼容
 *
 * @author xxx
 */
@Primary
@SuppressWarnings("unchecked")
public class CustomEnumObjectMapper extends ObjectMapper {

    private static final String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public CustomEnumObjectMapper(List<String> packages) {
        super();
        initMapper(packages);
        initJavaTimeModule();

    }

    private void initJavaTimeModule() {
        this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATA_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        javaTimeModule.addSerializer(Date.class, new DateSerializer(false, simpleDateFormat));

        javaTimeModule.addDeserializer(Date.class, new DateJsonDeserializer(DATA_FORMAT));
        this.registerModule(javaTimeModule);
    }

    private void initMapper(List<String> packages) {
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<BaseEnumJsonDeserializer> deserializers = initEnumJsonDeserializer(packages);
        if (!CollectionUtils.isEmpty(deserializers)) {
            SimpleModule module = new SimpleModule();
            for (BaseEnumJsonDeserializer deser : deserializers
            ) {
                module.addDeserializer(deser.getEnumClass(), deser);
            }
            this.registerModule(module);
        }

    }

    private List<BaseEnumJsonDeserializer> initEnumJsonDeserializer(List<String> packages) {

        if (CollectionUtils.isEmpty(packages)) {
            return null;
        }
        List<Class<?>> classList = new ArrayList<>();
        for (String packageItem : packages) {
            List<Class<?>> list = ClassUtils.getClasssFromPackage(packageItem);
            if (!CollectionUtils.isEmpty(list)) {
                classList.addAll(list);
            }
        }
        if (CollectionUtils.isEmpty(classList)) {
            return null;
        }
        return classList.stream().distinct().filter(c -> c.isEnum())
                .map(c -> new BaseEnumJsonDeserializer(c))
                .collect(Collectors.toList())
                ;

    }
}
