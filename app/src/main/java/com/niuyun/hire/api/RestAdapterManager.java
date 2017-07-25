package com.niuyun.hire.api;

import com.niuyun.hire.api.FastJsonConvert.FastJsonConverterFactory;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.utils.LogUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by sun.luwei on 2016/11/23.
 */

public class RestAdapterManager {

    static OkHttpClient httpClient;
    static Retrofit restAdapter;
    //获取地址工具类对象
    /**
     * 获取基础地址服务
     */
//    public static String BASEURL ="http://59.46.12.226:8864";  // BASE URL
    public static String BASEURL ="http://192.168.51.85:8864";  // BASE URL


    public static Retrofit getRestAdapter() {

        if (null == restAdapter) {
            restAdapter = new Retrofit.Builder()
                    .baseUrl(BASEURL).client(genericClient())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .build();
        }

        return restAdapter;
    }

    public static OkHttpClient genericClient() {
        if (null == httpClient) {
            httpClient = new OkHttpClient.Builder().connectTimeout(30l, TimeUnit.SECONDS).readTimeout(30l, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request.Builder builder = chain.request().newBuilder();


                            LogUtils.e("url:" + chain.request().method() + ":" + chain.request().url());
                            builder = addHeaders(builder);
                            Request request = builder.build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

        }

        return httpClient;
    }

    /**
     * @param builder
     * @return 构建头信息
     */
    private static Request.Builder addHeaders(Request.Builder builder) {

        builder.addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Accept-Encoding", "gzip, deflate");

        return builder;

    }

    public static Map<String, String> getHeaderMap() {
        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("deviceid", MyDeviceInfo.getDeviceId());//设备id
        hashMap.put("timestamp", System.currentTimeMillis() + "");//当前时间
//        hashMap.put("devicebrand", MyDeviceInfo.getDeviceName());//设备型号
//        hashMap.put("systembrand", MyDeviceInfo.getOsVersion());//操作系统版本
//        hashMap.put("version", SysUtils.getVersionName(BaseContext.getInstance()));//应用版本
        hashMap.put("APPkey", Constants.APP_KEY);//应用名称
//        hashMap.put("lon", BaseContext.getInstance().getLocationInfo().getLongitude() + "");
//        hashMap.put("lat", BaseContext.getInstance().getLocationInfo().getLatitude() + "");
//        hashMap.put("cityId", BaseContext.getInstance().getLocationInfo().getCityId() + "");
//        hashMap.put("deviceresolution", SysUtils.getScreenWidth(BaseContext.getInstance().getInstance()) + "x" + SysUtils.getScreenHeight(BaseContext.getInstance()));//分辨率
//        String sign = Utils.getSign(hashMap);
//        hashMap.put("sign", sign);//加密串
        if (null != BaseContext.getInstance().getUserInfo()) {
            hashMap.put("token", BaseContext.getInstance().getUserInfo().token);
        }
        return hashMap;
    }

    /**
     * @return 获取api
     */
    public static JyApi getApi() {
        return getRestAdapter().create(JyApi.class);
    }

}
