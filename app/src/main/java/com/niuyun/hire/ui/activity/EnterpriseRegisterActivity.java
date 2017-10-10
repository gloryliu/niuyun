package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.AppManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.bean.ErrorBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UserInfoBean;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.NetUtil;
import com.niuyun.hire.utils.TelephoneUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.CleanableEditText;
import com.niuyun.hire.view.TitleBar;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 企业注册
 * Created by chen.zhiwei on 2017-7-19.
 */

public class EnterpriseRegisterActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.phone_name)
    CleanableEditText etPhone;
    @BindView(R.id.et_check_code)
    EditText etCode;
    @BindView(R.id.tv_check_code)
    TextView tvGetCode;


    @BindView(R.id.tv_next)
    TextView tvNext;

    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.user_password_again)
    CleanableEditText user_password_again;
    @BindView(R.id.user_password)
    CleanableEditText user_password;
    @BindView(R.id.company_name)
    CleanableEditText company_name;
    @BindView(R.id.user_name)
    CleanableEditText user_name;
    @BindView(R.id.tv_to_login)
    TextView tv_to_login;
    CountDownTimer timer;

    boolean isCodeSended = false;
    Call<SuperBean<UserInfoBean>> call;//注册
    Call<ErrorBean> getCheckCodeCall;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_enterprise_register_layout;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        etCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        tv_to_login.setOnClickListener(this);
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
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean isRegistEventBus() {
        return false;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {

    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    @OnClick(R.id.tv_check_code)
    public void onGetCodeClick(View view) {
        String phoneNumber = etPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            UIUtil.showToast("请输入手机号");
            return;
        }
        if (phoneNumber.trim().length() != 11) {
            Toast.makeText(this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.tv_next)
    public void onNextClick(View view) {
        if (!NetUtil.isNetworkConnected(this)) {
            UIUtil.showToast("网络连接失败，请检查您的网络");
            return;
        }
        if (TextUtils.isEmpty(company_name.getText())) {
            UIUtil.showToast("公司名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(user_name.getText())) {
            UIUtil.showToast("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(etPhone.getText())) {
            UIUtil.showToast("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(etCode.getText())) {
            UIUtil.showToast("验证码不能为空");
            return;
        }

        if (TextUtils.isEmpty(user_password.getText())) {
            UIUtil.showToast("密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(user_password_again.getText())) {
            UIUtil.showToast("确认密码不能为为空");
            return;
        }
        if (!user_password_again.getText().toString().equals(user_password.getText().toString())) {
            UIUtil.showToast("两次密码不一致");
            return;
        }
//        final String tel = etPhone.getText().toString();
//        final String code = etCode.getText().toString();
        HashMap<String, String> map = new HashMap<>();
        map.put("companyname", company_name.getText().toString());
        map.put("username", user_name.getText().toString());
        map.put("mobile", etPhone.getText().toString());
//        map.put("checkCode", etCode.getText().toString());
        map.put("password", user_password.getText().toString());
        map.put("utype", "1");
        map.put("regType", "1");
        call = RestAdapterManager.getApi().reister(map);
        call.enqueue(new JyCallBack<SuperBean<UserInfoBean>>() {
            @Override
            public void onSuccess(Call<SuperBean<UserInfoBean>> call, Response<SuperBean<UserInfoBean>> response) {
                try {

                    if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
//                        BaseContext.getInstance().setUserInfo(response.body().getData());
                        Intent findPsIntent = new Intent(EnterpriseRegisterActivity.this, PerfectEnterpriseInformation.class);
                        timer.cancel();
                        Bundle bundle=new Bundle();
                        bundle.putString("uid", response.body().getData().uid+"");
                        bundle.putString("companyId", response.body().getData().companyId+"");
                        findPsIntent.putExtras(bundle);
                        startActivity(findPsIntent);
                        AppManager.getAppManager().finishActivity(LoginActivity.class);
                        UIUtil.showToast(response.body().getMsg());
                        finish();
                    } else {
                        try {
                            UIUtil.showToast(response.body().getMsg());
                        } catch (Exception e) {
                            UIUtil.showToast("注册失败");
                        }

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onError(Call<SuperBean<UserInfoBean>> call, Throwable t) {
                UIUtil.showToast("注册失败");
            }

            @Override
            public void onError(Call<SuperBean<UserInfoBean>> call, Response<SuperBean<UserInfoBean>> response) {
                try {
                    ErrorMessageUtils.taostErrorMessage(EnterpriseRegisterActivity.this, response.errorBody().string(), "");
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

    private void initTitle() {

        titleView.setTitle("用户注册");
        titleView.setTitleColor(Color.WHITE);
        titleView.setLeftImageResource(R.mipmap.ic_title_back);
        titleView.setLeftText("返回");
        titleView.setLeftTextColor(Color.WHITE);
        titleView.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_to_login:
                //返回登陆
                AppManager.getAppManager().finishActivity(SelectedRegisterRoler.class);
                finish();
                break;
        }
    }
}
