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
import com.niuyun.hire.ui.activity.PreviewResumeActivity;
import com.niuyun.hire.ui.activity.WorkPositionDetailActivity;
import com.niuyun.hire.ui.adapter.GalleryAdapter;
import com.niuyun.hire.ui.adapter.IndexCompanyListItemAdapter;
import com.niuyun.hire.ui.adapter.IndexSeekPersonListItemAdapter;
import com.niuyun.hire.ui.adapter.PublishedPositionGalleryAdapter;
import com.niuyun.hire.ui.bean.AllJobsBean;
import com.niuyun.hire.ui.bean.EnterpriseFindPersonBean;
import com.niuyun.hire.ui.bean.EnterprisePublishedPositionBean;
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
 * 意向人才页面
 * Created by chen.zhiwei on 2017-7-18.
 */

public class SeekPersonFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.tag_recyclerview)
    RecyclerView tag_recyclerview;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ivb_add_intent)
    ImageButton ivb_add_intent;
    private Call<PositionIntentBean> gePositionIntentList;
    private IndexSeekPersonListItemAdapter listItemAdapter;
    private PublishedPositionGalleryAdapter mAdapter;
    private List<String> mDatas = new ArrayList<>();
    private Call<EnterpriseFindPersonBean> allJobsBeanCall;
    private int pageNum = 1;
    private int pageSize = 20;
    private EnterprisePublishedPositionBean.DataBeanX.DataBean clickCacheFilterBean;
    private Call<EnterprisePublishedPositionBean> getMyAttentionCall;
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
        mAdapter = new PublishedPositionGalleryAdapter(getActivity());
        tag_recyclerview.setAdapter(mAdapter);
        mAdapter.setIntentTagClickListerner(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                listItemAdapter.ClearData();
                clickCacheFilterBean = (EnterprisePublishedPositionBean.DataBeanX.DataBean) bean;
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
        listItemAdapter = new IndexSeekPersonListItemAdapter(getActivity());
        recyclerview.setAdapter(listItemAdapter);
        listItemAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                EnterpriseFindPersonBean.DataBeanX.DataBean databean = (EnterpriseFindPersonBean.DataBeanX.DataBean) bean;
                if (databean != null) {
                    Intent intent = new Intent(getActivity(), PreviewResumeActivity.class);
                    Bundle bundle = new Bundle();
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
            if (eventBusCenter.getEvenCode() == Constants.UPDATE_PUBLISHED_POSITION) {
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
        Map<String, String> map = new HashMap<>();
//        map.put("pageNum", pageNum + "");
//        map.put("pageSize", pageSize + "");
        map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");

        getMyAttentionCall = RestAdapterManager.getApi().getMyPublishedPosition(map);

        getMyAttentionCall.enqueue(new JyCallBack<EnterprisePublishedPositionBean>() {
            @Override
            public void onSuccess(Call<EnterprisePublishedPositionBean> call, Response<EnterprisePublishedPositionBean> response) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                }

                if (response != null && response.body() != null && response.body().getData() != null && response.body().getCode() == Constants.successCode) {
                    mAdapter.ClearData();
                    mAdapter.addList(response.body().getData().getData());
                    initIntent();
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
                        mAdapter.ClearData();
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                }
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<EnterprisePublishedPositionBean> call, Response<EnterprisePublishedPositionBean> response) {
                if (refreshLayout != null) {
                        mAdapter.ClearData();
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

    private void getAllJobs() {
        if (clickCacheFilterBean == null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", pageSize + "");

        map.put("intentionJobs", clickCacheFilterBean.getJobsName());
//        map.put("intentionJobsId", clickCacheFilterBean.getJobsName());
        map.put("categoryCn", clickCacheFilterBean.getCategoryCn());
//        map.put("wage", clickCacheFilterBean.get() + "");
//        map.put("wageCn", clickCacheFilterBean.getWageCn());
        map.put("education", clickCacheFilterBean.getEducation() + "");
        map.put("educationCn", clickCacheFilterBean.getEducationCn());
        map.put("experience", clickCacheFilterBean.getExperience() + "");
        map.put("experienceCn", clickCacheFilterBean.getExperienceCn());
        map.put("district", clickCacheFilterBean.getDistrict() + "");
        map.put("districtCn", clickCacheFilterBean.getDistrictCn());
        map.put("sdistrict", clickCacheFilterBean.getSdistrict()+"");
        map.put("tdistrict", clickCacheFilterBean.getTdistrict()+"");
        map.put("id", clickCacheFilterBean.getId() + "");
        allJobsBeanCall = RestAdapterManager.getApi().getPersonon(map);


        allJobsBeanCall.enqueue(new JyCallBack<EnterpriseFindPersonBean>() {
            @Override
            public void onSuccess(Call<EnterpriseFindPersonBean> call, Response<EnterpriseFindPersonBean> response) {
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
            public void onError(Call<EnterpriseFindPersonBean> call, Throwable t) {
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
            public void onError(Call<EnterpriseFindPersonBean> call, Response<EnterpriseFindPersonBean> response) {
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
