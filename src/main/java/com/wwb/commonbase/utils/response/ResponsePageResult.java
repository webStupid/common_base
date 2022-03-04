package com.wwb.commonbase.utils.response;

import io.swagger.annotations.ApiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页返回结果集
 *
 * @param <D>
 */
@ApiModel(value = "ResponsePageResult<T>", description = "分页返回结果集")
public class ResponsePageResult<D> extends ResponseResult<PageListData<D>> {

    protected ResponsePageResult(PageListData<D> data, boolean success, int code, String msg) {
        super(data, success, code, msg);
    }

    public ResponsePageResult() {
        super();
    }


    @SuppressWarnings("rawtypes")
    public static <D> ResponsePageResult success(Long total, List<D> list) {

        return new ResponsePageResult<D>(new PageListData<D>(total, list), true, BaseErrorCode.SUCCESS_CODE.getErrorCode(), "");
    }

    @SuppressWarnings("rawtypes")
    public static <D> ResponsePageResult success(List<D> list) {
        list = null == list ? new ArrayList<D>() : list;
        return new ResponsePageResult<D>(new PageListData<D>((long) list.size(), list), true, BaseErrorCode.SUCCESS_CODE.getErrorCode(), "");
    }

    @SuppressWarnings("rawtypes")
    public static ResponsePageResult fail(int code, String msg) {
        return new ResponsePageResult<Object>(null, false, code, msg);

    }

    @SuppressWarnings("rawtypes")
    public static <D> ResponsePageResult fail(Long total, List<D> list, int code, String msg) {
        return new ResponsePageResult<D>(new PageListData<D>(total, list), false, code, msg);
    }

    @SuppressWarnings("rawtypes")
    public static ResponsePageResult fail(BaseErrorCode errorCode) {
        return new ResponsePageResult<Object>(null, false, errorCode.getErrorCode(), errorCode.getErrorMsg());
    }


}