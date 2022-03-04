package com.wwb.commonbase.feign;

import feign.FeignException;
import feign.Request;

/**
 * @author weibo
 */
public class CustomFeignException extends FeignException {

    private int code;

    public int getCode() {
        return code;
    }

    public CustomFeignException(int code, String message, Request request){
        super(200,message,request);
        this.code=code;
    }
}
