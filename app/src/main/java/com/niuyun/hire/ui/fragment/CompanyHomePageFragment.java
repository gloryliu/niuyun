package com.niuyun.hire.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.utils.UIUtil;

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
        tv_content .setText("哈哈哈哈哈哈哈");

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

    public void upDate(String content) {
        UIUtil.showToast(content);
        if (tv_content != null) {

            tv_content.setText(content);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if (bundle!=null){
            tv_content.setText(bundle.getString("content"));
        }
    }

}
