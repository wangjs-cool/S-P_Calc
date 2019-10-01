package com.wjs.calc;

import java.math.BigDecimal;

/**
 * Created by cool on 2017/8/31.
 */

public class ArithHelper {

    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 32;

    // 这个类不能实例化
    private ArithHelper() {
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */

//    public static double add(double v1, double v2) {
//        BigDecimal b1 = new BigDecimal(Double.toString(v1));
//        BigDecimal b2 = new BigDecimal(Double.toString(v2));
//        return b1.add(b2).doubleValue();
//    }

    public static double add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */

//    public static double sub(double v1, double v2) {
//        BigDecimal b1 = new BigDecimal(Double.toString(v1));
//        BigDecimal b2 = new BigDecimal(Double.toString(v2));
//        return b1.subtract(b2).doubleValue();
//    }

    public static double sub(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1
     *            被乘数
     * @param v2
     *            乘数
     * @return 两个参数的积
     */

//    public static double mul(double v1, double v2) {
//        BigDecimal b1 = new BigDecimal(Double.toString(v1));
//        BigDecimal b2 = new BigDecimal(Double.toString(v2));
//        return b1.multiply(b2).doubleValue();
//    }

    public static double mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @return 两个参数的商
     */

//    public static double div(double v1, double v2) {
//        return div(v1, v2, DEF_DIV_SCALE);
//    }

    public static double div(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

//    /**
//     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
//     *
//     * @param v1 被除数
//     * @param v2 除数
//     * @param scale 表示表示需要精确到小数点以后几位。
//     * @return 两个参数的商
//     */

//    public static double div(double v1, double v2, int scale) {
//        if (scale < 0) {
//            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
//        }
//        BigDecimal b1 = new BigDecimal(Double.toString(v1));
//        BigDecimal b2 = new BigDecimal(Double.toString(v2));
//        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
//    }

//    /**
//     * 提供精确的小数位四舍五入处理。
//     *
//     * @param v 需要四舍五入的数字
//     * @param scale 小数点后保留几位
//     * @return 四舍五入后的结果
//     */

//    public static double round(double v, int scale) {
//        if (scale < 0) {
//            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
//        }
//        BigDecimal b = new BigDecimal(Double.toString(v));
//        BigDecimal one = new BigDecimal("1");
//        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
//    }

//    public static double round(String v, int scale) {
//        if (scale < 0) {
//            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
//        }
//        BigDecimal b = new BigDecimal(v);
//        BigDecimal one = new BigDecimal("1");
//        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
//    }

    public static double pow(String v1, String v2) {
        double b1 = Double.valueOf(v1);
        double b2 = Double.valueOf(v2);
        return Math.pow(b1,b2);
    }
    public static double root(String v1, String v2) {
        double b1 = Double.valueOf(v1);
        double b2 = Double.valueOf(v2);
        return Math.pow(b2,1.0/b1);
    }
    public static double sin(String v1, String v2){
        double b2 = Double.valueOf(v2);
        return Math.sin(b2);
    }
    public static double cos(String v1, String v2){
        double b2 = Double.valueOf(v2);
        return Math.cos(b2);
    }
    public static double tan(String v1, String v2){
        double b2 = Double.valueOf(v2);
        return Math.tan(b2);
    }
    public static double asin(String v1, String v2){
        double b2 = Double.valueOf(v2);
        return Math.asin(b2);
    }
    public static double acos(String v1, String v2){
        double b2 = Double.valueOf(v2);
        return Math.acos(b2);
    }
    public static double atan(String v1, String v2){
        double b2 = Double.valueOf(v2);
        return Math.atan(b2);
    }
    public static double lg(String v1, String v2){
        double b2 = Double.valueOf(v2);
        return Math.log10(b2);
    }
    public static double ln(String v1, String v2){
        double b2 = Double.valueOf(v2);
        return Math.log(b2);
    }
    public static double fac(String v1, String v2){
        double b1 = Double.valueOf(v1);
        if(b1==(long)b1) {
            for (long i = (long) b1-1; i > 0; i--) {
                b1=b1*i;
            }
            return b1;
        }else {
            return 0.0/0.0;
        }
    }
    public static double percent(String v1, String v2){
        double b1 = Double.valueOf(v1);
        return b1/100;
    }
}