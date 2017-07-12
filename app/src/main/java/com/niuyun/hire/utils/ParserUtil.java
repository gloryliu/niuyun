package com.niuyun.hire.utils;


import com.alibaba.fastjson.JSON;

/**
 * Created by sun.luwei on 2016/12/30.
 */

public class ParserUtil {

    public static <T> T parseObj(String content, Class<T> clazz){
        try {
            return (T) JSON.parseObject(content,clazz);
        }catch (Exception e){
            LogUtils.e("ParserUtil", "解析异常");
        }
        return null;
    }

//    public static <T> List<T> parseList(String content, Class<T> clazz) {
//
//        try {
//            return JSON.parseObject(content, new TypeToken<List<T>>(){}.getType());
//        }catch (Exception e){
//            LogUtils.e("ParserUtil", "解析异常");
//        }
//        return null;
//
//    }
    /**
     * 解析jsonArray
     *
     * @param content
     * @param clazz
     * @return
     */
    public static <T> T parseArray(String content, Class<T> clazz) {
        return (T) JSON.parseArray(content, clazz);
    }
}
