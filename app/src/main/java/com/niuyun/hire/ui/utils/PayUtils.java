package com.niuyun.hire.ui.utils;

import android.app.Activity;

/**
 * Created by chen.zhiwei on 2017-6-30.
 */

public class PayUtils {

    /**
     * 微信支付
     */
    private void weixinPay() {
        //1.创建微信支付请求
//        WechatPayReq wechatPayReq = new WechatPayReq.Builder()
//                .with(this) //activity实例
//                .setAppId(appid) //微信支付AppID
//                .setPartnerId(partnerid)//微信支付商户号
//                .setPrepayId(prepayid)//预支付码
////								.setPackageValue(wechatPayReq.get)//"Sign=WXPay"
//                .setNonceStr(noncestr)
//                .setTimeStamp(timestamp)//时间戳
//                .setSign(sign)//签名
//                .create();
        //2.发送微信支付请求
//        PayAPI.getInstance().sendPayRequest(wechatPayReq);


        //关于微信支付的回调
        //wechatPayReq.setOnWechatPayListener(new OnWechatPayListener);
    }


    /**
     * 支付宝支付
     */
    public void alPay(Activity context) {


    }

}
