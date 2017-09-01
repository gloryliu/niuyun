package com.niuyun.hire.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.utils.UIUtil;


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
        initLocation();
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
            }, 100);
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
    /**
     * 初始化定位
     */
    private void initLocation() {
        BaseContext.setDefultCityInfo();

        if (Build.VERSION.SDK_INT >= 23) {
            int checkLocationPermisson = ContextCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkLocationPermisson != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 11);
                return;
            } else {
                startLocation();
            }
        } else {
            startLocation();
        }
    }
    private void startLocation() {
        com.niuyun.hire.utils.map.LocationManager.getInstance().startLocation();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 11) {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocation();
            } else {
                UIUtil.showToast("无法获取位置权限，请重新设置");
            }
        }
    }
}
