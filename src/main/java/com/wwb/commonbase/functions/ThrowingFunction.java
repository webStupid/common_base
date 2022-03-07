package com.wwb.commonbase.functions;

/**
 * 可捕获异常的Function
 *
 * @author xxx
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    /**
     * 执行方法
     *
     * @param t
     * @return
     * @throws E
     */
    R apply(T t) throws E;
}
