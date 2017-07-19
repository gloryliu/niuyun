package com.niuyun.hire.ui.index.fragment;

import android.graphics.Color;
import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindView;

import static com.niuyun.hire.R.id.title_view;

/**
 * Created by chen.zhiwei on 2017-7-18.
 */

public class LiveFragment extends BaseFragment{
    @BindView(title_view)
    TitleBar titleView;
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_live_layout;
    }

    @Override
    protected void initViewsAndEvents() {
        initTitle();
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
    private void initTitle() {

        titleView.setTitle("直播");
        titleView.setTitleColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setImmersive(true);
    }
}
