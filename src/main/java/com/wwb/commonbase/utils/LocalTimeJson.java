package com.wwb.commonbase.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;

/**
 * @author weibo
 */
public class LocalTimeJson {

    public static class LocalTimeJsonDeserializer extends JsonDeserializer<LocalTime> {

        @Override
        public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            try {
                LocalTime.parse(jsonParser.getValueAsString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class LocalTimeJsonSerializer extends JsonSerializer<LocalTime> {

        @Override
        public void serialize(LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(localTime.getHour() + ":" + localTime.getMinute() + ":" + localTime.getSecond());
        }
    }
}
