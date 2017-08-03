package com.niuyun.hire.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.EventBusCenter;

import butterknife.BindView;

/**
 * Created by chen.zhiwei on 2017-7-26.
 */

public class CompanyHomePageFragment extends BaseFragment {
    @BindView(R.id.tv_content)
    TextView tv_content;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_company_home_page_layout;
    }

    @Override
    protected void initViewsAndEvents() {
        Bundle bundle=getArguments();
        if (bundle!=null&&tv_content!=null){
            tv_content.setText(bundle.getString("content"));
        }
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
