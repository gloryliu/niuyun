package com.niuyun.hire.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.EventBusCenter;


/**
 * Created by dai.fengming on 2015/12/17.
 * 欢迎界面
 */
public class SplashScreenActivity extends BaseActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isFirstStart = true;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    public void initViewsAndEvents() {
        setIsOpenTitle(false);
        sharedPreferences = getSharedPreferences("SplashScreenActivity", Context.MODE_PRIVATE); //私有数据
        editor = sharedPreferences.edit();//获取编辑器
        if (sharedPreferences != null) {
            isFirstStart = sharedPreferences.getBoolean("isFirstStart", true);
        }
        if (isFirstStart) {
            isFirstStart = false;
            if (editor != null) {
                editor.putBoolean("isFirstStart", isFirstStart);
                editor.commit();
            }
            startActivity(new Intent(SplashScreenActivity.this, GuideActivity.class));
            this.finish();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    SplashScreenActivity.this.finish();
                }
            }, 1);
//            this.finish();
        }

    }


    @Override
    public void loadData() {

    }

    @Override
    public boolean isRegistEventBus() {
        return false;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {

    }

    @Override
    protected View isNeedLec() {
        return null;
    }

}
