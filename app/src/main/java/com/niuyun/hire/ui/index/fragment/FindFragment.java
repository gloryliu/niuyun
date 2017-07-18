package com.niuyun.hire.ui.index.fragment;

import android.graphics.Color;
import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindString;
import butterknife.BindView;

/**
 * Created by chen.zhiwei on 2017-7-18.
 */

public class FindFragment extends BaseFragment {
    @BindView(R.id.title_view)
    TitleBar titleView;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_find_layout;
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

        titleView.setTitle("发现");
        titleView.setTitleColor(Color.WHITE);
//        titleView.setLeftImageResource(R.mipmap.ic_title_back);
//        titleView.setLeftText("返回");
//        titleView.setLeftTextColor(Color.WHITE);
//        titleView.setLeftClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setImmersive(true);
    }
}
