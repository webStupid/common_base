package com.wwb.commonbase.utils.exception;

import com.wwb.commonbase.utils.TwoTuple;
import com.wwb.commonbase.utils.response.BaseErrorCode;
import com.wwb.commonbase.utils.response.ResponseResult;

/**
 * 自定义异常
 * @author weibo
 */
public class CustomException extends Exception {

    private boolean hasParsed = false;
    private int code;

    private String msg;

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(Exception e) {
        super(e);
    }

    public CustomException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.hasParsed = true;
    }

    public ResponseResult<?> toResult() {
        if (!hasParsed) {
            TwoTuple<Integer, String> tuple = parseError(this.getMessage());
            if (tuple != null) {
                code = tuple.first;
                msg = tuple.second;
            } else {
                code = BaseErrorCode.UNKNOWN_ERROR_CODE.getErrorCode();
            }
        }
        if (msg == null || msg.isEmpty()) {
            msg = getMessage();
        }
        return ResponseResult.fail(code, msg);
    }

    private static TwoTuple<Integer, String> parseError(String message) {
        if (message == null || message.isEmpty()) {
            return null;
        }
        int i_start = message.indexOf(":");
        if (i_start < 0) {
            return new TwoTuple<>(BaseErrorCode.UNKNOWN_ERROR_CODE.getErrorCode(), message);
        }
        String s_code = message.substring(0, i_start).trim();
        try {
            int code = Integer.parseInt(s_code);
            return new TwoTuple<>(code, message.substring(i_start + 1));
        } catch (Exception e) {
            return parseError(message.substring(i_start + 1));
        }
    }
}
