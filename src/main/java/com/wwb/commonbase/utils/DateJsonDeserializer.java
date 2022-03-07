package com.wwb.commonbase.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author xxx
 */
@Slf4j
public class DateJsonDeserializer extends JsonDeserializer<Date> {

    private String dateFormat;

    public DateJsonDeserializer() {
        this.dateFormat = "yyyy-MM-dd HH:mm:ss";
    }

    public DateJsonDeserializer(String dateFormat) {
        this.dateFormat = dateFormat;

        simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        simpleDateFormat3.setTimeZone(TimeZone.getTimeZone("GMT+08"));
    }

    private SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    private SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String value = jsonParser.getValueAsString();
        if (StringUtils.isBlank(value)) {
            return null;
        }
        Date date = parse(simpleDateFormat1, value);
        if (date == null) {
            date = parse(simpleDateFormat2, value);
        }
        if (date == null) {
            date = parse(simpleDateFormat3, value);
        }
        if (date == null) {
            try {
                date = new Date(Long.parseLong(value));
            } catch (Exception ex1) {
                ex1.printStackTrace();
            }
        }
        return date;
    }

    private Date parse(SimpleDateFormat sdf, String value) {
        try {
            return sdf.parse(value);
        } catch (Exception e) {
            return null;
        }
    }
}
