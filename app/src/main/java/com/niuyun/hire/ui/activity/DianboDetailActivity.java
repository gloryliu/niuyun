package com.niuyun.hire.ui.activity;

import android.app.FragmentTransaction;
import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.DianboListBean;
import com.niuyun.hire.ui.live.common.widget.CommonLivePlayerView;

import butterknife.BindView;

/**
 * Created by chen.zhiwei on 2017-9-29.
 */

public class DianboDetailActivity extends BaseActivity {
    @BindView(R.id.pv_play)
    CommonLivePlayerView pv_play;
    private DianboListBean.DataBeanX.DataBean dataBean;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_layout_dianbo_detail;
    }

    @Override
    public void initViewsAndEvents() {
        dataBean = (DianboListBean.DataBeanX.DataBean) getIntent().getSerializableExtra("bean");
    }

    @Override
    public void loadData() {
        if (dataBean != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            pv_play.setTransaction(ft);
            pv_play.setmCoverImagePath(dataBean.getImageUrl());
            pv_play.setmVideoPath(dataBean.getVideoUrl());
        }
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
