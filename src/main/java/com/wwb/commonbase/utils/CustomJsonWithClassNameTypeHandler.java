package com.wwb.commonbase.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xxx
 */
public class CustomJsonWithClassNameTypeHandler<E> implements TypeHandler<E> {
    private final Class<E> type;

    public CustomJsonWithClassNameTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        } else {
            this.type = type;
        }
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, E e, JdbcType jdbcType) throws SQLException {
        if (e != null) {
            String value = JSON.toJSONString(e, SerializerFeature.WriteClassName);
            preparedStatement.setString(i, value);
        } else {
            preparedStatement.setString(i, "");
        }
    }

    private E toObject(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return JSON.parseObject(value, type, Feature.SupportAutoType);
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