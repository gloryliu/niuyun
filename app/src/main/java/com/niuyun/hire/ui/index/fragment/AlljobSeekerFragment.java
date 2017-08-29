package com.niuyun.hire.ui.index.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.activity.AllPersonSearchActivity;
import com.niuyun.hire.ui.activity.PreviewResumeActivity;
import com.niuyun.hire.ui.adapter.CommonPerfectInfoTagAdapter;
import com.niuyun.hire.ui.adapter.IndexSeekPersonListItemAdapter;
import com.niuyun.hire.ui.adapter.JobPerfectInfoTagAdapter;
import com.niuyun.hire.ui.adapter.JobPerfectInfoTagAdapter1;
import com.niuyun.hire.ui.adapter.PoPuMenuListCommonAdapter;
import com.niuyun.hire.ui.bean.CommonTagBean;
import com.niuyun.hire.ui.bean.CommonTagItemBean;
import com.niuyun.hire.ui.bean.EnterpriseFindPersonBean;
import com.niuyun.hire.ui.bean.GetBaseTagBean;
import com.niuyun.hire.ui.bean.JobTagBean;
import com.niuyun.hire.ui.bean.SelectedBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.DropDownMenu;
import com.niuyun.hire.view.TitleBar;
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
 * Created by chen.zhiwei on 2017-8-23.
 */

public class AlljobSeekerFragment extends BaseFragment{
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private IndexSeekPersonListItemAdapter listItemAdapter;
    //筛选标题list
    private List<String> types = new ArrayList<>();
    //筛选view的list
    private List<View> popupViews = new ArrayList<>();
    private List<SelectedBean> demands = new ArrayList<>();
    private List<SelectedBean> demands1 = new ArrayList<>();
    private PoPuMenuListCommonAdapter mMenuAdapter;
    private PoPuMenuListCommonAdapter mMenuAdapter2;
    private ImageView mSearchView;
    private Call<EnterpriseFindPersonBean> allJobsBeanCall;
    private int pageNum = 1;
    private int pageSize = 20;


    private int jobStep = 0;

    private JobPerfectInfoTagAdapter adapter1;
    private JobPerfectInfoTagAdapter adapter2;
    private JobTagBean.DataBean cacheJobTag;

    private List<JobTagBean.DataBean> jobTagBean;

    private String education;//学历
    private String educationCn;

    private String experience;//经验
    private String experienceCn;


    private String wage;//期望薪资
    private String wageCn;

    private String intentionJobsId;//期望职位,每一级的id中间用 . 隔开
    private String intentionJobs;

    private String intentionJobsId1;
    private String intentionJobsId2;
    private String intentionJobsId3;

