package com.niuyun.hire.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chen.zhiwei on 2016/12/29.
 * 验证电话号码统一工具
 */
public class TelephoneUtils {

    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9])|(14[0-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);

        System.out.println(m.matches() + "---");

        return m.matches();
    }

    // 判断一个字符串是否含有数字

    public static boolean hasDigit(String content) {

        boolean flag = false;

        Pattern p = Pattern.compile(".*\\d+.*");

        Matcher m = p.matcher(content);

        if (m.matches())

            flag = true;

        return flag;

    }
}
