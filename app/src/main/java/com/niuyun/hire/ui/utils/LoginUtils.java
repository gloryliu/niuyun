package com.niuyun.hire.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.AppManager;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UserInfoBean;
import com.niuyun.hire.ui.index.EnterpriseMainActivity;
import com.niuyun.hire.ui.index.MainActivity;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.SharePreManager;
import com.niuyun.hire.utils.UIUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;

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
    static Call<SuperBean<UserInfoBean>> getUserInfoCall;
    static Call<SuperBean<UserInfoBean>> thirdLoginCall;
    static boolean isSuccess;
    static String uid;

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
                    if (BaseContext.getInstance().getUserInfo().utype == 1) {
                        Intent jmActivityIntent = new Intent(context, EnterpriseMainActivity.class);
                        context.startActivity(jmActivityIntent);
                        AppManager.getAppManager().finishActivity(MainActivity.class);
                    } else {
                        Intent jmActivityIntent = new Intent(context, MainActivity.class);
                        context.startActivity(jmActivityIntent);
                        AppManager.getAppManager().finishActivity(EnterpriseMainActivity.class);
                    }

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

    public static void getUserByUid(String userid) {
        uid = userid;
        getUserByUid();
    }

    public static void getUserByUid() {
        if (BaseContext.getInstance().getUserInfo() == null) {
            return;
        }
        if (BaseContext.getInstance().getUserInfo() != null) {

            getUserInfoCall = RestAdapterManager.getApi().getUser(BaseContext.getInstance().getUserInfo().uid + "");
        } else {
            getUserInfoCall = RestAdapterManager.getApi().getUser(uid);

        }
        getUserInfoCall.enqueue(new JyCallBack<SuperBean<UserInfoBean>>() {
            @Override
            public void onSuccess(Call<SuperBean<UserInfoBean>> call, Response<SuperBean<UserInfoBean>> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    BaseContext.getInstance().setUserInfo(response.body().getData());
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    SharePreManager.instance(BaseContext.getInstance()).setLoginTime(now.getTime());
                    SharePreManager.instance(BaseContext.getInstance()).setUserInfo(response.body().getData());
                    SharePreManager.instance(BaseContext.getInstance()).setUserInfo(response.body().getData());
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.LOGIN_SUCCESS));
                } else {
                    UIUtil.showToast(response.body().getMsg());
                }
            }

            @Override
            public void onError(Call<SuperBean<UserInfoBean>> call, Throwable t) {
                DialogUtils.closeDialog();
            }

            @Override
            public void onError(Call<SuperBean<UserInfoBean>> call, Response<SuperBean<UserInfoBean>> response) {
            }
        });
    }

    /**
     * 登陆聊天
     */
    public static void initChat() {
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setFaceUrl(Constants.COMMON_PERSON_URL+BaseContext.getInstance().getUserInfo().avatars);

        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                Log.e("initChat", "modifyProfile failed: " + code + " desc" + desc);
            }

            @Override
            public void onSuccess() {
                Log.e("initChat", "modifyProfile succ");
            }
        });

        //设置资料关系链拉取字段，这里只关心好友验证类型、头像URL、昵称和自定义字段"Tag_Profile_Custom_Test"
//        TIMFriendshipSettings settings = new TIMFriendshipSettings();
//        long flags = 0;
//        flags |= TIMFriendshipManager.TIM_PROFILE_FLAG_ALLOW_TYPE
//                | TIMFriendshipManager.TIM_PROFILE_FLAG_FACE_URL
//                | TIMFriendshipManager.TIM_PROFILE_FLAG_NICK;
//        settings.setFlags(flags);
//        settings.addCustomTag("Tag_Profile_Custom_Test");
//
//        TIMUserConfig config = new TIMUserConfig();
//        config.setFriendshipSettings(settings);
//
////将用户配置与当前通信管理器关联
//        TIMManager.getInstance().setUserConfig(config);



        if (BaseContext.getInstance().getUserInfo() == null) {
            return;
        }
        TIMManager.getInstance().login(BaseContext.getInstance().getUserInfo().identify, BaseContext.getInstance().getUserInfo().userSig, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                UIUtil.showToast("登录失败" + i + s);
            }

            @Override
            public void onSuccess() {
                UIUtil.showToast("登录成功");
            }
        });
    }

    /**
     * 退出im
     */
    public static void quitIm() {
        TIMManager.getInstance().logout(null);
    }
}