    private CommonTagBean commonTagBean;
    private String clickTag;
    private CommonTagItemBean cacheCommonTagBean;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_alljobs_layout;
    }

    @Override
    protected void initViewsAndEvents() {
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
        getTagItmes();
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

        if (!TextUtils.isEmpty(intentionJobsId2) || !TextUtils.isEmpty(education) || !TextUtils.isEmpty(experience) || !TextUtils.isEmpty(wage)) {
            map.put("category", intentionJobs);
            map.put("education", education);
            map.put("experience", experience);
            map.put("wage", wage);
        }
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


//--------------------------------------------------------筛选模块-开始--------------------------------------------------------


    private void setFilter() {
        types.clear();
        popupViews.clear();

        //职位  经验  学历   薪资

        View positionView = View.inflate(getActivity(), R.layout.popupwindow_job_tag, null);
        final RecyclerView tag1 = (RecyclerView) positionView.findViewById(R.id.tag1);
        final RecyclerView tag2 = (RecyclerView) positionView.findViewById(R.id.tag2);
        final RecyclerView tag3 = (RecyclerView) positionView.findViewById(R.id.tag3);
        tag1.setLayoutManager(new LinearLayoutManager(getActivity()));
        JobPerfectInfoTagAdapter1 adapter = new JobPerfectInfoTagAdapter1(getActivity(), jobTagBean);
        tag1.setAdapter(adapter);
        adapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {

                tag2.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter1 = new JobPerfectInfoTagAdapter(getActivity());
                tag2.setAdapter(adapter1);
                adapter1.setCommonInterface(new RecyclerViewCommonInterface() {
                    @Override
                    public void onClick(Object bean) {
                        //点击了第二页的
                        tag3.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter2 = new JobPerfectInfoTagAdapter(getActivity());
                        tag3.setAdapter(adapter2);
                        adapter2.setCommonInterface(new RecyclerViewCommonInterface() {
                            @Override
                            public void onClick(Object bean) {
                                //点击了第三页
                                intentionJobsId3 = ((JobTagBean.DataBean) bean).getId() == -1 ? "" : ((JobTagBean.DataBean) bean).getId() + "";
                                cacheJobTag = (JobTagBean.DataBean) bean;
//                                if (!TextUtils.isEmpty(intentionJobsId1) && !TextUtils.isEmpty(intentionJobsId2) && !TextUtils.isEmpty(intentionJobsId3)) {
//                                    intentionJobsId = intentionJobsId1 + "." + intentionJobsId2 + "." + intentionJobsId3;
//                                }
                                if (((JobTagBean.DataBean) bean).getId() == -1) {
                                    intentionJobs = "职位";
                                } else {
                                    intentionJobs = cacheJobTag.getCategoryname();
                                }
                                pageNum = 1;
                                getAllJobs();
                                mDropDownMenu.setTabText(intentionJobs);
                                mDropDownMenu.closeMenu();
                            }
                        });

                        jobStep = 2;
                        intentionJobsId2 = ((JobTagBean.DataBean) bean).getId() == -1 ? "" : ((JobTagBean.DataBean) bean).getId() + "";
                        getJobData(((JobTagBean.DataBean) bean).getId() + "");
                    }
                });

                //点击了第一页的
                jobStep = 1;
                intentionJobsId1 = ((JobTagBean.DataBean) bean).getId() == -1 ? "" : ((JobTagBean.DataBean) bean).getId() + "";
                getJobData(((JobTagBean.DataBean) bean).getId() + "");
            }
        });
        types.add("职位");
        popupViews.add(positionView);

//经验

        View experienceView = View.inflate(getActivity(), R.layout.popupwindow_common_tag, null);
        RecyclerView tag_recyclerview = (RecyclerView) experienceView.findViewById(R.id.tag_recyclerview);
        tag_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<CommonTagItemBean> listExperience = commonTagBean.getData().getQS_experience();
        listExperience.add(0, new CommonTagItemBean(-1, "不限"));
        CommonPerfectInfoTagAdapter experienceAdapter = new CommonPerfectInfoTagAdapter(getActivity(), listExperience);
        tag_recyclerview.setAdapter(experienceAdapter);
        experienceAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                cacheCommonTagBean = (CommonTagItemBean) bean;
                if (cacheCommonTagBean.getCId() == -1) {
                    experience = "";
                    experienceCn = "经验";
                } else {
                    experience = cacheCommonTagBean.getCId() + "";
                    experienceCn = cacheCommonTagBean.getCName();
                }
                pageNum = 1;
                getAllJobs();
                mDropDownMenu.setTabText(experienceCn);
                mDropDownMenu.closeMenu();
            }
        });
        types.add("经验");
        popupViews.add(experienceView);
//学历

        View educationView = View.inflate(getActivity(), R.layout.popupwindow_common_tag, null);
        RecyclerView education_tag_recyclerview = (RecyclerView) educationView.findViewById(R.id.tag_recyclerview);
        education_tag_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<CommonTagItemBean> listEducation = commonTagBean.getData().getQS_education();
        listEducation.add(0, new CommonTagItemBean(-1, "不限"));
        CommonPerfectInfoTagAdapter educationAdapter = new CommonPerfectInfoTagAdapter(getActivity(), listEducation);
        education_tag_recyclerview.setAdapter(educationAdapter);
        educationAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                cacheCommonTagBean = (CommonTagItemBean) bean;
                if (cacheCommonTagBean.getCId() == -1) {
                    education = "";
                    educationCn = "学历";
                } else {
                    education = cacheCommonTagBean.getCId() + "";
                    educationCn = cacheCommonTagBean.getCName();
                }
                pageNum = 1;
                getAllJobs();
                mDropDownMenu.setTabText(educationCn);
                mDropDownMenu.closeMenu();
            }
        });
        types.add("学历");
        popupViews.add(educationView);
