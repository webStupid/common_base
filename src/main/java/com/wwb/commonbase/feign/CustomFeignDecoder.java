package com.wwb.commonbase.feign;

import com.wwb.commonbase.utils.response.ResponsePageResult;
import com.wwb.commonbase.utils.response.ResponseResult;
import feign.FeignException;
import feign.Response;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 自定义Feign解码器
 * @author xxx
 */
public class CustomFeignDecoder extends SpringDecoder {

    CustomFeignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        if (type instanceof ParameterizedTypeImpl) {
            Class<?> rawType = ((ParameterizedTypeImpl) type).getRawType();
            if (rawType.equals(ResponseResult.class) || rawType.equals(ResponsePageResult.class)) {
                return super.decode(response, type);
            }
        }

        ParameterizedTypeImpl parameterizedType = ParameterizedTypeImpl.make(ResponseResult.class, new Type[]{type}, null);
        ResponseResult<?> result = (ResponseResult<?>) super.decode(response, parameterizedType);
        if(result!=null && !result.isSuccess()){
            throw new CustomFeignException(result.getCode(), result.getMsg(),response.request());
        }
        return result.getData();
    }
}