package com.wwb.commonbase.utils;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xxx
 */
public class CustomJsonTypeHandler<E> implements TypeHandler<E> {
    private final Class<E> type;

    public CustomJsonTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        } else {
            this.type = type;
        }
    }


    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, E e, JdbcType jdbcType) throws SQLException {
        if (e != null) {
            String value = JSON.toJSONString(e);
            preparedStatement.setString(i, value);
        } else {
            preparedStatement.setString(i, "");
        }
    }

    private E toObject(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return JSON.parseObject(value, type);
    }

    @Override
    public E getResult(ResultSet resultSet, String s) throws SQLException {
        return toObject(resultSet.getString(s));
    }

    @Override
    public E getResult(ResultSet resultSet, int i) throws SQLException {
        return toObject(resultSet.getString(i));
    }

    @Override
    public E getResult(CallableStatement callableStatement, int i) throws SQLException {
        return toObject(callableStatement.getString(i));
    }
}
