package com.niuyun.hire.ui.activity;

import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.EventBusCenter;

/**
 * Created by chen.zhiwei on 2017-7-31.
 */

public class PerfectEnterpriseInformation extends BaseActivity{
    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_perfect_enterprise_information;
    }

    @Override
    public void initViewsAndEvents() {

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
}
