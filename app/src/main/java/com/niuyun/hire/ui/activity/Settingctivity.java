package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.niuyun.hire.R;
import com.niuyun.hire.api.JyApi;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UpdateBean;
import com.niuyun.hire.utils.ApkUpdateManager;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.MyDeviceInfo;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.TitleBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/12.
 */

public class Settingctivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.ll_quit_login)
    LinearLayout ll_quit_login;
    @BindView(R.id.rl_about_we)
    RelativeLayout rl_about_we;
    @BindView(R.id.rl_change_pass)
    RelativeLayout rl_change_pass;
    @BindView(R.id.rl_change_phone)
    RelativeLayout rl_change_phone;
    @BindView(R.id.rl_clear)
    RelativeLayout rl_clear;
    @BindView(R.id.tv_version_number)
    TextView tv_version_number;
    @BindView(R.id.tv_version_notice)
    TextView tv_version_notice;
    @BindView(R.id.tv_new)
    TextView tv_new;
    @BindView(R.id.rl_check_version)
    RelativeLayout rl_check_version;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_setting_layout;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        ll_quit_login.setOnClickListener(this);
        rl_about_we.setOnClickListener(this);
        rl_change_pass.setOnClickListener(this);
        rl_change_phone.setOnClickListener(this);
        rl_clear.setOnClickListener(this);
        rl_check_version.setOnClickListener(this);
        tv_version_number.setText("当前版本V" + MyDeviceInfo.getLocalVersionName());

    }

    @Override
    public void loadData() {
        getVersion();
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

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("设置");
        title_view.setTitleColor(Color.WHITE);
        title_view.setLeftImageResource(R.mipmap.ic_title_back);
        title_view.setLeftText("返回");
        title_view.setLeftTextColor(Color.WHITE);
        title_view.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title_view.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        title_view.setActionTextColor(Color.WHITE);
        title_view.setImmersive(true);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_quit_login:
                showExitDialog();
                break;
            case R.id.rl_about_we:
                startActivity(new Intent(this, AboutWeActivity.class));
                break;
            case R.id.rl_change_pass:
                startActivity(new Intent(this, UpdatePasswordActivity.class));
                break;
            case R.id.rl_change_phone:
                startActivity(new Intent(this, UpdatePhoneActivity.class));
                break;
            case R.id.rl_clear:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(Settingctivity.this).clearMemory();
                        Glide.get(Settingctivity.this).clearDiskCache();
                    }
                });

                try {
                    rl_clear.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            UIUtil.showToast("已清理完毕");
                        }
                    }, 1000);
                } catch (Exception e) {
                }
                break;
            case R.id.rl_check_version:
                ApkUpdateManager.getInstance().checkVersion(this, true);
                break;
        }
    }

    private void showExitDialog() {
        DialogUtils.showOrderCancelMsg(this, "确定要退出登录吗？", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag().equals("确定")) {
                    BaseContext.getInstance().Exit();
                    Intent i = new Intent(Settingctivity.this, LoginActivity.class);
                    startActivity(i);
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.LOGIN_FAILURE));
                }

            }

//            @Override
//            public void callBack() {//退出登录
//
//            }
        });
    }

    private void getVersion() {
        JyApi jyApi = RestAdapterManager.getApi();
        Call<SuperBean<UpdateBean>> call = jyApi.checkVersion("Android", "niuyunAPP", MyDeviceInfo.getLocalVersionCode() + "");
        call.enqueue(new JyCallBack<SuperBean<UpdateBean>>() {
            @Override
            public void onSuccess(Call<SuperBean<UpdateBean>> call, Response<SuperBean<UpdateBean>> response) {
                try {
                    UpdateBean updateInfo = response.body().getData();
                    if (updateInfo != null && !TextUtils.isEmpty(updateInfo.version)) {
                        if (Integer.parseInt(updateInfo.version) > MyDeviceInfo.getLocalVersionCode()) {
                            tv_new.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onError(Call<SuperBean<UpdateBean>> call, Throwable t) {

            }

            @Override
            public void onError(Call<SuperBean<UpdateBean>> call, Response<SuperBean<UpdateBean>> response) {

            }
        });
    }
}
