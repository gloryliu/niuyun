package com.niuyun.hire.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.niuyun.hire.ui.activity.ControlPositionIntentActivity;
import com.niuyun.hire.ui.activity.LoginActivity;
import com.niuyun.hire.ui.activity.WorkPositionDetailActivity;
import com.niuyun.hire.ui.adapter.GalleryAdapter;
import com.niuyun.hire.ui.adapter.IndexCompanyListItemAdapter;
import com.niuyun.hire.ui.bean.AllJobsBean;
import com.niuyun.hire.ui.bean.PositionIntentBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 意向职位页面
 * Created by chen.zhiwei on 2017-7-18.
 */

public class CompanyFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.tag_recyclerview)
    RecyclerView tag_recyclerview;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ivb_add_intent)
    ImageButton ivb_add_intent;
    private Call<PositionIntentBean> gePositionIntentList;
    private IndexCompanyListItemAdapter listItemAdapter;
    private GalleryAdapter mAdapter;
    private List<String> mDatas = new ArrayList<>();
    private Call<AllJobsBean> allJobsBeanCall;
    private int pageNum = 1;
    private int pageSize = 20;
    private PositionIntentBean.DataBean clickCacheFilterBean;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_company_layout;
    }

    @Override
    protected void initViewsAndEvents() {
        ivb_add_intent.setOnClickListener(this);
        //标签recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tag_recyclerview.setLayoutManager(linearLayoutManager);
//设置适配器
        mAdapter = new GalleryAdapter(getActivity());
        tag_recyclerview.setAdapter(mAdapter);
        mAdapter.setIntentTagClickListerner(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                listItemAdapter.ClearData();
                clickCacheFilterBean = (PositionIntentBean.DataBean) bean;
                pageNum = 1;
                getAllJobs();
            }

        });
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
                    bundle.putString("uid", databean.getUid() + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    protected void loadData() {
        getIntentList();
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
            if (eventBusCenter.getEvenCode() == Constants.UPDATE_POSITION_INTENT) {
                getIntentList();
            } else if (eventBusCenter.getEvenCode() == Constants.LOGIN_SUCCESS) {
                getIntentList();
            }
        }

    }

    /**
     * 设置默认值
     */
    private void initIntent() {
        if (mAdapter.getmDatas() != null && mAdapter.getmDatas().size() > 0) {

            clickCacheFilterBean = mAdapter.getmDatas().get(0);
            pageNum = 1;
            getAllJobs();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivb_add_intent:
                if (BaseContext.getInstance().getUserInfo() != null) {

                    startActivity(new Intent(getActivity(), ControlPositionIntentActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
        }
    }

    /**
     * 求职意向列表
     */
    private void getIntentList() {
        if (BaseContext.getInstance().getUserInfo() == null) {
//            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        gePositionIntentList = RestAdapterManager.getApi().gePositionIntentList(BaseContext.getInstance().getUserInfo().uid + "");
        gePositionIntentList.enqueue(new JyCallBack<PositionIntentBean>() {
            @Override
            public void onSuccess(Call<PositionIntentBean> call, Response<PositionIntentBean> response) {
                if (response.body() != null && response.body().getCode() == Constants.successCode) {
                    mAdapter.ClearData();
                    mAdapter.addList(response.body().getData());
                    initIntent();
                } else {
                    UIUtil.showToast(response.body().getMsg());
                }
            }

            @Override
            public void onError(Call<PositionIntentBean> call, Throwable t) {
                UIUtil.showToast("接口异常");
            }

            @Override
            public void onError(Call<PositionIntentBean> call, Response<PositionIntentBean> response) {
                UIUtil.showToast("接口异常");
            }
        });
    }

    private void getAllJobs() {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", pageSize + "");

        map.put("jobsName", clickCacheFilterBean.getJobsName());
        map.put("trade", clickCacheFilterBean.getTrade() + "");
        map.put("tradeCn", clickCacheFilterBean.getTradeCn());
        map.put("wage", clickCacheFilterBean.getWage() + "");
        map.put("wageCn", clickCacheFilterBean.getWageCn());
        map.put("district", clickCacheFilterBean.getDistrict() + "");
        map.put("districtCn", clickCacheFilterBean.getDistrictCn());
        map.put("id", clickCacheFilterBean.getId() + "");
        allJobsBeanCall = RestAdapterManager.getApi().getFilterJobs(map);


        allJobsBeanCall.enqueue(new JyCallBack<AllJobsBean>() {
            @Override
            public void onSuccess(Call<AllJobsBean> call, Response<AllJobsBean> response) {
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
            public void onError(Call<AllJobsBean> call, Throwable t) {
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
            public void onError(Call<AllJobsBean> call, Response<AllJobsBean> response) {
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

}
