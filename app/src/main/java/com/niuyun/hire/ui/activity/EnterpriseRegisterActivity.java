package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindView;

import static com.niuyun.hire.R.id.title_view;

/**
 * 企业注册
 * Created by chen.zhiwei on 2017-7-19.
 */

public class EnterpriseRegisterActivity extends BaseActivity {
    @BindView(title_view)
    TitleBar titleView;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_enterprise_register_layout;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
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
}
