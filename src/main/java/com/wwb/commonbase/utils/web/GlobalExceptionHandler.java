package com.wwb.commonbase.utils.web;


import com.wwb.commonbase.utils.exception.CustomException;
import com.wwb.commonbase.utils.response.BaseErrorCode;
import com.wwb.commonbase.utils.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;


/**
 * 全局异常处理，各个服务的Application的SpringBootApplication注解应加上scanBasePackages参数,如示例:
 *
 * @author weibo
 * @example:
 * @SpringBootApplication(scanBasePackages = {"com.moac.common.utils.web","com.moac.smsserver"})
 * public class SmsServerApplication  {
 * }
 */
@SuppressWarnings("unchecked")
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理数据格式验证失败产生的异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity handValidException(MethodArgumentNotValidException exception) {

        // ConstraintViolationException
        log.info("数据格式验证错误了");
        List<ObjectError> errors = exception.getAllErrors();
        if (exception.hasErrors()) {
            ObjectError objectError = errors.get(0);
            String message = objectError.getDefaultMessage();

            return new ResponseEntity(new CustomException(message).toResult(), HttpStatus.OK);
        }

        return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.OK);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException e) {

        return new ResponseEntity(new CustomException(e).toResult(), HttpStatus.OK);
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity handleCustomException(CustomException e) {

        return new ResponseEntity(e.toResult(), HttpStatus.OK);
    }

    /**
     * 处理运行异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(HttpServletRequest request, RuntimeException ex) {
        log.error("", ex);
        if (ex instanceof UndeclaredThrowableException) {
            UndeclaredThrowableException undeclaredEx = (UndeclaredThrowableException) ex;
            if (undeclaredEx.getUndeclaredThrowable() instanceof CustomException) {
                return new ResponseEntity(((CustomException) undeclaredEx.getUndeclaredThrowable()).toResult(), HttpStatus.OK);
            }
        }
//        log.error("请求地址：" + request.getRequestURL());
//        log.error("请求参数: " + JSONUtil.toJsonStr(request.getParameterMap()));
        ResponseResult result = ResponseResult.fail(BaseErrorCode.SYSTEM_REQUEST_EXCEPTION.getErrorCode(), ex.getMessage());
        return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
    }


    /**
     * 用来捕获404，400这种无法到达controller的错误
     *
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> defaultErrorHandler(Exception ex) throws Exception {
        log.error("", ex);
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
//        ResponseData<Object> result = new ResponseData<Object>();
//        result.setMessage(ex.getMessage());
//        if (ex instanceof NoHandlerFoundException) {
//            result.setCode("404");
//        } else {
//            result.setCode("500");
//        }
//        result.setData(null);
//        result.setSuccess(false);
//        return result;
    }

}
