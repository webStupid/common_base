package com.wwb.commonbase.utils;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 枚举对象处理器
 * @author xxx
 */
public class CustomEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private final Class<E> type;

    public CustomEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        } else {
            this.type = type;
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, E accountType, JdbcType jdbcType) throws SQLException {
        try {
            Method method = type.getMethod("getValue");
            int value = (int) method.invoke(accountType);
            preparedStatement.setInt(i, value);
        } catch (Exception ex) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not have the method:getValue().");
        }
    }

    @Override
    public E getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);
        return EnumUtils.valueOf(type, i, "getValue");
    }

    @Override
    public E getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int v = resultSet.getInt(i);
        return EnumUtils.valueOf(type, v, "getValue");
    }

    @Override
    public E getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int v = callableStatement.getInt(i);
        return EnumUtils.valueOf(type, v, "getValue");
    }
}
