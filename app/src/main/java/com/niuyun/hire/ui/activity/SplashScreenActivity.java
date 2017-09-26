package com.niuyun.hire.ui.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.huawei.android.pushagent.PushManager;
import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.chat.ui.customview.DialogActivity;
import com.niuyun.hire.ui.chat.utils.PushUtil;
import com.niuyun.hire.utils.SharePreManager;
import com.niuyun.hire.utils.UIUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.qcloud.presentation.business.InitBusiness;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.event.GroupEvent;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.presentation.event.RefreshEvent;
import com.tencent.qcloud.ui.NotifyDialog;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dai.fengming on 2015/12/17.
 * 欢迎界面
 */
public class SplashScreenActivity extends BaseActivity implements TIMCallBack {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isFirstStart = true;
    //    SplashPresenter presenter;
    private int LOGIN_RESULT_CODE = 100;
    private int GOOGLE_PLAY_RESULT_CODE = 200;
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    public void initViewsAndEvents() {
        initLocation();
        setIsOpenTitle(false);
        clearNotification();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final List<String> permissionsList = new ArrayList<>();
//        if (ConnectionResult.SUCCESS != GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)){
//            Toast.makeText(this, getString(R.string.google_service_not_available), Toast.LENGTH_SHORT).show();
////            GoogleApiAvailability.getInstance().getErrorDialog(this, GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this),
////                    GOOGLE_PLAY_RESULT_CODE).show();
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionsList.size() == 0) {
                init();
            } else {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PHONE_PERMISSIONS);
            }
        } else {
            init();
        }
    }

    private void start() {
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
            }, 20);
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

    /**
     * 跳转到主界面
     */

    public void navToHome() {
        //登录之前要初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig();
        userConfig.setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                Log.d(TAG, "receive force offline message");
                Intent intent = new Intent(SplashScreenActivity.this, DialogActivity.class);
                startActivity(intent);
            }

            @Override
            public void onUserSigExpired() {
                //票据过期，需要重新登录
                new NotifyDialog().show(getString(R.string.tls_expire), getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                            logout();
                    }
                });
            }
        })
                .setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
                        Log.i(TAG, "onConnected");
                    }

                    @Override
                    public void onDisconnected(int code, String desc) {
                        Log.i(TAG, "onDisconnected");
                    }

                    @Override
                    public void onWifiNeedAuth(String name) {
                        Log.i(TAG, "onWifiNeedAuth");
                    }
                });

        //设置刷新监听
        RefreshEvent.getInstance().init(userConfig);
        userConfig = FriendshipEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        userConfig = MessageEvent.getInstance().init(userConfig);
        TIMManager.getInstance().setUserConfig(userConfig);
        if (BaseContext.getInstance().getUserInfo() != null) {
//            LoginBusiness.loginIm(UserInfo.getInstance().getId(), UserInfo.getInstance().getUserSig(), this);
            LoginBusiness.loginIm(BaseContext.getInstance().getUserInfo().identify, BaseContext.getInstance().getUserInfo().userSig, this);
        }
    }

    /**
     * 跳转到登录界面
     */
    public void navToLogin() {
//        Intent intent = new Intent(getApplicationContext(), HostLoginActivity.class);
//        startActivityForResult(intent, LOGIN_RESULT_CODE);
        SharePreManager.instance(this).clearUserInfO();
        start();
    }

    /**
     * 是否已有用户登录
     */
//    public boolean isUserLogin() {
//        return UserInfo.getInstance().getId()!= null && (!TLSService.getInstance().needLogin(UserInfo.getInstance().getId()));
//    }

    /**
     * imsdk登录失败后回调
     */
    @Override
    public void onError(int i, String s) {
        Log.e(TAG, "login error : code " + i + " " + s);
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
                NotifyDialog dialog = new NotifyDialog();
                dialog.show(getString(R.string.kick_logout), getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navToHome();
                    }
                });
                break;
            case 6200:
                Toast.makeText(this, getString(R.string.login_error_timeout), Toast.LENGTH_SHORT).show();
                navToLogin();
                break;
            default:
                Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                navToLogin();
                break;
        }

    }

    /**
     * imsdk登录成功后回调
     */
    @Override
    public void onSuccess() {

        //初始化程序后台后消息推送
        PushUtil.getInstance();
        //初始化消息监听
        MessageEvent.getInstance();
        String deviceMan = android.os.Build.MANUFACTURER;
        //注册小米和华为推送
        if (deviceMan.equals("Xiaomi") && shouldMiInit()) {
            MiPushClient.registerPush(getApplicationContext(), "2882303761517480335", "5411748055335");
        } else if (deviceMan.equals("HUAWEI")) {
            PushManager.requestToken(this);
        }


//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "refreshed token: " + refreshedToken);

//        if(!TextUtils.isEmpty(refreshedToken)) {
//            TIMOfflinePushToken param = new TIMOfflinePushToken(169, refreshedToken);
//            TIMManager.getInstance().setOfflinePushToken(param, null);
//        }
//        MiPushClient.clearNotification(getApplicationContext());
//        Log.d(TAG, "imsdk env " + TIMManager.getInstance().getEnv());
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//        finish();
        start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult code:" + requestCode);
        if (LOGIN_RESULT_CODE == requestCode) {
//            Log.d(TAG, "login error no " + TLSService.getInstance().getLastErrno());
//            if (0 == TLSService.getInstance().getLastErrno()){
//                String id = TLSService.getInstance().getLastUserIdentifier();
//                UserInfo.getInstance().setId(id);
//                UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
//                navToHome();
//            } else if (resultCode == RESULT_CANCELED){
//                finish();
//            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    Toast.makeText(this, getString(R.string.need_permission), Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case 11:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocation();
                } else {
                    UIUtil.showToast("无法获取位置权限，请重新设置");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void init() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int loglvl = pref.getInt("loglvl", TIMLogLevel.DEBUG.ordinal());
        //初始化IMSDK
        InitBusiness.start(getApplicationContext(), loglvl);
        //初始化TLS
//        TlsBusiness.init(getApplicationContext());
//        String id =  TLSService.getInstance().getLastUserIdentifier();
//        UserInfo.getInstance().setId(id);
//        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
//        presenter = new SplashPresenter(this);
//        presenter.start();
        if (BaseContext.getInstance().getUserInfo() != null) {
            navToHome();
        }else {
            start();
        }

    }

    /**
     * 判断小米推送是否已经初始化
     */
    private boolean shouldMiInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 清楚所有通知栏通知
     */
    private void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        MiPushClient.clearNotification(getApplicationContext());
    }
}
