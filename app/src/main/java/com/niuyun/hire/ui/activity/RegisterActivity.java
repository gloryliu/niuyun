package com.niuyun.hire.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.niuyun.hire.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.bean.ErrorBean;
import com.niuyun.hire.ui.utils.LoginUtils;
import com.niuyun.hire.ui.utils.login.LoginApi;
import com.niuyun.hire.ui.utils.login.OnLoginListener;
import com.niuyun.hire.ui.utils.login.UserInfo;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.NetUtil;
import com.niuyun.hire.utils.TelephoneUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.CleanableEditText;
import com.niuyun.hire.view.TitleBar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by chen.zhiwei on 2016/7/14.
 * 注册
 */
public class RegisterActivity extends BaseActivity implements
        View.OnClickListener {
    @BindView(R.id.user_name)
    CleanableEditText etPhone;
    @BindView(R.id.et_check_code)
    EditText etCode;
    @BindView(R.id.tv_check_code)
    TextView tvGetCode;

    @BindView(R.id.checkBox)
    CheckBox cbAgree;


    @BindView(R.id.tv_next)
    TextView tvNext;

    @BindView(R.id.title_view)
    TitleBar titleView;

    @BindString(R.string.regist)
    String regist;

    @BindView(R.id.user_nick_name)
    CleanableEditText user_nick_name;
    @BindView(R.id.user_password)
    CleanableEditText user_password;
    @BindView(R.id.iv_weixin)
    ImageView iv_weixin;
    @BindView(R.id.iv_qq)
    ImageView iv_qq;
    @BindView(R.id.iv_sina)
    ImageView iv_sina;


    CountDownTimer timer;

    boolean isCodeSended = false;
    Call<String> call;//注册
    Call<ErrorBean> getCheckCodeCall;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.mine_regist_activity_layout;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        etCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});

        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvGetCode.setText("重新发送" + "(" + millisUntilFinished / 1000 + "s)");
                tvGetCode.setEnabled(false);
            }

            @Override
            public void onFinish() {
                tvGetCode.setText("重新发送");
                tvGetCode.setEnabled(true);
            }
        };
        iv_sina.setOnClickListener(this);
        iv_weixin.setOnClickListener(this);
        iv_qq.setOnClickListener(this);
    }

    @Override
    public void loadData() {

    }

    @OnClick(R.id.tv_check_code)
    public void onGetCodeClick(View view) {
        String phoneNumber = etPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            UIUtil.showToast("请输入手机号");
            return;
        }
        if (phoneNumber.trim().length() != 11) {
            Toast.makeText(this, "请输入11位账号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TelephoneUtils.isMobile(phoneNumber)) {
            UIUtil.showToast("手机号格式错误");
            return;
        }

        if (!NetUtil.isNetworkConnected(this)) {
            UIUtil.showToast("网络连接失败，请检查您的网络");
            return;
        }

        getCode(phoneNumber);

    }


    private void getCode(String phoneNumber) {


        if (TextUtils.isEmpty(phoneNumber)) {
            UIUtil.showToast("请输入手机号");
            return;
        } else if (!TelephoneUtils.isMobile(phoneNumber)) {
            UIUtil.showToast("手机号格式错误");
            return;
        }

        timer.start();
        getCheckCodeCall = RestAdapterManager.getApi().getCheckCode(phoneNumber);
        getCheckCodeCall.enqueue(new JyCallBack<ErrorBean>() {
            @Override
            public void onSuccess(Call<ErrorBean> call, Response<ErrorBean> response) {
                if (response != null && response.body().code == Constants.successCode) {
                    UIUtil.showToast(response.body().msg);
                } else {
                    UIUtil.showToast("发送验证码失败");
                }
            }

            @Override
            public void onError(Call<ErrorBean> call, Throwable t) {

            }

            @Override
            public void onError(Call<ErrorBean> call, Response<ErrorBean> response) {

            }
        });


    }

    private void initTitle() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.color_ff6900);


        titleView.setTitle(regist);
        titleView.setTitleColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.color_ff6900));
        titleView.setImmersive(true);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    @OnClick(R.id.userAgreement)
    public void agreeMentClick(View view) {

//        Intent web = new Intent(this, WebViewActivity.class);
//        Bundle webBundle = new Bundle();
//        webBundle.putString(Constants.STRING_TAG, AGREEMENT_URL);
//        webBundle.putBoolean("show_title", true);
//        web.putExtras(webBundle);
//        startActivity(web);

    }

