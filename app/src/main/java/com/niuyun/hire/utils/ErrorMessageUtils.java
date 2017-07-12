package com.niuyun.hire.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.niuyun.hire.bean.ErrorBean;

import org.json.JSONObject;

/**
 * Created by luo.xiao on 2016/1/22.
 */
public class ErrorMessageUtils {
    public static void taostErrorMessage(Context context, JSONObject obj) {
        String message = "访问网络失败";
        try {
            message = obj.getString("msg");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void taostErrorMessage(Context context, String errorMessage) {
        taostErrorMessage(context, errorMessage, "数据异常");

    }


    public static void taostErrorMessage(Context context, String errorMessage, String deafuleMessage) {
        if (!TextUtils.isEmpty(errorMessage)) {
            try {
                ErrorBean res =  JSON.parseObject(errorMessage, ErrorBean.class);
                Toast.makeText(context, res.msg, Toast.LENGTH_SHORT).show();
                return;
            } catch (Exception e) {

            }
            Toast.makeText(context, deafuleMessage, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, deafuleMessage, Toast.LENGTH_SHORT).show();
        }

    }
}
