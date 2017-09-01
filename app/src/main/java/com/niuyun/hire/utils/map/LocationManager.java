package com.niuyun.hire.utils.map;

import android.content.Intent;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.ui.bean.LocationInfo;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.utils.DownLoadAndSaveUtils;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.SharePrefUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 39157 on 2016/11/8.
 */

public class LocationManager {

    private BmapLocationClient bmapLocationClient;
    private BaseContext appContext;
    private static volatile LocationManager locationManager = null;
    public static final String LOCATION_SUCCESS = "location_success";

    //定位得到的
    LocationInfo locationInfo;

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    private LocationManager() {
        appContext = BaseContext.getInstance();
    }

    public static LocationManager getInstance() {
        if (locationManager == null) {
            synchronized (DownLoadAndSaveUtils.class) {
                if (locationManager == null) {
                    locationManager = new LocationManager();
                }
            }
        }
        return locationManager;
    }

    public boolean isChoose() {
        boolean isChoose = false;
        String cityId = SharePrefUtil.getString(appContext, Constants.SELECTED_CITY_ID, null);
        String cityName = SharePrefUtil.getString(appContext, Constants.SELECTED_CITY_NAME, null);
        if (!TextUtils.isEmpty(cityId) && !TextUtils.isEmpty(cityName)) {
            isChoose = true;
        } else {
            isChoose = false;
        }
        return isChoose;
    }

    public void startLocation() {
        bmapLocationClient = new BmapLocationClient(appContext, new LocationLinstener() {
            @Override
            public void locationSuccess(BDLocation location) {

                //当前定位的信息------注意此处定位的地址信息要是拿不到可以进一步进行反解析地址，暂时先不做处理
                if (null != location && location.getCity() != null) {
                    LocationInfo locationInfo = new LocationInfo();
                    locationInfo.setCityId(appContext.selectCityId(location.getCity()));
                    locationInfo.setProvince(location.getProvince());
                    locationInfo.setCity(location.getCity());
                    locationInfo.setDistrict(location.getDistrict());
                    locationInfo.setLatitude(location.getLatitude());
                    locationInfo.setLongitude(location.getLongitude());
                    if (!isChoose()) {
                        appContext.setLocationInfo(locationInfo);
                    }

                    LocationInfo locationInfo1 = new LocationInfo();
                    try {
                        locationInfo1.setCityId(appContext.selectCityId(location.getCity()));
                    } catch (Exception e) {
                        LogUtils.e(e.getMessage());
                    }
                    locationInfo1.setProvince(location.getProvince());
                    locationInfo1.setCity(location.getCity());
                    locationInfo1.setDistrict(location.getDistrict());
                    locationInfo1.setLatitude(location.getLatitude());
                    locationInfo1.setLongitude(location.getLongitude());
                    LocationManager.this.locationInfo = locationInfo1;
                    //发送广播 通知定位成功
                    Intent intent = new Intent();
                    intent.setAction(LOCATION_SUCCESS);
                    appContext.sendBroadcast(intent);
                    LogUtils.log("--------定位到的城市--->" + location.getCity());
                    LogUtils.log("--------查询数据库的城市ID--->" + locationInfo1.getCityId());
                    updateLocation();
                    //定位成功
                } else {//设置默认定位信息为北京
                    if (!isChoose()) {
                        appContext.setDefultCityInfo();
                    }
                }
                LogUtils.e("locationSuccess--city-" + location.getCity());

            }

            @Override
            public void locationError() {
                if (!isChoose()) {
                    appContext.setDefultCityInfo();
                }
                LogUtils.e("locationError--city-");
            }
        });

        bmapLocationClient.startLocation(appContext);
    }

    private void updateLocation() {
        if (BaseContext.getInstance().getUserInfo() == null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("mapX", BaseContext.getInstance().getLocationInfo().latitude + "");
        map.put("mapY", BaseContext.getInstance().getLocationInfo().longitude + "");
        map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
        Call<SuperBean<String>> updateLocation = RestAdapterManager.getApi().updateLocation(map);
        updateLocation.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {

            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {

            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {

            }
        });
    }
}
