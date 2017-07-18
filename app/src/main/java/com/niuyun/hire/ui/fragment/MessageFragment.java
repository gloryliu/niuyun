package com.niuyun.hire.ui.fragment;

import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.EventBusCenter;

/**
 * Created by chen.zhiwei on 2017-7-18.
 */

public class MessageFragment extends BaseFragment{
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_message_layout;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    @Override
    public boolean isRegistEventBus() {
        return false;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {

    }
}
