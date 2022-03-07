package com.wwb.commonbase.utils;

import java.math.BigDecimal;

/**
 * Decimal 操作辅助类
 *
 * @author weibo
 */
public class DecimalUtils {

    /**
     * 获取小数位数
     *
     * @param bigDecimal
     * @return 小数位数
     */
    public static int getNumberOfDecimalPlaces(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0;
        }
        String string = bigDecimal.stripTrailingZeros().toPlainString();
        int index = string.indexOf(".");
        return index < 0 ? 0 : string.length() - index - 1;
    }

    /**
     * 比较 两个数，bigDecimal1是否小于bigDecimal2
     *
     * @param bigDecimal1
     * @param bigDecimal2
     * @return 小于返回true, 否则返回false
     */
    public static boolean lessThen(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        if (bigDecimal1 == null) {
            bigDecimal1 = BigDecimal.ZERO;
        }
        if (bigDecimal2 == null) {
            bigDecimal2 = BigDecimal.ZERO;
        }
        return bigDecimal1.compareTo(bigDecimal2) == -1;
    }

    /**
     * 比较 两个数，bigDecimal1是否等于bigDecimal2
     *
     * @param bigDecimal1
     * @param bigDecimal2
     * @return 等于返回true, 否则返回false
     */
    public static boolean equal(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        if (bigDecimal1 == null) {
            bigDecimal1 = BigDecimal.ZERO;
        }
        if (bigDecimal2 == null) {
            bigDecimal2 = BigDecimal.ZERO;
        }
        return bigDecimal1.compareTo(bigDecimal2) == 0;
    }

    /**
     * 比较 两个数，bigDecimal1是否大于bigDecimal2
     *
     * @param bigDecimal1
     * @param bigDecimal2
     * @return 大于返回true, 否则返回false
     */
    public static boolean greaterThan(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        if (bigDecimal1 == null) {
            bigDecimal1 = BigDecimal.ZERO;
        }
        if (bigDecimal2 == null) {
            bigDecimal2 = BigDecimal.ZERO;
        }
        return bigDecimal1.compareTo(bigDecimal2) == 1;
    }

    /**
     * 比较 两个数，bigDecimal1是否大于或等于bigDecimal2
     *
     * @param bigDecimal1
     * @param bigDecimal2
     * @return 大于返回true, 否则返回false
     */
    public static boolean greaterThanOrEqual(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        if (bigDecimal1 == null) {
            bigDecimal1 = BigDecimal.ZERO;
        }
        if (bigDecimal2 == null) {
            bigDecimal2 = BigDecimal.ZERO;
        }
        return bigDecimal1.compareTo(bigDecimal2) == 1
                || bigDecimal1.compareTo(bigDecimal2) == 0;

    }


}
