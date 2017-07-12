package com.niuyun.hire.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by dai.fengming on 2015/12/29.
 */
public class AppStartData {

    private Context context;

    public AppStartData(Context context) {
        this.context = context;
    }

    /**
     * 记录app是否是第一次启动：默认为0次，等于0时为第一次启动
     */
    public boolean getAppStartData() {
        int oldeVersion = 0;
        PackageManager manager = context.getPackageManager();
        int version = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SharedPreferences preferences = context.getSharedPreferences("version_or_count", 0);
        oldeVersion = preferences.getInt("version_or_count", 0);
        if (oldeVersion != version){
            saveAppStartData(version);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 保存启动次数
     */
    public void saveAppStartData(int count) {
        SharedPreferences preferences = context.getSharedPreferences("version_or_count", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("version_or_count", count);
        editor.commit();
    }

    /**
     * 用户确定升级时修改启动次数，以便重新进入引导页
     */
    public void updataAppStartData() {
        SharedPreferences preferences = context.getSharedPreferences("version_or_count", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("version_or_count", 0);
        editor.commit();
    }
}
