package com.yamo.cdcommoncore.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class JWDUtil {

    /**
     * 经纬度格式转换
     * Description： N 39°34'14.95"   ==>  39.570819445
     *
     * @param jwd
     * @return
     */
    public static String dmsToDouble(String jwd) {
//        if (StringUtils.isEmpty(jwd)) return null;
//        String firstStr = jwd.substring(0, 1);
//        String regEx = "[EWNS°'\"]";
//        Pattern compile = Pattern.compile(regEx);
//        String[] split = compile.split(jwd);
////        if (4 != split.length) return null;
//
//        BigDecimal b = new BigDecimal(split[1].trim())
//                .add( new BigDecimal(split[2].trim()).divide(BigDecimal.valueOf(60.0), 9, BigDecimal.ROUND_HALF_UP) )
//                .add( new BigDecimal(split[3].trim()).divide(BigDecimal.valueOf(3600.0), 9, BigDecimal.ROUND_HALF_UP) );
//        if ("W".equals(firstStr) || "S".equals(firstStr)) {
//            b = b.negate();
//        }
//        return b.toPlainString();
        if (StringUtils.isEmpty(jwd)) return null;
        jwd = jwd.replaceAll("N", "").replaceAll("E", "").replaceAll("W", "").replaceAll("E", "");
        String degree;
        if(jwd.contains("°")) {
            degree = jwd.split("°")[0];
            jwd = jwd.split("°")[1];
        }
        else return null;

        String minute;
        if (jwd.trim().split("′").length > 1) {
            minute = jwd.trim().split("′")[0];
            jwd = jwd.trim().split("′")[1];
        } else if (jwd.trim().split("'").length > 1) {
            minute = jwd.trim().split("'")[0];
            jwd = jwd.trim().split("'")[1];
        } else return null;

        String second;
        if (jwd.contains("″")) {
            if (jwd.trim().split("″").length > 1) {
                second = jwd.split("″")[0];
            } else {
                second=jwd.substring(0,jwd.indexOf("″"));
            }
        }else if(jwd.contains("\"")) {
            if (jwd.trim().split("\"").length >= 1) {
                second = jwd.trim().split("\"")[0];
            }else{
                second=jwd.substring(0,jwd.indexOf("\""));
            }
        }
        else return null;

        BigDecimal b = new BigDecimal(degree.trim())
                .add(new BigDecimal(minute.trim()).divide(BigDecimal.valueOf(60.0), 9, BigDecimal.ROUND_HALF_UP))
                .add(new BigDecimal(second.trim()).divide(BigDecimal.valueOf(3600.0), 9, BigDecimal.ROUND_HALF_UP));
        return b.toPlainString();
    }

    /**
     * 经纬度格式转换
     * Description： "39.570819445"   ==>  N 39°34'14.95"
     * "39"             ==>  N 39°00'00.00"
     * "39.0"           ==>  N 39°00'00.00"
     * "-39"            ==>  S 39°00'00.00"
     * "-39.0"          ==>  S 39°00'00.00"
     * " +39.570819445"  ==>  N 39°34'14.95"
     * " -39.570819445"  ==>  S 39°34'14.95"
     *
     * @param jwd
     * @param isJd true：经度；false：纬度
     * @return
     */
    public static String doubleToDms(String jwd, boolean isJd) {
        if (StringUtils.isEmpty(jwd) || !isNumeric(jwd)) return null;
        String firstStr = jwd.substring(0, 1);
        StringBuilder sb = new StringBuilder();
        if (isJd) {
            // 东西经度
            sb.append("-".equals(firstStr) ? "W " : "E ");
        } else {
            // 南北纬度
            sb.append("-".equals(firstStr) ? "S " : "N ");
        }

        BigDecimal big = new BigDecimal(jwd);
        Integer deg = big.intValue();
        BigDecimal rem = big.subtract(new BigDecimal(deg));
        Integer min = rem.multiply(BigDecimal.valueOf(60)).intValue();
        BigDecimal sec = (rem.multiply(BigDecimal.valueOf(60)).subtract(BigDecimal.valueOf(min)))
                .multiply(BigDecimal.valueOf(60)).setScale(2, BigDecimal.ROUND_HALF_UP);

        if ("-".equals(firstStr)) {
            // 度 分 秒
            deg = Math.abs(deg);
            min = Math.abs(min);
            sec = sec.negate();
        }
        DecimalFormat g2 = new DecimalFormat("00");
        DecimalFormat g3 = new DecimalFormat("00.00");
        sb.append(deg).append("°");
        return sb.append(g2.format(min)).append("'").append(g3.format(sec)).append("\"").toString();
    }

    /**
     * 判断字符串是否是数字
     * 校验值为true ："+12"、"12"、"-12"、"-12.5"、"+12.5"、"127777"
     * 校验值为fasle："+12.5a"、"q2.5"、"12.-5"、"12..5"、""、" "、null
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (StringUtils.isEmpty(str)) return false;
        Pattern pattern = Pattern.compile("(-|\\+)?[0-9]+.?[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static void main(String[] args) {
        String s = dmsToDouble("N 39°34'14.95\"");
        System.out.println(s);
        String s1 = doubleToDms(s, false);
        System.out.println(s1);
    }
}
