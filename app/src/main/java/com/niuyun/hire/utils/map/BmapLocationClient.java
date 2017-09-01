package com.niuyun.hire.utils.map;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.niuyun.hire.utils.LogUtils;

/**
 */
public class BmapLocationClient {

    private Context context;
    private BaiduMap mBaiduMap;

    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    public Vibrator mVibrator;

    private LocationLinstener locationLinstener;

    /***
     * 返回定位结果
     *
     * @param context
     * @param locationLinstener
     */
    public BmapLocationClient(Context context, LocationLinstener locationLinstener) {
        this.context = context;
        this.locationLinstener = locationLinstener;
    }

    /**
     * 显示定位图层
     *
     * @param context
     * @param mBaiduMap
     */
    public BmapLocationClient(Context context, BaiduMap mBaiduMap) {
        this.context = context;
        this.mBaiduMap = mBaiduMap;
    }

    /**
     * 显示定位图层并返回定位信息
     *
     * @param context
     * @param locationLinstener
     */
    public BmapLocationClient(Context context, LocationLinstener locationLinstener, BaiduMap mBaiduMap) {
        this.context = context;
        this.locationLinstener = locationLinstener;
        this.mBaiduMap = mBaiduMap;
    }


    /**
     * 开启定位
     */
    public void startLocation(Context context) {
        mLocationClient = new LocationClient(context);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);// 设置定位监听函数
        LocationClientOption option = new LocationClientOption();
        // option.setOpenGps(true);//设置是否打开gps

        option.setCoorType("bd09ll");// 设置坐标类型百度经纬度坐标系（使用此坐标系）
        option.setScanSpan(2000);// 设置定位间隔时间
        option.setProdName("家园医疗");
        option.setIsNeedAddress(true);// 设置定位是否返回地址
        option.setAddrType("all");// all值表示返回地址信息，其他值都表示不返回地址信息
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置为高精度定位模式
        mLocationClient.setLocOption(option);

        mVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        mLocationClient.start();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null || location.getLatitude() == 4.9E-324) {
                if (null != locationLinstener) {
                    locationLinstener.locationError();
                }
                return;
            }
            //定位成功后停止進行定位
            mLocationClient.stop();
            if (null != locationLinstener) {
                locationLinstener.locationSuccess(location);
            }
            //显示定位图层
            showLocationOverLay(location);

            LogUtils.d("MyLocationListener---" + location.getLatitude() + "--" + location.getLongitude());
            LogUtils.d("定位省份---" + location.getProvince());
            LogUtils.d("定位城市---" + location.getCity());
            LogUtils.d("定位街道---" + location.getStreet());

        }
    }

    /**
     * 停止定位
     **/
    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(mMyLocationListener);
        }
    }

    /**
     * 销毁定位监听
     */
    public void destroyClient() {
        if (mLocationClient != null && mMyLocationListener != null) {
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(mMyLocationListener);
        }
    }

    /**
     * 再次请求定位
     */
    public void requestLocClick() {
        if (mLocationClient != null) {
            mLocationClient.start();
            LogUtils.d("再次定位成功----------->>>>>");
        } else {
            LogUtils.d("locClient is null or not started");
        }
    }

    /**
     * 显示定位图层
     *
     * @param location
     */
    private void showLocationOverLay(BDLocation location) {
        // 获取定位数据
        MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
        // 地图不为null时设置定位成功移动到地图的中心点并且显示当前位置的图层
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationData(locData);
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL, true, null));
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            if (u != null) {
                mBaiduMap.animateMapStatus(u);
            }
        }
    }

    PoiSearchResultListener poiSearchResultListener;
    public void setPoiSearchResultListener(PoiSearchResultListener poiSearchResultListener){
        this.poiSearchResultListener = poiSearchResultListener;
    }
    PoiSearch poi;
    String text;
    public void getSearchPointResult(final String city, final String textp){
        this.text = textp;
        if (poi == null){
            poi = PoiSearch.newInstance();
            poi.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
                @Override
                public void onGetPoiResult(PoiResult result) {

                    if (result == null
                            || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                        Toast.makeText(context, "未找到结果", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                        if (poiSearchResultListener != null){
                            poiSearchResultListener.pointResult(result, text);
                        }
                        return;
                    }
                    if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

                        // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                    }

                }

                @Override
                public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                }

                @Override
                public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

                }
            });
        }
        poi.searchInCity(new PoiCitySearchOption().city(city).keyword(text).pageCapacity(20));
    }

    public void releaseSearch(){
        poi.destroy();
    }

}
