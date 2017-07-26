package com.niuyun.hire.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.activity.WorkPositionDetailActivity;
import com.niuyun.hire.ui.adapter.IndexCompanyListItemAdapter;
import com.niuyun.hire.ui.bean.AllJobsBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.LogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-7-26.
 */

public class CompanyHotPositionFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int pageNum = 1;
    private int pageSize = 20;
    private IndexCompanyListItemAdapter listItemAdapter;
    private Call<AllJobsBean> allJobsBeanCall;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_company_hot_position_layout;
    }

    @Override
    protected void initViewsAndEvents() {
//下拉刷新
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getAllJobs();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum += 1;
                getAllJobs();
            }
        });
        refreshLayout.setEnableLoadmore(false);
        //求职recyclerView
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItemAdapter = new IndexCompanyListItemAdapter(getActivity());
        recyclerview.setAdapter(listItemAdapter);
        listItemAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                AllJobsBean.DataBeanX.DataBean databean = (AllJobsBean.DataBeanX.DataBean) bean;
                if (databean != null) {
                    Intent intent = new Intent(getActivity(), WorkPositionDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", databean.getId() + "");
                    bundle.putString("companyId", databean.getCompanyId() + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected void loadData() {
        getAllJobs();
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

    private void getAllJobs() {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", pageSize + "");

        allJobsBeanCall = RestAdapterManager.getApi().getAllJobs(map);

        allJobsBeanCall.enqueue(new JyCallBack<AllJobsBean>() {
            @Override
            public void onSuccess(Call<AllJobsBean> call, Response<AllJobsBean> response) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                if (pageNum == 1) {
                    listItemAdapter.ClearData();
                }
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    listItemAdapter.addList(response.body().getData().getData());
                    if (pageNum >= response.body().getData().getPageCount()) {
                        refreshLayout.setEnableLoadmore(false);
                    } else {
                        refreshLayout.setEnableLoadmore(true);
                    }
                } else {
                    if (pageNum == 1) {
                        //无数据

                    } else {
                        //加载完全部数据
                    }
                }

            }

            @Override
            public void onError(Call<AllJobsBean> call, Throwable t) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<AllJobsBean> call, Response<AllJobsBean> response) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                try {
                    LogUtils.e(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (allJobsBeanCall != null) {
            allJobsBeanCall.cancel();
        }
    }
}
