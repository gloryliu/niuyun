package com.niuyun.hire.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UserInfoBean;
import com.niuyun.hire.ui.index.MainActivity;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.SharePreManager;
import com.niuyun.hire.utils.UIUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-6-27.
 */

public class LoginUtils {
    static Call<SuperBean<UserInfoBean>> loginCall;
    static Call<SuperBean<UserInfoBean>> thirdLoginCall;
    static boolean isSuccess;

    public static void commitlogin(final Context context, final String tel, String password) {
        DialogUtils.showDialog(context, "登陆...", false);
        Map<String, String> map = new HashMap<>();
        map.put("mobile", tel);
        map.put("password", password);
        loginCall = RestAdapterManager.getApi().login(map);
        loginCall.enqueue(new JyCallBack<SuperBean<UserInfoBean>>() {
            @Override
            public void onSuccess(Call<SuperBean<UserInfoBean>> call, Response<SuperBean<UserInfoBean>> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    BaseContext.getInstance().setUserInfo(response.body().getData());
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    SharePreManager.instance(context).setLoginTime(now.getTime());
                    SharePreManager.instance(context).setUserInfo(response.body().getData());
                    SharePreManager.instance(context).setUserInfo(response.body().getData());
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.LOGIN_SUCCESS));

                    Intent jmActivityIntent = new Intent(context, MainActivity.class);
                    context.startActivity(jmActivityIntent);
                    ((Activity) context).finish();
                } else {
                    UIUtil.showToast(response.body().getMsg());
                }
            }

            @Override
            public void onError(Call<SuperBean<UserInfoBean>> call, Throwable t) {
                UIUtil.showToast("登陆失败~请稍后重试");
                DialogUtils.closeDialog();
            }

            @Override
            public void onError(Call<SuperBean<UserInfoBean>> call, Response<SuperBean<UserInfoBean>> response) {
                try {
                    DialogUtils.closeDialog();
                    ErrorMessageUtils.taostErrorMessage(context, response.errorBody().string(), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
