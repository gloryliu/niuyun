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
import com.niuyun.hire.ui.adapter.FindAroundListItemAdapter;
import com.niuyun.hire.ui.bean.AroundResultBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.view.TitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-9-1.
 */

public class FindAroundActivity extends BaseActivity {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private FindAroundListItemAdapter listItemAdapter;
    private Call<AroundResultBean> AroundResultBeanCall;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_find_around;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        //下拉刷新
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getAllJobs();
            }
        });
        refreshLayout.setEnableLoadmore(false);
        //求职recyclerView
        recyclerview.setLayoutManager(new LinearLayoutManager(FindAroundActivity.this));
        listItemAdapter = new FindAroundListItemAdapter(FindAroundActivity.this);
        recyclerview.setAdapter(listItemAdapter);
        listItemAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                AroundResultBean.DataBean databean = (AroundResultBean.DataBean) bean;
                if (databean != null) {
                    Intent intent = new Intent(FindAroundActivity.this, CompanyDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", databean.getCompanyId() + "");
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

    private void getAllJobs() {
        Map<String, String> map = new HashMap<>();

        map.put("lookType", "0");
        map.put("mapX", BaseContext.getInstance().getLocationInfo().latitude + "");
        map.put("mapY", BaseContext.getInstance().getLocationInfo().longitude + "");
        AroundResultBeanCall = RestAdapterManager.getApi().getAround(map);


        AroundResultBeanCall.enqueue(new JyCallBack<AroundResultBean>() {
            @Override
            public void onSuccess(Call<AroundResultBean> call, Response<AroundResultBean> response) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                listItemAdapter.ClearData();
                if (response != null && response.body() != null && response.body().getData() != null && response.body().getCode() == Constants.successCode) {
                    listItemAdapter.addList(response.body().getData());
                }

            }

            @Override
            public void onError(Call<AroundResultBean> call, Throwable t) {
                if (refreshLayout != null) {
                    listItemAdapter.ClearData();
                    refreshLayout.finishRefresh();
                }
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<AroundResultBean> call, Response<AroundResultBean> response) {
                if (refreshLayout != null) {
                    listItemAdapter.ClearData();
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

    private void initTitle() {

        titleView.setTitle("发现");
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
        titleView.addAction(new TitleBar.TextAction("···") {
            @Override
            public void performAction(View view) {

            }
        });
        titleView.setActionTextColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setImmersive(true);
    }
}
