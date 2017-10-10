package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindString;
import butterknife.BindView;

/**
 * 选择注册角色
 * <p>
 * Created by chen.zhiwei on 2017-7-18.
 */

public class SelectedRegisterRoler extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindString(R.string.regist)
    String regist;
    @BindView(R.id.tv_person_register)
    TextView tv_person_register;
    @BindView(R.id.tv_enterprise_register)
    TextView tv_enterprise_register;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_selected_register;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        tv_person_register.setOnClickListener(this);
        tv_enterprise_register.setOnClickListener(this);
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

    private void initTitle() {

        titleView.setTitle(regist);
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
            case R.id.tv_person_register:
                //个人注册
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.tv_enterprise_register:
                //企业注册
                startActivity(new Intent(this, EnterpriseRegisterActivity.class));
                finish();
                break;
        }
    }
}
