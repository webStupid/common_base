package com.wwb.commonbase.utils.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.wwb.commonbase.utils.EnumUtils;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 枚举类型JSON反序列化
 * @author xxx
 */
@JacksonStdImpl
public class BaseEnumJsonDeserializer<T extends Enum<T>> extends JsonDeserializer<T> {

    private Class<T> enumClass;

    public Class<T> getEnumClass() {
        return enumClass;
    }

    public BaseEnumJsonDeserializer(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Class<?> handledType() {
        return enumClass;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        Class<T> clazz = getEnumClass();
        T[] es = clazz.getEnumConstants();
        JsonNode node = jp.getCodec().readTree(jp);

        if (node.isTextual()) {
            String enumType = node.asText();
            for (T e : es) {
                if (e.name().equals(enumType)) {
                    return e;
                }
            }
            return null;
        }

        Integer value = null;
        if (node.isInt()) {
            value = node.asInt();
        }
        if (node.isObject()) {
            value = node.findValue("value").asInt();
        }
        if (value == null) {
            throw new IOException(node.asText() + " 反序列化为" + clazz.getName() + "失败");
        }
        try {
            Method method = clazz.getMethod("getValue");
            return EnumUtils.valueOf(clazz, value, method);
        } catch (NoSuchMethodException err) {
            return (T) es[value];
        }

    }


}