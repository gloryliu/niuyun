package com.niuyun.hire.ui.index.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.activity.DianboDetailActivity;
import com.niuyun.hire.ui.activity.LoginActivity;
import com.niuyun.hire.ui.adapter.IndexLiveListItemAdapter2;
import com.niuyun.hire.ui.bean.DianboListBean;
import com.niuyun.hire.ui.bean.LiveListBean;
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

import static com.niuyun.hire.R.id.title_view;

/**
 * Created by chen.zhiwei on 2017-7-18.
 */

public class LiveFragment2 extends BaseFragment implements View.OnClickListener {
    @BindView(title_view)
    TitleBar titleView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.bt_go_live)
    ImageButton bt_go_live;
    private IndexLiveListItemAdapter2 listItemAdapter;
    private String uid = "fb3a182807";
    private String channelId = "128591";
    private String password = "128591";
    private Call<DianboListBean> listBeanCall;
    private int pageNum = 1;
    private int pageSize = 20;
    private LiveListBean liveListBean;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_live_layout;
    }

    @Override
    protected void initViewsAndEvents() {
        initTitle();
        bt_go_live.setOnClickListener(this);
        //下拉刷新
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getList();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum += 1;
                getList();
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItemAdapter = new IndexLiveListItemAdapter2(getActivity());
        recyclerview.setAdapter(listItemAdapter);
        listItemAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {

                if (bean != null) {
                    DianboListBean.DataBeanX.DataBean dataBean = (DianboListBean.DataBeanX.DataBean) bean;
//                    channelId = dataBean.getChannelId() + "";
//                    uid = dataBean.getUserId() + "";
                        Intent intent = new Intent(getActivity(), DianboDetailActivity.class);
                        intent.putExtra("bean", dataBean);
                        startActivity(intent);

                }

            }
        });
        setButtonVisible();
    }

    @Override
    protected void loadData() {
        getList();
    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    @Override
    public boolean isRegistEventBus() {
        return true;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {
        if (eventBusCenter != null) {
            if (eventBusCenter.getEvenCode() == Constants.LOGIN_SUCCESS) {
                setButtonVisible();

            }
        }
    }

    private void setButtonVisible() {
        if (BaseContext.getInstance().getUserInfo() != null) {
            if (BaseContext.getInstance().getUserInfo().utype == 1) {
                bt_go_live.setVisibility(View.GONE);
            } else {
                bt_go_live.setVisibility(View.GONE);
            }
        } else {
            bt_go_live.setVisibility(View.GONE);
        }
    }

    private void getList() {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", pageSize + "");
        listBeanCall = RestAdapterManager.getApi().geDianboList(map);
        listBeanCall.enqueue(new JyCallBack<DianboListBean>() {
            @Override
            public void onSuccess(Call<DianboListBean> call, Response<DianboListBean> response) {
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
            public void onError(Call<DianboListBean> call, Throwable t) {
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
            public void onError(Call<DianboListBean> call, Response<DianboListBean> response) {
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

    private void initTitle() {

        titleView.setTitle("直播");
        titleView.setTitleColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_go_live:
                //我要直播
                if (BaseContext.getInstance().getUserInfo() != null) {
//                    Intent intent = new Intent(getActivity(), PolyvSettingActivity.class);
//                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }

                break;

        }
    }

}
