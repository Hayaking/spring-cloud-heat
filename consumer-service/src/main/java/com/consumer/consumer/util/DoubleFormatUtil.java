package com.consumer.consumer.util;

import java.math.BigDecimal;

public class DoubleFormatUtil {
    public static Double halfUp(Double number) {
        if (Double.isNaN(number) || Double.isInfinite(number)) {
            return 0D;
        }
        BigDecimal bigDecimal = new BigDecimal(number);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Double halfUp(double number, boolean isDecimal) {
        String v = halfUpStr(number,isDecimal);
        return Double.valueOf(v);
    }
    /**
     *
     * @param number
     * @param isDecimal 是否带小数
     * @return
     */
    public static String halfUpStr(double number,boolean isDecimal) {
        String r = String.format("%.2f", number);
        if(!isDecimal){
            r = r.substring(0,r.indexOf("."));
        }
        return r;
    }

    /**
     * 保留两位小数 不四舍五入
     * @param number
     * @return
     */
    public static Double noHalfUp(double number) {
        BigDecimal bigDecimal = new BigDecimal(number);
        return bigDecimal.setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
    }
}
