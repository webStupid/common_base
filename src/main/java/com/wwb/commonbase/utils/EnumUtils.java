package com.wwb.commonbase.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author weibo
 */
@SuppressWarnings("unchecked")
@Slf4j
public class EnumUtils {

    @FunctionalInterface
    public interface EnumFieldFunction<T, R> extends Serializable, Function<T, R> {

        static <E extends Enum<?>> TwoTuple<Class<E>, String> getEnums(Serializable lambda) {
            try {
                Method method = lambda.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
                String getter = serializedLambda.getImplMethodName();
                String clazzName = serializedLambda.getImplClass();
                Class<E> clazz = (Class<E>) Class.forName(clazzName.replace("/", "."));
                return new TwoTuple<>(clazz, getter);

            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T, R, E extends Enum<?>> E valueOf(EnumFieldFunction<T, R> func, Object value) {
        TwoTuple<Class<E>, String> tuple = EnumFieldFunction.getEnums(func);
        return valueOf(tuple.first, value, tuple.second);
    }


    /**
     * 值映射为枚举
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param method    取值方法
     * @param <E>       对应枚举
     * @return
     */
    public static <E extends Enum<?>> E valueOf(Class<E> enumClass, Object value, Method method) {
        E[] es = enumClass.getEnumConstants();
        for (E e : es) {
            Object evalue = null;
            try {

                method.setAccessible(true);
                evalue = method.invoke(e);
            } catch (IllegalAccessException | InvocationTargetException e1) {
                log.error("Error: NoSuchMethod in " + enumClass.getName(), e1);
            }
            if (value instanceof Number && evalue instanceof Number
                    && new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(String.valueOf(evalue))) == 0) {
                return e;
            }
            if (Objects.equals(evalue, value)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 值映射为枚举
     *
     * @param enumClass  枚举类
     * @param value      枚举值
     * @param methodName 取值方法名 e.g. getValue
     * @param <E>        对应枚举
     * @return
     */
    public static <E extends Enum<?>> E valueOf(Class<E> enumClass, Object value, String methodName) {
        try {
            Method method = enumClass.getMethod(methodName);
            return valueOf(enumClass, value, method);
        } catch (NoSuchMethodException err) {
            log.error("Error: NoSuchMethod in " + enumClass.getName(), err);
        }
        return null;
    }

    /**
     * 值映射为枚举
     *
     * @param clazz   枚举类
     * @param ordinal 序号
     * @return
     */
    public static <T extends Enum<T>> T valueOf(Class<T> clazz, int ordinal) {
        return (T) clazz.getEnumConstants()[ordinal];
    }
}
