package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
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
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.bean.ErrorBean;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.NetUtil;
import com.niuyun.hire.utils.TelephoneUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.CleanableEditText;
import com.niuyun.hire.view.TitleBar;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.niuyun.hire.R.id.title_view;


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



    @BindView(R.id.tv_next)
    TextView tvNext;

    @BindView(title_view)
    TitleBar titleView;

    @BindString(R.string.regist)
    String regist;

    @BindView(R.id.user_nick_name)
    CleanableEditText user_nick_name;
    @BindView(R.id.user_password)
    CleanableEditText user_password;


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
    protected View isNeedLec() {
        return null;
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
        if (!NetUtil.isNetworkConnected(this)) {
            UIUtil.showToast("网络连接失败，请检查您的网络");
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
        if (TextUtils.isEmpty(user_nick_name.getText())) {
            UIUtil.showToast("昵称不能为空");
            return;
        }
        if (TextUtils.isEmpty(user_password.getText())) {
            UIUtil.showToast("密码不能为空");
            return;
        }

//        final String tel = etPhone.getText().toString();
//        final String code = etCode.getText().toString();
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", etPhone.getText().toString());
//        map.put("checkCode", etCode.getText().toString());
        map.put("password", user_password.getText().toString());
        map.put("utype", "2");
        map.put("regType", "1");
        call = RestAdapterManager.getApi().reister(map);
        call.enqueue(new JyCallBack<String>() {
            @Override
            public void onSuccess(Call<String> call, Response<String> response) {
                try {

                    if (response != null && response.body() != null && !TextUtils.isEmpty(response.body())) {
                        if (response.body().contains("注册成功")) {
                            Intent findPsIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                            timer.cancel();
                            findPsIntent.putExtra("phone", etPhone.getText().toString());
                            findPsIntent.putExtra("pwd", user_password.getText().toString());
                            startActivity(findPsIntent);
                        }
                        ErrorMessageUtils.taostErrorMessage(RegisterActivity.this, response.body(), "");
                    } else {
                        UIUtil.showToast("注册失败");
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onError(Call<String> call, Throwable t) {
                UIUtil.showToast("注册失败");
            }

            @Override
            public void onError(Call<String> call, Response<String> response) {
                try {
                    ErrorMessageUtils.taostErrorMessage(RegisterActivity.this, response.errorBody().string(), "");
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

        }
    }
}
