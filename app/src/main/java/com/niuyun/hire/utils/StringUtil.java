package com.niuyun.hire.utils;


import java.text.DecimalFormat;
import java.util.List;

public class StringUtil {

    static DecimalFormat df = new DecimalFormat("######0.00");

    /**
     * 判断字符串是否为null或空字符串
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        boolean result = false;
        if (null == str || "".equals(str.trim())) {
            result = true;
        }
        return result;
    }


    /**
     * @param string
     * @return
     */
    public static boolean isNotNull(String string) {
        return null != string && !"".equals(string.trim());
    }

    public static String getNotNullString(String text) {
        if (text == null || "null".equals(text)) {
            return "";
        } else {
            return text;
        }
    }

    /**
     * 获取性别  1男  2女
     *
     * @param gender
     * @return
     */
    public static String getSex(String gender) {
        return gender == null ? "" : gender.equals("1") ? "男" : "女";
    }

    /**
     * 获取婚姻状态 1未婚 2已婚
     *
     * @param marriageStatus
     * @return
     */
    public static String getMarriageStatus(String marriageStatus) {
        return marriageStatus == null ? "" : marriageStatus.equals("1") ? "未婚" : "已婚";
    }


    /**
     * 保留小数点后两位
     *
     * @param dou
     * @return
     */
    public static String getDoubleTwo(double dou) {
        return df.format(dou);
    }

    public static String getDoubleTwo(String dou) {
        if (isNullOrEmpty(dou)) {
            dou = "0.00";
        }
        return String.format("%.2f", Double.parseDouble(dou));
    }


    /**
     * 将 list转化成字符串
     *
     * @param data
     * @return
     */
    public static String getStringFromList(List<String> data) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (data != null && data.size() > 0) {
            int count = data.size();
            for (int i = 0; i < count; i++) {
                String text = data.get(i);
                stringBuilder.append(text).append(",");
            }
            stringBuilder.replace(stringBuilder.lastIndexOf(","), stringBuilder.length(), "");
        }
        return stringBuilder.toString();
    }


}
