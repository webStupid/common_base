package com.wwb.commonbase.mq.impl.rocketmq;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * JSON序列化执行器
 *
 * @author xxx
 */
@Slf4j
public class Jackson2JsonMqSerializer<T> {

    static final byte[] EMPTY_ARRAY = new byte[0];
    public static final Charset DEFAULT_CHARSET;
    private final JavaType javaType;
    private ObjectMapper objectMapper = new ObjectMapper();

    public Jackson2JsonMqSerializer(Class<T> type) {
        this.javaType = this.getJavaType(type);
    }

    public Jackson2JsonMqSerializer(JavaType javaType) {
        this.javaType = javaType;
    }

    static boolean isEmpty(@Nullable byte[] data) {
        return data == null || data.length == 0;
    }

    public T deserialize(@Nullable byte[] bytes) throws SerializationException {
        if (isEmpty(bytes)) {
            return null;
        } else {
            try {
                return this.objectMapper.readValue(bytes, 0, bytes.length, this.javaType);
            } catch (Exception var3) {
                throw new SerializationException("Could not read JSON: " + var3.getMessage(), var3);
            }
        }
    }

    public byte[] serialize(@Nullable Object t) throws SerializationException {
        if (t == null) {
            return EMPTY_ARRAY;
        } else {
            try {

                return this.objectMapper.writeValueAsBytes(t);
            } catch (Exception var3) {
                throw new SerializationException("Could not write JSON: " + var3.getMessage(), var3);
            }
        }
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "'objectMapper' must not be null");
        this.objectMapper = objectMapper;
    }

    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }

    static {
        DEFAULT_CHARSET = StandardCharsets.UTF_8;
    }
}
