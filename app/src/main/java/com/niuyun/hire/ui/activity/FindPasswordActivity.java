package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;


/**
 * 找回密码
 */
public class FindPasswordActivity extends BaseActivity {

    @BindView(R.id.title_view)
    TitleBar titleView;
    CountDownTimer timer;
    @BindView(R.id.user_name)
    CleanableEditText etPhone;
    @BindView(R.id.et_check_code)
    EditText etCode;
    @BindView(R.id.tv_check_code)
    TextView tvGetCode;
    @BindView(R.id.user_password)
    CleanableEditText user_password;
    @BindView(R.id.user_again_password)
    CleanableEditText user_again_password;
    @BindView(R.id.tv_next)
    Button tvNext;
    private Call<ErrorBean> call;
    boolean isCodeSended = false;
    Call<ErrorBean> getCheckCodeCall;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.mine_findpassword_activity_layout;
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

    private void initTitle() {
        titleView.setTitle("找回密码");
        titleView.setTitleColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.color_ea0000));
        titleView.setLeftImageResource(R.mipmap.ic_title_back);
        titleView.setLeftText("返回");
        titleView.setLeftTextColor(Color.WHITE);
        titleView.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleView.setImmersive(true);
    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean isRegistEventBus() {
        return true;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {
        if (null != eventBusCenter) {

        }
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


    private void getCode(String phone) {
        timer.start();
        getCheckCodeCall = RestAdapterManager.getApi().getCheckCode(phone);
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

        if (TextUtils.isEmpty(etCode.getText())) {
            UIUtil.showToast("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(user_password.getText())) {
            UIUtil.showToast("密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(user_again_password.getText())) {
            UIUtil.showToast("密码不能为空");
            return;
        }
        if (!user_again_password.getText().equals(user_again_password.getText())) {
            UIUtil.showToast("密码不一致");
            return;
        }
        commitData();
    }

    private void commitData() {
        Map<String, String> map = new HashMap<>();
        map.put("checkCode", etCode.getText().toString());
        map.put("newPwd", user_password.getText().toString());
        map.put("phone", etPhone.getText().toString());
        call = RestAdapterManager.getApi().commitNewPassword(map);
        call.enqueue(new JyCallBack<ErrorBean>() {
            @Override
            public void onSuccess(Call<ErrorBean> call, Response<ErrorBean> response) {
                if (response != null && response.body() != null) {
                    if (response.body().code == 1000) {
                        UIUtil.showToast(response.body().msg);
                        finish();
                    } else {
                        UIUtil.showToast("修改密码失败~请稍后重试");
                    }
                } else {
                    UIUtil.showToast("修改密码失败~请稍后重试");
                }
            }

            @Override
            public void onError(Call<ErrorBean> call, Throwable t) {
                UIUtil.showToast("修改密码失败~请稍后重试");
            }

            @Override
            public void onError(Call<ErrorBean> call, Response<ErrorBean> response) {
                try {
                    ErrorMessageUtils.taostErrorMessage(FindPasswordActivity.this, response.errorBody().string(), "");
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

}
