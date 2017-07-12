package com.niuyun.hire.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.niuyun.hire.ui.bean.UserInfoBean;

/**
 * Created by liu.zhenrong on 2016/8/22.
 * 对SharedPreferences存储进行管理
 */
public class SharePreManager {
    private static SharePreManager preManager;

    private SharePreManager(Context context) {
        PreferencesUtils.initPrefs(context);
    }

    public static synchronized SharePreManager instance(Context context) {
        if (preManager == null)
            preManager = new SharePreManager(context);
        return preManager;
    }

    /**
     * userInfo 保存在SharePreferences中
     * @param userInfo
     */
    public void setUserInfo(UserInfoBean userInfo) {
        PreferencesUtils.putString("loginuser", JSON.toJSONString(userInfo));
    }

    /**
     * 是否显示小红点
     * @param isShow
     */
    public void setNoticeRedDotFlag(boolean isShow){
        PreferencesUtils.putBoolean("redDotFlag",isShow);
    }

    /**
     *
     * @return
     */
    public boolean getNoticeRedDotFlag(){
        return PreferencesUtils.getBoolean("redDotFlag",false);
    }
    /**
     * 退出登录后清除用户的本地信息
     */
    public void clearUserInfO(){
        PreferencesUtils.remove("loginuser");
    }

    /**
     * 得到本地用户信息
     * @return
     */
    public UserInfoBean getUserInfo(){
        UserInfoBean  userInfo = null;
        String userJson = PreferencesUtils.getString("loginuser","");
        if(!TextUtils.isEmpty(userJson)){
            userInfo = JSON.parseObject(userJson, UserInfoBean.class);
        }
        return userInfo;
    }

    /**
     * 存贮登录时间
     * @param time
     */
    public void setLoginTime(long time){
        PreferencesUtils.putLong("logintime", time);
    }

    /**
     * 获取登录时间
     * @return
     */
    public long getLoginTime(){
         return PreferencesUtils.getLong("logintime",0);
    }


}
