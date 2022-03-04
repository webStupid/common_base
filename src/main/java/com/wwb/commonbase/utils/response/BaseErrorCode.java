package com.wwb.commonbase.utils.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseErrorCode {

    SUCCESS_CODE(0, "成功"),
    UNKNOWN_ERROR_CODE(-1, "未知错误"),

    SYSTEM_REQUEST_EXCEPTION(-2, "系统请求操作异常");


    private int errorCode;
    private String errorMsg;
}
