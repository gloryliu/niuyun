package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.bean.ErrorBean;
import com.niuyun.hire.ui.utils.LoginUtils;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.NetUtil;
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

import static com.niuyun.hire.R.id.title_view;


/**
 * 更换密码
 */
public class UpdatePasswordActivity extends BaseActivity {

    @BindView(title_view)
    TitleBar titleView;
    //    CountDownTimer timer;
    @BindView(R.id.user_name)
    CleanableEditText etPhone;
    @BindView(R.id.et_check_code)
    EditText etCode;
    @BindView(R.id.user_new_pass)
    CleanableEditText user_new_pass;
    @BindView(R.id.tv_next)
    Button tvNext;
    private Call<ErrorBean> call;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.mine_uodate_password_activity_layout;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        etCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});


    }

    private void initTitle() {
        titleView.setTitle("修改密码");
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
        titleView.setActionTextColor(Color.WHITE);
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


    @OnClick(R.id.tv_next)
    public void onNextClick(View view) {
        if (!NetUtil.isNetworkConnected(this)) {
            UIUtil.showToast("网络连接失败，请检查您的网络");
            return;
        }

        String phoneNumber = etPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            UIUtil.showToast("请输入原密码");
            return;
        }

        if (TextUtils.isEmpty(etCode.getText())) {
            UIUtil.showToast("新密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(user_new_pass.getText())) {
            UIUtil.showToast("新密码不能为空");
            return;
        }
        if (!user_new_pass.getText().toString().equals(etCode.getText().toString())) {
            UIUtil.showToast("新密码不一致");
            return;
        }
        commitData();
    }

    private void commitData() {
        DialogUtils.showDialog(this, "", false);
        Map<String, String> map = new HashMap<>();
        map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
        map.put("newPwd", user_new_pass.getText().toString());
        map.put("oldPwd", etPhone.getText().toString());
        call = RestAdapterManager.getApi().updatePassword(map);
        call.enqueue(new JyCallBack<ErrorBean>() {
            @Override
            public void onSuccess(Call<ErrorBean> call, Response<ErrorBean> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null) {
                    if (response.body().code == 1000) {
                        UIUtil.showToast(response.body().msg);
                        LoginUtils.getUserByUid();
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
                DialogUtils.closeDialog();
                UIUtil.showToast("修改密码失败~请稍后重试");
            }

            @Override
            public void onError(Call<ErrorBean> call, Response<ErrorBean> response) {
                try {
                    DialogUtils.closeDialog();
                    ErrorMessageUtils.taostErrorMessage(UpdatePasswordActivity.this, response.errorBody().string(), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
//        timer.cancel();
        if (call != null) {
            call.cancel();
        }
        super.onDestroy();
    }

}
