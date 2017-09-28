package com.niuyun.hire.base;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;
import com.niuyun.hire.ui.activity.SplashScreenActivity;
import com.niuyun.hire.ui.bean.AllTagBean;
import com.niuyun.hire.ui.bean.City;
import com.niuyun.hire.ui.bean.LocationInfo;
import com.niuyun.hire.ui.bean.UserInfoBean;
import com.niuyun.hire.ui.chat.utils.Foreground;
import com.niuyun.hire.ui.utils.LoginUtils;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.SharePreManager;
import com.niuyun.hire.utils.SharePrefUtil;
import com.niuyun.hire.utils.Utils;
import com.niuyunzhipin.greendao.CityDao;
import com.niuyunzhipin.greendao.DaoMaster;
import com.niuyunzhipin.greendao.DaoSession;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

import java.util.List;


/**
 */

public class BaseContext extends MultiDexApplication {
    /**
     * 登录聊天室/ppt直播所需，请填写自己的appId和appSecret，否则无法登陆
     * appId和appSecret在直播系统管理后台的用户信息页的API设置中用获取
     */
    private static final String appId = "es99t13h27";
    private static final String appSecret = "93b89c2a81494f5ba4a4dfa5873e0c38";
    //加密秘钥和加密向量，在后台->设置->API接口中获取，用于解密SDK加密串
    //值修改请参考https://github.com/easefun/polyv-android-sdk-demo/wiki/10.%E5%85%B3%E4%BA%8E-SDK%E5%8A%A0%E5%AF%86%E4%B8%B2-%E4%B8%8E-%E7%94%A8%E6%88%B7%E9%85%8D%E7%BD%AE%E4%BF%A1%E6%81%AF%E5%8A%A0%E5%AF%86%E4%BC%A0%E8%BE%93
    /**
     * 加密秘钥
     */
    private String aeskey = "VXtlHmwfS2oYm0CZ";
    /**
     * 加密向量
     */
    private String iv = "2u9gDPKdX6GyQJKU";
    private static BaseContext instance;
    //用户信息
    public static UserInfoBean userInfo;
    public static int type = 0;//照片选择，分为最多九张和只选一张,1代表选择一张，其他九张
    //    private ResponseBodyBeanDao userInfoDao;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private AllTagBean allTagBean;
    public static String currentUserNick = "";
    private static LocationInfo locationInfo;
    public AllTagBean getAllTagBean() {
        this.allTagBean = instance.getDaoSession().getAllTagBeanDao().loadAll().get(0);
        return allTagBean;
    }

    public void setAllTagBean(AllTagBean allTagBeanDao) {
        instance.getDaoSession().getAllTagBeanDao().deleteAll();
        instance.getDaoSession().getAllTagBeanDao().insert(allTagBeanDao);
        this.allTagBean = allTagBeanDao;
    }


    public LocationInfo getLocationInfo() {
        if (locationInfo == null || TextUtils.isEmpty(locationInfo.getCityId())) {
//            setDefultCityInfo();
        }
        return locationInfo;
    }

    public static void setLocationInfo(LocationInfo locationInfo) {
        getInstance().locationInfo = locationInfo;
        SharePrefUtil.saveString(getInstance(), "cityName", locationInfo.getCity());
        SharePrefUtil.saveString(getInstance(), "cityId", locationInfo.getCityId());

    }



    /**
     * 初始化聊天室配置
     */
//    public void initPolyvChatConfig() {
//        PolyvChatManager.initConfig(appId, appSecret);
//        com.easefun.polyvsdk.rtmp.chat.PolyvChatManager.initConfig(appId, appSecret);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;
        SDKInitializer.initialize(getApplicationContext());//百度地图
//        initPolyvChatConfig();
//        initPolyvCilent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new MaterialHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
        //初始化数据库
        initGreenrobot();
        //城市列表
//        initCityDataBase();


        Foreground.init(this);
        if(MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify){
                        //消息被设置为需要提醒
                        notification.doNotify(getApplicationContext(), android.R.mipmap.sym_def_app_icon);
                    }
                }
            });
        }

    }

