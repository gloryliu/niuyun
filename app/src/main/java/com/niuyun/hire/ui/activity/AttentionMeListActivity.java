package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.AttentionMeListAdapter;
import com.niuyun.hire.ui.bean.AttentionMeBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.view.TitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-8-10.
 */

public class AttentionMeListActivity extends BaseActivity {

    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int pageNum = 1;
    private int pageSize = 20;
    private Call<AttentionMeBean> getAttentionMeCall;
    private AttentionMeListAdapter listItemAdapter;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_attention_list;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        //下拉刷新
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getAllJobs();
            }
        });
//        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                pageNum += 1;
//                getAllJobs();
//            }
//        });
        refreshLayout.setEnableLoadmore(false);
        //求职recyclerView
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        listItemAdapter = new AttentionMeListAdapter(this);
        recyclerview.setAdapter(listItemAdapter);
        listItemAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                AttentionMeBean.DataBean databean = (AttentionMeBean.DataBean) bean;
                if (databean != null) {
                    Intent intent = new Intent(AttentionMeListActivity.this, WorkPositionDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("companyId", databean.getCompanyId() + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void loadData() {
        getAllJobs();
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

    private void initTitle() {
        titleView.setTitle("谁看过我");
        titleView.setTitleColor(Color.WHITE);
        titleView.setLeftImageResource(R.mipmap.ic_title_back);
        titleView.setLeftText("返回");
        titleView.setLeftTextColor(Color.WHITE);
        titleView.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setActionTextColor(Color.WHITE);
        titleView.setImmersive(true);
    }

    private void getAllJobs() {

        getAttentionMeCall = RestAdapterManager.getApi().getAttentionMe(BaseContext.getInstance().getUserInfo().uid+"");

        getAttentionMeCall.enqueue(new JyCallBack<AttentionMeBean>() {
            @Override
            public void onSuccess(Call<AttentionMeBean> call, Response<AttentionMeBean> response) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                }
                if (pageNum == 1) {
                    listItemAdapter.ClearData();
                }
                if (response != null && response.body() != null && response.body().getData() != null && response.body().getCode() == Constants.successCode) {
                    listItemAdapter.addList(response.body().getData());
//                    if (pageNum >= response.body().getData().getPageCount()) {
//                        refreshLayout.setEnableLoadmore(false);
//                    } else {
//                        refreshLayout.setEnableLoadmore(true);
//                    }
                } else {
                    if (pageNum == 1) {
                        //无数据

                    } else {
                        //加载完全部数据
                    }
                }

            }

            @Override
            public void onError(Call<AttentionMeBean> call, Throwable t) {
                if (refreshLayout != null) {
//                    if (pageNum == 1) {
                        listItemAdapter.ClearData();
//                    }
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                }
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<AttentionMeBean> call, Response<AttentionMeBean> response) {
                if (refreshLayout != null) {
//                    if (pageNum == 1) {
                        listItemAdapter.ClearData();
//                    }
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                }
                try {
                    LogUtils.e(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
