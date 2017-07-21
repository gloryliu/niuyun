package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindView;

/**
 * Created by chen.zhiwei on 2017-7-21.
 */

public class PerfectPersonInformation extends BaseActivity {
    @BindView(R.id.title_view)
    TitleBar titleView;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_perfect_person_information;
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

        titleView.setTitle("完善信息");
        titleView.setTitleColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setImmersive(true);
    }
}
