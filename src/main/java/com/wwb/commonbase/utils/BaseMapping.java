package com.wwb.commonbase.utils;

/**
 * 类型互转
 *
 * @author xxx
 */
public interface BaseMapping<Source, Target> {

    /**
     * target转换成source类型
     *
     * @param target
     * @return
     */
    Source convertToSource(Target target);

    /**
     * source转换成target类型
     *
     * @param source
     * @return
     */
    Target convertToTarget(Source source);
}
