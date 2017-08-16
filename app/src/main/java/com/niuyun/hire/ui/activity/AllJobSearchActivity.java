package com.niuyun.hire.ui.activity;

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
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.HotSearchAdapter;
import com.niuyun.hire.ui.adapter.SearchHistoryAdapter;
import com.niuyun.hire.ui.bean.CommonTagBean;
import com.niuyun.hire.ui.bean.GetBaseTagBean;
import com.niuyun.hire.ui.bean.SearchHistoryBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.ui.utils.service.SearchHistoryService;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-8-16.
 */

public class AllJobSearchActivity extends BaseActivity implements View.OnClickListener {
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
    private HotSearchAdapter adapter;
    private SearchHistoryAdapter historyAdapter;
    private CommonTagBean commonTagBean;
    private int mSearchType = 0;//当前选中的业态
    private List<SearchHistoryBean> searchList;
    private SearchHistoryService searchHistoryService;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_alljob_search;
    }

    @Override
    public void initViewsAndEvents() {
        rv_hot_tag.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new HotSearchAdapter(this);
        rv_hot_tag.setAdapter(adapter);
        tv_clear_all.setOnClickListener(this);
        tv_button.setOnClickListener(this);
        iv_clear.setOnClickListener(this);


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
//        search(word, mSearchType);
        UIUtil.closeSoftKeyBoard(this);
        getHistoryData();
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
//        if (searchList != null) {
//            if (searchList.size() > 0) {
//                tv_no_search_history.setVisibility(View.GONE);
//                tv_search_clear.setVisibility(View.VISIBLE);
//            } else {
//                tv_no_search_history.setVisibility(View.VISIBLE);
//                tv_search_clear.setVisibility(View.GONE);
//            }
//        } else {
//            tv_no_search_history.setVisibility(View.VISIBLE);
//            tv_search_clear.setVisibility(View.GONE);
//        }
//        rl_latest_search.setVisibility(View.VISIBLE);
//        ll_no_result_search.setVisibility(View.GONE);
    }

    private void getTagItmes() {
        List<String> list = new ArrayList<>();
        list.add("QS_jobtag");
        GetBaseTagBean tagBean = new GetBaseTagBean();
        tagBean.setAlias(list);
        Call<CommonTagBean> commonTagBeanCall = RestAdapterManager.getApi().getWorkAgeAndResume(tagBean);
        commonTagBeanCall.enqueue(new JyCallBack<CommonTagBean>() {
            @Override
            public void onSuccess(Call<CommonTagBean> call, Response<CommonTagBean> response) {
                LogUtils.e(response.body().getMsg());
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    commonTagBean = response.body();
                    adapter.addList(commonTagBean.getData().getQS_jobtag());
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
                break;

        }
    }
}
