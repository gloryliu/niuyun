package com.niuyun.hire.utils;


import com.niuyun.hire.base.BaseContext;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by sun.luwei on 2016/12/13.\
 * 缓存
 * 应用 acache
 */

public class CacheManager {
    static Object object = new Object();
    ACache cache;
    static CacheManager instance;

    public CacheManager() throws IOException {
        cache = ACache.get(BaseContext.getInstance());
    }

    public static CacheManager getInstance() {
        synchronized (object) {
            if (null == instance) {
                try {
                    instance = new CacheManager();
                } catch (IOException e) {
                    LogUtils.e(e.getMessage());
                }
            }
        }
        return instance;
    }

    public void putString(String key, String value) {
        cache.put(key, value);
    }

    public String getString(String key) {

        return cache.getAsString(key);
    }

    public void putJson(String key, JSONObject value) {
        cache.put(key, value);
    }

    public JSONObject getJson(String key) {

        return cache.getAsJSONObject(key);
    }

    public void putJsonArray(String key, JSONArray value) {
        cache.put(key, value);
    }

    public JSONArray getJsonArray(String key) {
        return cache.getAsJSONArray(key);
    }
    public void putSerializable(String key, Serializable value) {
        cache.put(key, value);
    }

    public Object getSerializable(String key) {
        return cache.getAsObject(key);
    }

    public static class Key {

        public static String KEY_INDEX = "index";//首页
        public static String KEY_DCTOR= "doctor";//找医生频道页
        public static String KEY_MALL = "mall";//健康商城
        public static String KEY_MALL_TAG = "mall_tag";//健康商城 标签
    }

}
