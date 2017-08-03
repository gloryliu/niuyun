package com.niuyun.hire.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.easefun.polyvsdk.live.chat.PolyvChatManager;
import com.easemob.redpacketsdk.RPInitRedPacketCallback;
import com.easemob.redpacketsdk.RPValueCallback;
import com.easemob.redpacketsdk.RedPacket;
import com.easemob.redpacketsdk.bean.RedPacketInfo;
import com.easemob.redpacketsdk.bean.TokenData;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.niuyun.hire.ui.bean.AllTagBean;
import com.niuyun.hire.ui.bean.UserInfoBean;
import com.niuyun.hire.ui.chat.DemoHelper;
import com.niuyun.hire.utils.SharePreManager;
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


/**
 */

public class BaseContext extends MultiDexApplication {
    /**
     * 登录聊天室/ppt直播所需，请填写自己的appId和appSecret，否则无法登陆
     * appId和appSecret在直播系统管理后台的用户信息页的API设置中用获取
     */
    private static final String appId = "es99t13h27";
    private static final String appSecret = "93b89c2a81494f5ba4a4dfa5873e0c38";
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

    public AllTagBean getAllTagBean() {
        this.allTagBean = instance.getDaoSession().getAllTagBeanDao().loadAll().get(0);
        return allTagBean;
    }

    public void setAllTagBean(AllTagBean allTagBeanDao) {
        instance.getDaoSession().getAllTagBeanDao().deleteAll();
        instance.getDaoSession().getAllTagBeanDao().insert(allTagBeanDao);
        this.allTagBean = allTagBeanDao;
    }


//    public LocationInfo getLocationInfo() {
//        if (locationInfo == null || TextUtils.isEmpty(locationInfo.getCityId())) {
////            setDefultCityInfo();
//        }
//        return locationInfo;
//    }

//    public void setLocationInfo(LocationInfo locationInfo) {
////        this.locationInfo = locationInfo;
//        SharePrefUtil.saveString(getInstance(), "cityName", locationInfo.getCity());
//        SharePrefUtil.saveString(getInstance(), "cityId", locationInfo.getCityId());
//
//    }

//    private LocationInfo locationInfo;

    /**
     * 初始化聊天室配置
     */
    public void initPolyvChatConfig() {
        PolyvChatManager.initConfig(appId, appSecret);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;
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


        //init demo helper
        DemoHelper.getInstance().init(this);
        //red packet code : 初始化红包SDK，开启日志输出开关
        RedPacket.getInstance().initRedPacket(this, RPConstant.AUTH_METHOD_EASEMOB, new RPInitRedPacketCallback() {

            @Override
            public void initTokenData(RPValueCallback<TokenData> callback) {
                TokenData tokenData = new TokenData();
                tokenData.imUserId = EMClient.getInstance().getCurrentUser();
                //此处使用环信id代替了appUserId 开发者可传入App的appUserId
                tokenData.appUserId = EMClient.getInstance().getCurrentUser();
                tokenData.imToken = EMClient.getInstance().getAccessToken();
                //同步或异步获取TokenData 获取成功后回调onSuccess()方法
                callback.onSuccess(tokenData);
            }

            @Override
            public RedPacketInfo initCurrentUserSync() {
                //这里需要同步设置当前用户id、昵称和头像url
                String fromAvatarUrl = "";
                String fromNickname = EMClient.getInstance().getCurrentUser();
                EaseUser easeUser = EaseUserUtils.getUserInfo(fromNickname);
                if (easeUser != null) {
                    fromAvatarUrl = TextUtils.isEmpty(easeUser.getAvatar()) ? "none" : easeUser.getAvatar();
                    fromNickname = TextUtils.isEmpty(easeUser.getNick()) ? easeUser.getUsername() : easeUser.getNick();
                }
                RedPacketInfo redPacketInfo = new RedPacketInfo();
                redPacketInfo.fromUserId = EMClient.getInstance().getCurrentUser();
                redPacketInfo.fromAvatarUrl = fromAvatarUrl;
                redPacketInfo.fromNickName = fromNickname;
                return redPacketInfo;
            }
        });
        RedPacket.getInstance().setDebugMode(true);
        //end of red packet code

        initPolyvChatConfig();

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
//    public String selectCityId(String cityName) {
//
//
//        String cityId = Constants.DEFAULT_CITYID;
//        if (!TextUtils.isEmpty(cityName)) {
//            try {
//                List<City> list = getDaoSession().getCityDao().queryBuilder().where(CityDao.Properties.Name.eq(cityName)).list();
//                if (null != list && list.size() > 0) {
//                    cityId = list.get(0).cityId;
//                }
//
//            } catch (Exception e) {
//                LogUtils.e(e.getMessage());
//            }
//
//            LogUtils.e("根据定位城市名称查询本地库中的城市成功-----cityId--------->>>>" + cityId);
//        }
//
//
//        return cityId;
//    }


    /**
     * 设置默认的城市信息
     */
//    public void setDefultCityInfo() {
//        LocationInfo locationInfo = new LocationInfo();
//        //如果SP中cityName和cityName都不为空则取SP中的值
//        if (!TextUtils.isEmpty(SharePrefUtil.getString(getInstance(), "cityName", null)) &&
//                (!TextUtils.isEmpty(SharePrefUtil.getString(getInstance(), "cityId", null)))) {
//            locationInfo.setCityId(SharePrefUtil.getString(getInstance(), "cityId", Constants.DEFAULT_CITYID));
//            locationInfo.setCity(SharePrefUtil.getString(getInstance(), "cityName", "北京市"));
//        } else {
//            locationInfo.setCityId(Constants.DEFAULT_CITYID);
//            locationInfo.setCity("北京市");
//        }
//
//        locationInfo.setProvince("北京市");
//        locationInfo.setDistrict("东城区");
//        locationInfo.setLatitude(39.9);
//        locationInfo.setLongitude(116.3);
//        this.setLocationInfo(locationInfo);
//    }


    /**
     * 清除用户信息
     */
    public void Exit() {
        SharePreManager.instance(this).clearUserInfO();
        userInfo = null;
//        AppManager.getAppManager().finishAllActivity();
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

//    public void restartApp() {
//        if (!Utils.isBackground(this)) {
//            Intent intent = new Intent(instance, SplashActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            instance.startActivity(intent);
//        }
//        AppManager.getAppManager().AppExit(this);
//        android.os.Process.killProcess(android.os.Process.myPid()); //
//    }
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
}
