package com.wwb.commonbase.mq.impl.rocketmq;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wwb.commonbase.utils.enums.BaseEnumJsonDeserializer;
import com.wwb.commonbase.utils.enums.CustomEnumObjectMapper;

import java.nio.charset.StandardCharsets;

/**
 * 消息序列化模板,默认序列化时将类型写入
 *
 * @author xxx
 */
public class RocketMqTemplate<T> {

    Jackson2JsonMqSerializer<T> jackson2JsonMqSerializer;


    protected Jackson2JsonMqSerializer<T> getJackson2JsonMqSerializer() {

        if (jackson2JsonMqSerializer != null) {
            return jackson2JsonMqSerializer;
        }
        jackson2JsonMqSerializer = new Jackson2JsonMqSerializer<T>(clazz);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        om.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonMqSerializer.setObjectMapper(om);
        return jackson2JsonMqSerializer;
    }

    private Class clazz;

    public Class getClazz() {
        return clazz;
    }

    public RocketMqTemplate(Class clazz) {

        this.clazz = clazz;
    }

    public String toJsonString(Object obj) {
        return new String(getJackson2JsonMqSerializer().serialize(obj));
    }

    public byte[] toJsonBytes(Object obj) {
        return getJackson2JsonMqSerializer().serialize(obj);
    }

    public T toObject(String json) {
        return getJackson2JsonMqSerializer().deserialize(json.getBytes(StandardCharsets.UTF_8));
    }

    public T toObject(byte[] json) {

        String str = new String(json);
        return getJackson2JsonMqSerializer().deserialize(str.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * 序列化时，不写入类型
     */
    public static class RocketMqNormalTemplate<T> extends RocketMqTemplate<T> {
        Jackson2JsonMqSerializer<T> jackson2JsonRedisSerializer;

        private Class clazz;

        private ObjectMapper objectMapper;

        public RocketMqNormalTemplate(Class clazz, BaseEnumJsonDeserializer... deserializers) {
            super(clazz);
            this.clazz = clazz;
            if (jackson2JsonRedisSerializer == null) {
                jackson2JsonRedisSerializer = new Jackson2JsonMqSerializer<T>(clazz);

                if (deserializers != null && deserializers.length > 0) {
                    objectMapper = new ObjectMapper();
                    SimpleModule module = new SimpleModule();
                    for (BaseEnumJsonDeserializer deser : deserializers) {
                        module.addDeserializer(deser.getEnumClass(), deser);
                    }
                    objectMapper.registerModule(module);
                } else {
                    objectMapper = new CustomEnumObjectMapper(null);
                }
                jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
            }
        }


        @Override
        protected Jackson2JsonMqSerializer<T> getJackson2JsonMqSerializer() {
//            if(jackson2JsonRedisSerializer==null){
//                jackson2JsonRedisSerializer = new Jackson2JsonMqSerializer<T>(clazz);
//                objectMapper = SpringContextUtil.getBean(CustomEnumObjectMapper.class);
//                if(objectMapper==null){
//                    objectMapper=new CustomEnumObjectMapper();
//                }
//                jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//            }
            return jackson2JsonRedisSerializer;
        }
    }
}
