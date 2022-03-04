package com.wwb.commonbase.utils.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Api请求结果
 */
@ApiModel(value = "ResponseResult<T>", description = "请求响应数据结果")
@Data
public class ResponseResult<T> implements Serializable {


    /**
     * 响应数据
     */
    @Getter
    @Setter
    @ApiModelProperty("响应的数据")
    protected T data;

    /**
     * 错误码
     */
    @Getter
    @Setter
    @ApiModelProperty("错误码")
    protected int code;

    /**
     * 错误消息
     */
    @Getter
    @Setter
    @ApiModelProperty("提示消息")
    protected String msg = "";

    /**
     * 是否响应成功
     */
    @Getter
    @Setter
    @ApiModelProperty("响应是否成功")
    protected boolean success = true;

    protected ResponseResult(T data, boolean success, int status, String error) {

        this.success = success;
        this.code = status;
        this.msg = error;
        this.data = data;
    }

    public ResponseResult() {

    }

    @SuppressWarnings("rawtypes")
    public static ResponseResult success() {
        return new ResponseResult<Object>(null, true, BaseErrorCode.SUCCESS_CODE.getErrorCode(), "");
    }

    @SuppressWarnings("rawtypes")
    public static <D> ResponseResult success(D data) {
        return new ResponseResult<D>(data, true, BaseErrorCode.SUCCESS_CODE.getErrorCode(), "");
    }

    @SuppressWarnings("rawtypes")
    public static ResponseResult fail(int code, String msg) {

        return new ResponseResult<Object>(null, false, code, msg);
    }

    @SuppressWarnings("rawtypes")
    public static <D> ResponseResult fail(D data, int code, String msg) {
        return new ResponseResult<D>(data, false, code, msg);
    }

    @SuppressWarnings("rawtypes")
    public static ResponseResult fail(BaseErrorCode errorCode) {
        return new ResponseResult<Object>(null, false, errorCode.getErrorCode(), errorCode.getErrorMsg());
    }

}