//    private void initCityDataBase() {
//        JyApi api = RestAdapterManager.getApi();
//        Call<LinkedHashMap<String, List<City>>> cities = api.getCities();
//        cities.enqueue(new JyCallBack<LinkedHashMap<String, List<City>>>() {
//            @Override
//            public void onSuccess(Call<LinkedHashMap<String, List<City>>> call, Response<LinkedHashMap<String, List<City>>> response) {
//                List<City> cityBean = new ArrayList<>();
//                if (null != response && null != response.body()) {
//                    LinkedHashMap<String, List<City>> data = response.body();
//                    if (null != data && data.size() > 0) {
//                        for (Map.Entry<String, List<City>> entry : data.entrySet()) {
//
//                            List<City> subList = entry.getValue();
//                            for (City city : subList) {
//                                city.sort = entry.getKey();
//                                if(TextUtils.isEmpty(city.getShortPy())){
//                                    city.setShortPy(entry.getKey());
//                                }
//                            }
//                            cityBean.addAll(subList);
//
//                        }
//                        if (null != cityBean && !cityBean.isEmpty()) {
//                            saveCities(cityBean);
//                        }
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onError(Call<LinkedHashMap<String, List<City>>> call, Throwable t) {
//
//            }
//
//            @Override
//            public void onError(Call<LinkedHashMap<String, List<City>>> call, Response<LinkedHashMap<String, List<City>>> response) {
//
//            }
//        });
//
//    }

//    private void saveCities(List<City> cityBean) {
//
//        instance.getDaoSession().getCityDao().deleteAll();
//        if (null != cityBean && cityBean.size() > 0) {
//            long id = 0;
//            for (City city : cityBean) {
//                id = id + 1;
//                city.setId(id);
//            }
//            instance.getDaoSession().getCityDao().insertInTx(cityBean);
//        }
//
//        final List<City> cities = instance.getDaoSession().getCityDao().loadAll();
//        LogUtils.e(cities.size() + "");
//
//    }

    /**
     * @return 获取实例
     */
    public static BaseContext getInstance() {
        return instance;
    }

    public void setUserInfo(UserInfoBean info) {
        userInfo = info;
    }

    public UserInfoBean getUserInfo() {
        if (userInfo == null) {
            userInfo = SharePreManager.instance(this).getUserInfo();
        }
        return userInfo;
    }

    /**
     * @param info 更新用户信息
     */
    public void updateUserInfo(UserInfoBean info) {
        SharePreManager.instance(this).setUserInfo(info);
    }


    /**
     * 根据城市名称查询城市id
     *
     * @param cityName 城市名称
     * @return
     */
    public String selectCityId(String cityName) {


        String cityId = Constants.DEFAULT_CITYID;
        if (!TextUtils.isEmpty(cityName)) {
            try {
                List<City> list = getDaoSession().getCityDao().queryBuilder().where(CityDao.Properties.Name.eq(cityName)).list();
                if (null != list && list.size() > 0) {
                    cityId = list.get(0).cityId;
                }

            } catch (Exception e) {
                LogUtils.e(e.getMessage());
            }

            LogUtils.e("根据定位城市名称查询本地库中的城市成功-----cityId--------->>>>" + cityId);
        }


        return cityId;
    }


    /**
     * 设置默认的城市信息
     */
    public static void setDefultCityInfo() {
        LocationInfo locationInfo = new LocationInfo();
        //如果SP中cityName和cityName都不为空则取SP中的值
        if (!TextUtils.isEmpty(SharePrefUtil.getString(getInstance(), "cityName", null)) &&
                (!TextUtils.isEmpty(SharePrefUtil.getString(getInstance(), "cityId", null)))) {
            locationInfo.setCityId(SharePrefUtil.getString(getInstance(), "cityId", Constants.DEFAULT_CITYID));
            locationInfo.setCity(SharePrefUtil.getString(getInstance(), "cityName", "北京市"));
        } else {
            locationInfo.setCityId(Constants.DEFAULT_CITYID);
            locationInfo.setCity("北京市");
        }

        locationInfo.setProvince("北京市");
        locationInfo.setDistrict("东城区");
        locationInfo.setLatitude(39.9);
        locationInfo.setLongitude(116.3);
        setLocationInfo(locationInfo);
    }


    /**
     * 清除用户信息
     */
    public void Exit() {
//        EMClient.getInstance().logout(false);
        LoginUtils.quitIm();
        SharePreManager.instance(this).clearUserInfO();
        userInfo = null;
        AppManager.getAppManager().finishAllActivity();
        restartApp();
//        exitfromServer();
    }

    /**
     * 退出登录接口
     */
//    private void exitfromServer() {
//
//        String tokenID;
//
//        if (null != BaseContext.getInstance().getUserInfo()) {
//            tokenID = BaseContext.getInstance().getUserInfo().getResponseBody().getTokenid();
//        } else {
//            return;
//        }
//        userInfo = null;
//        RestAdapterManager.getApi().LoginOut(tokenID).enqueue(new JyCallBack<String>() {
//            @Override
//            public void onSuccess(Call<String> call, Response<String> response) {
////                EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.Tag.User_Logout));
//            }
//
//            @Override
//            public void onError(Call<String> call, Throwable t) {
//
//            }
//
//            @Override
//            public void onError(Call<String> call, Response<String> response) {
//                try {
//                    ErrorMessageUtils.taostErrorMessage(BaseContext.getInstance().getApplicationContext(), response.errorBody().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
////                EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.Tag.User_Logout));
//            }
//        });
//    }
    public void restartApp() {
        if (!Utils.isBackground(this)) {
            Intent intent = new Intent(instance, SplashScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            instance.startActivity(intent);
        }
        AppManager.getAppManager().AppExit(this);
        android.os.Process.killProcess(android.os.Process.myPid()); //
    }

    //
    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    private void initGreenrobot() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "jyHealth-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

//    public void initPolyvCilent() {
//        //网络方式取得SDK加密串，（推荐）
//        //网络获取到的SDK加密串可以保存在本地SharedPreference中，下次先从本地获取
////		new LoadConfigTask().execute();
//        PolyvSDKClient client = PolyvSDKClient.getInstance();
//        //使用SDK加密串来配置
//        client.setConfig("CMWht3MlpVkgpFzrLNAebYi4RdQDY/Nhvk3Kc+qWcck6chwHYKfl9o2aOVBvXVTRZD/14XFzVP7U5un43caq1FXwl0cYmTfimjTmNUYa1sZC1pkHE8gEsRpwpweQtEIiTGVEWrYVNo4/o5jI2/efzA==", aeskey, iv, getApplicationContext());
//        //初始化SDK设置
//        client.initSetting(getApplicationContext());
//        //启动Bugly
////        client.initCrashReport(getApplicationContext());
//        //启动Bugly后，在学员登录时设置学员id
////		client.crashReportSetUserId(userId);
//        //获取SD卡信息
//        PolyvDevMountInfo.getInstance().init(this, new PolyvDevMountInfo.OnLoadCallback() {
//
//            @Override
//            public void callback() {
//                //是否有可移除的存储介质（例如 SD 卡）或内部（不可移除）存储可供使用。
//                if (!PolyvDevMountInfo.getInstance().isSDCardAvaiable()) {
//                    // TODO 没有可用的存储设备,后续不能使用视频缓存功能
//                    LogUtils.e("没有可用的存储设备,后续不能使用视频缓存功能");
//                    return;
//                }
//
//                //可移除的存储介质（例如 SD 卡），需要写入特定目录/storage/sdcard1/Android/data/包名/。
//                String externalSDCardPath = PolyvDevMountInfo.getInstance().getExternalSDCardPath();
//                if (!TextUtils.isEmpty(externalSDCardPath)) {
//                    StringBuilder dirPath = new StringBuilder();
//                    dirPath.append(externalSDCardPath).append(File.separator).append("Android").append(File.separator).append("data")
//                            .append(File.separator).append(getPackageName()).append(File.separator).append("polyvdownload");
//                    File saveDir = new File(dirPath.toString());
//                    if (!saveDir.exists()) {
//                        getExternalFilesDir(null); // 生成包名目录
//                        saveDir.mkdirs();//创建下载目录
//                    }
//
//                    //设置下载存储目录
//                    PolyvSDKClient.getInstance().setDownloadDir(saveDir);
//                    return;
//                }
//
//                //如果没有可移除的存储介质（例如 SD 卡），那么一定有内部（不可移除）存储介质可用，都不可用的情况在前面判断过了。
//                File saveDir = new File(PolyvDevMountInfo.getInstance().getInternalSDCardPath() + File.separator + "polyvdownload");
//                if (!saveDir.exists()) {
//                    saveDir.mkdirs();//创建下载目录
//                }
//
//                //设置下载存储目录
//                PolyvSDKClient.getInstance().setDownloadDir(saveDir);
//            }
//        });
//    }

}