//薪资

        View wageView = View.inflate(getActivity(), R.layout.popupwindow_common_tag, null);
        RecyclerView wage_tag_recyclerview = (RecyclerView) wageView.findViewById(R.id.tag_recyclerview);
        wage_tag_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<CommonTagItemBean> listWage = commonTagBean.getData().getQS_wage();
        listWage.add(0, new CommonTagItemBean(-1, "不限"));
        CommonPerfectInfoTagAdapter wageAdapter = new CommonPerfectInfoTagAdapter(getActivity(), listWage);
        wage_tag_recyclerview.setAdapter(wageAdapter);
        wageAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                cacheCommonTagBean = (CommonTagItemBean) bean;
                if (cacheCommonTagBean.getCId() == -1) {
                    wage = "";
                    wageCn = "薪资";
                } else {
                    wage = cacheCommonTagBean.getCId() + "";
                    wageCn = cacheCommonTagBean.getCName();
                }
                pageNum = 1;
                getAllJobs();
                mDropDownMenu.setTabText(wageCn);
                mDropDownMenu.closeMenu();
            }
        });
        types.add("薪资");
        popupViews.add(wageView);


        mDropDownMenu.setDropDownMenu(types, popupViews);
    }

    /**
     * 获取职位信息
     *
     * @param id
     */
    private void getJobData(String id) {
        Call<JobTagBean> jobTagBeanCall = RestAdapterManager.getApi().getJobType(id);
        jobTagBeanCall.enqueue(new JyCallBack<JobTagBean>() {
            @Override
            public void onSuccess(Call<JobTagBean> call, Response<JobTagBean> response) {
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    if (jobStep == 0) {
                        jobTagBean = response.body().getData();
                        jobTagBean.add(0, new JobTagBean.DataBean(-1, "不限"));
                        try {
                            setFilter();
                        } catch (Exception e) {
                        }

                    } else if (jobStep == 1) {
                        adapter1.ClearData();
                        List<JobTagBean.DataBean> jobTagBean2 = response.body().getData();
                        jobTagBean2.add(0, new JobTagBean.DataBean(-1, "不限"));
                        adapter1.addList(jobTagBean2);
                    } else if (jobStep == 2) {
                        adapter2.ClearData();
                        List<JobTagBean.DataBean> jobTagBean3 = response.body().getData();
                        jobTagBean3.add(0, new JobTagBean.DataBean(-1, "不限"));
                        adapter2.addList(jobTagBean3);
                    }
                } else {
                    UIUtil.showToast("接口错误");
                }


            }

            @Override
            public void onError(Call<JobTagBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
                UIUtil.showToast("接口错误");
            }

            @Override
            public void onError(Call<JobTagBean> call, Response<JobTagBean> response) {

            }
        });
    }

    /**
     * 根据对应的分类id获取对应的数据
     */

    private void getTagItmes() {
        List<String> list = new ArrayList<>();
        list.add("QS_education");
        list.add("QS_experience");
        list.add("QS_wage");
        GetBaseTagBean tagBean = new GetBaseTagBean();
        tagBean.setAlias(list);
        Call<CommonTagBean> commonTagBeanCall = RestAdapterManager.getApi().getWorkAgeAndResume(tagBean);
        commonTagBeanCall.enqueue(new JyCallBack<CommonTagBean>() {
            @Override
            public void onSuccess(Call<CommonTagBean> call, Response<CommonTagBean> response) {
                LogUtils.e(response.body().getMsg());
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    commonTagBean = response.body();
                    getJobData("0");
                } else {
                    UIUtil.showToast(response.body().getMsg());
                }
            }

            @Override
            public void onError(Call<CommonTagBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<CommonTagBean> call, Response<CommonTagBean> response) {

            }
        });
    }

    private void initTitle() {

        titleView.setTitle("全部职位");
        titleView.setTitleColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        mSearchView = (ImageView) titleView.addAction(new TitleBar.ImageAction(R.mipmap.ic_search) {
            @Override
            public void performAction(View view) {
                Intent intent = new Intent(getActivity(), AllPersonSearchActivity.class);
                startActivity(intent);
            }
        });
        titleView.setImmersive(true);
    }
}
