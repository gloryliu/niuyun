package com.niuyun.hire.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.niuyun.hire.base.BaseContext;


/**
 * Created by chen.zhiwei on 2017-1-3.
 */

public class GetCurrentAddress {
    //基础地址
    private String DEVELOP_IP = "https://app.jyall.com";
    private String TEST_IP = "https://app.jyall.com";
    private String OFFICIAL_IP = "https://app.jyall.com";

    //测试TFS,开发TFS
    private static String TFS_SERVER_URL_test = "https://image2.jyall.com/v1/tfs/";
    //生产TFS
    private static String TFS_SERVER_URL_official = "https://image2.jyall.com/v1/tfs/";

    private String CurrentAddress;
    private boolean debugAble;
    private SharedPreferences share;

    /**
     * 获取基础地址
     *
     * @return
     */
    public String GetBaseAddress() {
        if (GetAbled()) {
            if ("1".equals(CurrentAddress)) {
                //生产
                return OFFICIAL_IP;
            } else if ("2".equals(CurrentAddress)) {
                //测试
                return TEST_IP;
            } else if ("3".equals(CurrentAddress)) {
                //开发
                return DEVELOP_IP;
            } else {
                return OFFICIAL_IP;
            }
        }
        return OFFICIAL_IP;

    }

    /**
     * @return
     *
     * 获取 银联 支付模式
     * 00 正式 01 测试
     */
    public String GetYLPayMode() {
        if (GetAbled()) {
            if ("1".equals(CurrentAddress)) {
                //生产
                return "00";
            } else if ("2".equals(CurrentAddress)) {
                //测试
                return "01";
            } else if ("3".equals(CurrentAddress)) {
                //开发
                return "01";
            } else {
                return "00";
            }
        }
        return "00";

    }

    /**
     * 获取图片TFS地址
     *
     * @return
     */
    public String GetTFSAddress() {
        if (GetAbled()) {
            if ("1".equals(CurrentAddress)) {
                //生产
                return TFS_SERVER_URL_official;
            } else if ("2".equals(CurrentAddress)) {
                //测试
                return TFS_SERVER_URL_test;
            } else if ("3".equals(CurrentAddress)) {
                //开发
                return TFS_SERVER_URL_test;
            } else {
                return TFS_SERVER_URL_official;
            }
        }
        return TFS_SERVER_URL_official;
    }

    /**
     * 获取缓存的状态,1:生产 2：测试 3：开发，空：获取异常
     *
     * @return
     */
    public String getCacheSurrounding() {
        if (GetAbled()) {
            return CurrentAddress;
        }
        return "";
    }

    public boolean getCacheDebugAble() {
        return getDebugAble();
    }

    /**
     * 获取debug标记
     *
     * @return
     */
    private boolean getDebugAble() {
        if (share == null) {
            share = BaseContext.getInstance().getSharedPreferences("LoginActivity", Activity.MODE_PRIVATE);
        }
        debugAble = share.getBoolean("DebugAble", false);
        return debugAble;
    }

    /**
     * 获取地址标记
     *
     * @return
     */
    private boolean GetAbled() {
        if (share == null) {
            share = BaseContext.getInstance().getSharedPreferences("LoginActivity", Activity.MODE_PRIVATE);
        }
        if (TextUtils.isEmpty(CurrentAddress)) {
            CurrentAddress = share.getString("ChangeAdress", "1");
        }
        return true;
    }

}
