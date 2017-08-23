package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.EnterprisePublishedPisitionAdapter;
import com.niuyun.hire.ui.bean.EnterprisePublishedPositionBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.view.TitleBar;
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
 * Created by chen.zhiwei on 2017-8-22.
 */

public class EnterprisePositionControlActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.bt_publish)
    Button bt_publish;
    private int pageNum = 1;
    private int pageSize = 20;
    private Call<EnterprisePublishedPositionBean> getMyAttentionCall;
    private EnterprisePublishedPisitionAdapter listItemAdapter;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.enterprise_activity_position_control;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        bt_publish.setOnClickListener(this);
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
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        listItemAdapter = new EnterprisePublishedPisitionAdapter(this);
        recyclerview.setAdapter(listItemAdapter);
        listItemAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                EnterprisePublishedPositionBean.DataBeanX.DataBean databean = (EnterprisePublishedPositionBean.DataBeanX.DataBean) bean;
                if (databean != null) {
                    Intent intent = new Intent(EnterprisePositionControlActivity.this, EnterprisePositionEditActivity.class);
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
    public void loadData() {
        getAllJobs();
    }

    @Override
    public boolean isRegistEventBus() {
        return true;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {
        if (eventBusCenter != null) {
            if (eventBusCenter.getEvenCode() == Constants.UPDATE_PUBLISHED_POSITION) {
                pageNum = 1;
                getAllJobs();
            }
        }
    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    private void getAllJobs() {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", pageSize + "");
        map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");

        getMyAttentionCall = RestAdapterManager.getApi().getMyPublishedPosition(map);

        getMyAttentionCall.enqueue(new JyCallBack<EnterprisePublishedPositionBean>() {
            @Override
            public void onSuccess(Call<EnterprisePublishedPositionBean> call, Response<EnterprisePublishedPositionBean> response) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                }
                if (pageNum == 1) {
                    listItemAdapter.ClearData();
                }
                if (response != null && response.body() != null && response.body().getData() != null && response.body().getCode() == Constants.successCode) {
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
            public void onError(Call<EnterprisePublishedPositionBean> call, Throwable t) {
                if (refreshLayout != null) {
                    if (pageNum == 1) {
                        listItemAdapter.ClearData();
                    }
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                }
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<EnterprisePublishedPositionBean> call, Response<EnterprisePublishedPositionBean> response) {
                if (refreshLayout != null) {
                    if (pageNum == 1) {
                        listItemAdapter.ClearData();
                    }
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

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("职位管理");
        title_view.setTitleColor(Color.WHITE);
//        title_view.setLeftImageResource(R.mipmap.ic_title_back);
//        title_view.setLeftText("返回");
//        title_view.setLeftTextColor(Color.WHITE);
//        title_view.setLeftClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        title_view.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        title_view.setImmersive(true);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_publish:
                startActivity(new Intent(this, EnterprisePublishPositionActivity.class));
                break;
        }
    }
}
