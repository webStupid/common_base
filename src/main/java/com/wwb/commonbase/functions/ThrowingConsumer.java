package com.wwb.commonbase.functions;

/**
 * 无返回值的，可捕获异常的 Function
 *
 * @author xxx
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {

    /**
     * 执行Function
     * @param t
     * @throws E
     * */
    void apply(T t) throws E;
}