//    @OnTextChanged(value = R.id.user_name, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
//    public void afterNameTextChanged(Editable s) {
//        if (s.length() == 0) {
//            tvNext.setEnabled(false);
//        } else {
//            if (cbAgree.isChecked() && isCodeSended && !TextUtils.isEmpty(etCode.getText().toString())) {
//                tvNext.setEnabled(true);
//            } else {
//                tvNext.setEnabled(false);
//            }
//
//        }
//    }
//
//    @OnTextChanged(value = R.id.et_check_code, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
//    public void afterCodeTextChanged(Editable s) {
//        if (s.length() == 0) {
//            tvNext.setEnabled(false);
//        } else {
//            if (cbAgree.isChecked() && isCodeSended && !TextUtils.isEmpty(etPhone.getText().toString())) {
//                tvNext.setEnabled(true);
//            } else {
//                tvNext.setEnabled(false);
//            }
//
//        }
//    }
//
//    @OnCheckedChanged(R.id.checkBox)
//    public void agrreeMentCheck(CompoundButton view, boolean isChecked) {
//        if (isChecked && isCodeSended && !TextUtils.isEmpty(etPhone.getText().toString()) && !TextUtils.isEmpty(etCode.getText().toString())) {
//            tvNext.setEnabled(true);
//        } else {
//            tvNext.setEnabled(false);
//        }
//    }

    @OnClick(R.id.tv_next)
    public void onNextClick(View view) {
        Map<String,String> param = new HashMap<>();
        param.put("Mobile","13691525924");
        param.put("mobile_vcode","1234");
        param.put("Password","123456");
        param.put("passwordVerify","123456");
        param.put("reg_type","2");
        param.put("Utype","2");

        call = RestAdapterManager.getApi().reister(param);
        call.enqueue(new JyCallBack<String>() {
            @Override
            public void onSuccess(Call<String> call, Response<String> response) {
                String result = response.body();
            }

            @Override
            public void onError(Call<String> call, Throwable t) {
                String ddd = t.getMessage();
            }

            @Override
            public void onError(Call<String> call, Response<String> response) {
                try {
                    String ddd = response.errorBody().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        if (call != null) {
            call.cancel();
        }
        if (getCheckCodeCall != null) {
            getCheckCodeCall.cancel();
        }
        super.onDestroy();
    }


    @Override
    public boolean isRegistEventBus() {
        return true;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {
        if (null != eventBusCenter) {
            if (eventBusCenter.getEvenCode() == Constants.REGIST_SUCCESS) {
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_weixin:
                //微信登录
                //测试时，需要打包签名；sample测试时，用项目里面的demokey.keystore
                //打包签名apk,然后才能产生微信的登录

                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                if (wechat.isClientValid()) {

                    login(wechat.getName());
                } else {
                    UIUtil.showToast("未安装微信");
                }
                break;
            case R.id.iv_qq:
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                login(qq.getName());
                break;
            case R.id.iv_sina:
                //新浪微博
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                login(sina.getName());
                break;

        }
    }

    private void login(String platformName) {
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.setOnLoginListener(new OnLoginListener() {
            public boolean onLogin(String platform, HashMap<String, Object> res) {
                // 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
                // 此处全部给回需要注册
                return true;
            }

            public boolean onRegister(UserInfo info) {
                LoginUtils.thirdLogin(RegisterActivity.this, info);
                // 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
                return true;
            }
        });
        api.login(this);
    }


}
