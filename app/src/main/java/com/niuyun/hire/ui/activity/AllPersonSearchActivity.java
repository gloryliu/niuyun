package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.HotSearchAdapter;
import com.niuyun.hire.ui.adapter.IndexSeekPersonListItemAdapter;
import com.niuyun.hire.ui.adapter.SearchHistoryAdapter;
import com.niuyun.hire.ui.bean.EnterpriseFindPersonBean;
import com.niuyun.hire.ui.bean.HotSearchBean;
import com.niuyun.hire.ui.bean.SearchHistoryBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.ui.utils.service.SearchHistoryService;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-8-16.
 */

public class AllPersonSearchActivity extends BaseActivity implements View.OnClickListener {
    //    @BindView(R.id.title_view)
//    TitleBar title_view;
    @BindView(R.id.rv_hot_tag)
    RecyclerView rv_hot_tag;
    @BindView(R.id.rv_history_tag)
    RecyclerView rv_history_tag;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_button)
    TextView tv_button;
    @BindView(R.id.tv_clear_all)
    TextView tv_clear_all;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv_search_result)
    RecyclerView rv_search_result;
    @BindView(R.id.ll_tag)
    LinearLayout ll_tag;
    private HotSearchAdapter adapter;
    private SearchHistoryAdapter historyAdapter;
    private HotSearchBean commonTagBean;
    private int mSearchType = 1;//当前选中的业态
    private List<SearchHistoryBean> searchList;
    private SearchHistoryService searchHistoryService;
    private Call<EnterpriseFindPersonBean> allJobsBeanCall;
    private Call<HotSearchBean> commonTagBeanCall;
    private int pageNum = 1;
    private int pageSize = 20;
    private IndexSeekPersonListItemAdapter listItemAdapter;
    private String keyword;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_alljob_search;
    }

    @Override
    public void initViewsAndEvents() {

        tv_clear_all.setOnClickListener(this);
        tv_button.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        rv_hot_tag.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new HotSearchAdapter(this);
        rv_hot_tag.setAdapter(adapter);
        adapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                et_search.setText(((HotSearchBean.DataBean) bean).getKeyword());
                search(((HotSearchBean.DataBean) bean).getKeyword());
            }
        });

        rv_history_tag.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new SearchHistoryAdapter(this);
        rv_history_tag.setAdapter(historyAdapter);
        historyAdapter.setSearchHistoryService(getSearchHistoryService());
        historyAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                if (bean != null) {
                    et_search.setText(((SearchHistoryBean) bean).getName());
                    search(((SearchHistoryBean) bean).getName());
                }

            }
        });


        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (!TextUtils.isEmpty(keyword)) {
                    pageNum = 1;
                    getAllJobs();
                } else {
                    refreshLayout.finishRefresh();

                }

            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (!TextUtils.isEmpty(keyword)) {
                    pageNum += 1;
                    getAllJobs();
                } else {
                    refreshLayout.finishLoadmore();
                }

            }
        });
        refreshLayout.setEnableLoadmore(false);


        rv_search_result.setLayoutManager(new LinearLayoutManager(this));

        listItemAdapter = new IndexSeekPersonListItemAdapter(this);
        rv_search_result.setAdapter(listItemAdapter);
        listItemAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
            @Override
            public void onClick(Object bean) {
                EnterpriseFindPersonBean.DataBeanX.DataBean databean = (EnterpriseFindPersonBean.DataBeanX.DataBean) bean;
                if (databean != null) {
                    Intent intent = new Intent(AllPersonSearchActivity.this, PreviewResumeActivity.class);
                    Bundle bundle = new Bundle();
//                    bundle.putString("id", databean.getId() + "");
//                    bundle.putString("companyId", databean.getCompanyId() + "");
                    bundle.putString("uid", databean.getUid() + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });


        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(et_search.getText().toString());
                }
                return false;
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//
                if (!TextUtils.isEmpty(et_search.getText())) {
                    tv_button.setText("搜索");
                    iv_clear.setVisibility(View.VISIBLE);
                } else {
                    tv_button.setText("取消");
                    iv_clear.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public void loadData() {
        getHistoryData();
        getTagItmes();
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

    /**
     * 执行搜索方法
     *
     * @param word 搜索关键字
     */
    private void search(String word) {
        SearchHistoryBean history = new SearchHistoryBean();
        history.setName(word);
        //history.setId(0);
        history.setType(mSearchType);
        getSearchHistoryService().saveRecent(history);
        UIUtil.closeSoftKeyBoard(this);
        getHistoryData();
        keyword = et_search.getText().toString();
        getAllJobs();
    }

    private SearchHistoryService getSearchHistoryService() {
        if (searchHistoryService == null) {
            searchHistoryService = new SearchHistoryService(this);
        }
        return searchHistoryService;
    }

    /**
     * 历史数据加载
     */

    private void getHistoryData() {
        searchList = getSearchHistoryService().getAll(mSearchType);
        historyAdapter.ClearData();
        historyAdapter.addList(searchList);
    }

    private void getTagItmes() {
        commonTagBeanCall = RestAdapterManager.getApi().getHotSearchTag();
        commonTagBeanCall.enqueue(new JyCallBack<HotSearchBean>() {
            @Override
            public void onSuccess(Call<HotSearchBean> call, Response<HotSearchBean> response) {
                LogUtils.e(response.body().getMsg());
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    commonTagBean = response.body();
                    adapter.addList(commonTagBean.getData());
                } else {
                    UIUtil.showToast(response.body().getMsg());
                }
            }

            @Override
            public void onError(Call<HotSearchBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<HotSearchBean> call, Response<HotSearchBean> response) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear_all:
                getSearchHistoryService().ClearAllData(mSearchType);
                getHistoryData();
                break;
            case R.id.tv_button:
                if (tv_button.getText().equals("搜索")) {
                    search(et_search.getText().toString());
                } else {
                    finish();
                }
                break;
            case R.id.iv_clear:
                et_search.setText("");
                keyword = "";
                setData();
                break;

        }
    }

    private void getAllJobs() {
        DialogUtils.showDialog(AllPersonSearchActivity.this, "", false);
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", pageSize + "");
        map.put("keyword", keyword);
        allJobsBeanCall = RestAdapterManager.getApi().getPersonon(map);


        allJobsBeanCall.enqueue(new JyCallBack<EnterpriseFindPersonBean>() {
            @Override
            public void onSuccess(Call<EnterpriseFindPersonBean> call, Response<EnterpriseFindPersonBean> response) {
                DialogUtils.closeDialog();
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
                setData();
            }

            @Override
            public void onError(Call<EnterpriseFindPersonBean> call, Throwable t) {
                DialogUtils.closeDialog();
                if (refreshLayout != null) {
                    if (pageNum == 1) {
                        listItemAdapter.ClearData();
                    }
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                }
                try {
                    setData();
                } catch (Exception e) {
                }

                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<EnterpriseFindPersonBean> call, Response<EnterpriseFindPersonBean> response) {
                DialogUtils.closeDialog();
                if (refreshLayout != null) {
                    if (pageNum == 1) {
                        listItemAdapter.ClearData();
                    }
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                }
                try {
                    setData();
                    LogUtils.e(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void setData() {
        if (!TextUtils.isEmpty(keyword)) {
            if (listItemAdapter.getList() != null && listItemAdapter.getList().size() > 0) {
//显示list
                ll_tag.setVisibility(View.GONE);
                rv_search_result.setVisibility(View.VISIBLE);
            } else {
                //全部以藏
                ll_tag.setVisibility(View.GONE);
                rv_search_result.setVisibility(View.GONE);
            }
        } else {
//显示热门
            ll_tag.setVisibility(View.VISIBLE);
            rv_search_result.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (allJobsBeanCall != null) {
            allJobsBeanCall.cancel();
        }
        if (commonTagBeanCall != null) {
            commonTagBeanCall.cancel();
        }
    }
}
