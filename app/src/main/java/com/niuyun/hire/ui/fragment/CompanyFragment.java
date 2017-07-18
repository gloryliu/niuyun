package com.niuyun.hire.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.GalleryAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by chen.zhiwei on 2017-7-18.
 */

public class CompanyFragment extends BaseFragment {
    @BindView(R.id.tag_recyclerview)
    RecyclerView tag_recyclerview;
    private GalleryAdapter mAdapter;
    private List<String> mDatas;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_company_layout;
    }

    @Override
    protected void initViewsAndEvents() {
        //标签recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tag_recyclerview.setLayoutManager(linearLayoutManager);
//设置适配器
        mDatas.add("Android");
        mDatas.add("Android");
        mDatas.add("Android");
        mAdapter = new GalleryAdapter(getActivity(), mDatas);
        tag_recyclerview.setAdapter(mAdapter);
        //        refreshLayout.setEnableHeaderTranslationContent(false);
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000);
//            }
//        });
//        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadmore(2000);
//            }
//        });
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
