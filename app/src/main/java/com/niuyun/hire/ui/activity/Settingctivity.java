package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindView;

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
                BaseContext.getInstance().Exit();
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
//                startActivity(new Intent(this, UpdatePasswordActivity.class));
                break;
        }
    }
}
