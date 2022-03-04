package com.wwb.commonbase.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据查询条件
 *
 * @author xxx
 */
public abstract class ISearchModel<T> {

    /**
     * 装载查询条件
     *
     * @return
     */
    public abstract QueryWrapper<T> toQueryWrapper();

    /**
     * 统计数量
     *
     * @param service
     * @return
     */
    public int count(IService<T> service) {
        return service.count(toQueryWrapper());
    }

    /**
     * 查询一条数据
     *
     * @param service
     * @return
     */
    public T getOne(IService<T> service) {
        return service.getOne(toQueryWrapper());
    }

    /**
     * 查询数据集合
     *
     * @param service
     * @return
     */
    public List<T> list(IService<T> service) {
        return list(service, null);
    }

    /**
     * 查询数据集合
     *
     * @param converter
     * @param service
     * @return
     */
    public <R> List<R> list(IService<T> service, Function<T, R> converter) {
        List<T> list = service.list(toQueryWrapper());
        if (converter == null) {
            return (List<R>) list;
        }
        return convert(list, converter);
//        return entityMap(service.list(toQueryWrapper()), converter);
    }

    /**
     * T
     * 数据类型转换
     *
     * @param list
     * @param converter
     * @return
     */
    protected List<T> entityMap(List<T> list, Function<T, T> converter) {
        if (CollectionUtils.isEmpty(list) || converter == null) {
            return list;
        }
        return list.stream().map(converter).collect(Collectors.toList());
    }

    /**
     * T
     * 数据类型转换
     *
     * @param list
     * @param converter
     * @return
     */
    protected <R> List<R> convert(List<T> list, Function<T, R> converter) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (converter == null) {
            return (List<R>) list;
        }
        return list.stream().map(converter).collect(Collectors.toList());
    }
}
