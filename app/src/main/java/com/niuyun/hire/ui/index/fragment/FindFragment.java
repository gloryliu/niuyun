package com.niuyun.hire.ui.index.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.activity.FindAroundActivity;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindView;

/**
 * Created by chen.zhiwei on 2017-7-18.
 */

public class FindFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.ll_find_around)
    LinearLayout ll_find_around;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_find_layout;
    }

    @Override
    protected void initViewsAndEvents() {
        initTitle();
        ll_find_around.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_find_around:
                if (BaseContext.getInstance().getLocationInfo() != null) {
                    startActivity(new Intent(getActivity(), FindAroundActivity.class));
                }
                break;
        }
    }
}
