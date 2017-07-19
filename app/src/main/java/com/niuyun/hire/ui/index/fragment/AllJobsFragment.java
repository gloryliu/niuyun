package com.niuyun.hire.ui.index.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.IndexCompanyListItemAdapter;
import com.niuyun.hire.ui.adapter.PoPuMenuListCommonAdapter;
import com.niuyun.hire.ui.bean.SelectedBean;
import com.niuyun.hire.view.DropDownMenu;
import com.niuyun.hire.view.MaxHeighListView;
import com.niuyun.hire.view.TitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.niuyun.hire.R.id.title_view;

/**
 * Created by chen.zhiwei on 2017-7-18.
 */

public class AllJobsFragment extends BaseFragment {
    @BindView(title_view)
    TitleBar titleView;
    @BindView(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private IndexCompanyListItemAdapter listItemAdapter;
    //筛选标题list
    private List<String> types = new ArrayList<>();
    //筛选view的list
    private List<View> popupViews = new ArrayList<>();
    private List<SelectedBean> demands = new ArrayList<>();
    private List<SelectedBean> demands1 = new ArrayList<>();
    private PoPuMenuListCommonAdapter mMenuAdapter;
    private PoPuMenuListCommonAdapter mMenuAdapter2;
    private ImageView mSearchView;

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
                refreshlayout.finishRefresh(2000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });
        refreshLayout.setEnableLoadmore(false);
        //求职recyclerView
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItemAdapter = new IndexCompanyListItemAdapter(getActivity());
        recyclerview.setAdapter(listItemAdapter);
    }

    @Override
    protected void loadData() {
        setFilter();
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
//--------------------------------------------------------筛选模块-开始--------------------------------------------------------


    private void setFilter() {
        types.clear();
        popupViews.clear();


        //区域
        final MaxHeighListView sortView = new MaxHeighListView(getActivity());
        sortView.setDividerHeight(0);
        sortView.setMaxHeight(199);
        demands.clear();
        demands.add(new SelectedBean("", "全部"));
        demands.add(new SelectedBean("salesPrice_asc", "北京"));
        demands.add(new SelectedBean("salesPrice_desc", "上海"));
        demands.add(new SelectedBean("floorSpace_asc", "成都"));
        demands.add(new SelectedBean("floorSpace_desc", "广州"));
        mMenuAdapter = new PoPuMenuListCommonAdapter(getActivity(), demands);

        sortView.setAdapter(mMenuAdapter);
        sortView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mDropDownMenu.setTabText(demands.get(position).getName());
                mDropDownMenu.closeMenu();
//                mSortBy = demands.get(position).getKey();
//                stitch();
            }
        });
        types.add("区域");
        popupViews.add(sortView);

        //区域
        final MaxHeighListView workView = new MaxHeighListView(getActivity());
        workView.setDividerHeight(0);
        workView.setMaxHeight(199);
        demands1.clear();
        demands1.add(new SelectedBean("", "全部"));
        demands1.add(new SelectedBean("salesPrice_asc", "Android"));
        demands1.add(new SelectedBean("salesPrice_desc", "ios"));
        demands1.add(new SelectedBean("floorSpace_asc", "会计"));
        demands1.add(new SelectedBean("floorSpace_desc", "产品"));
        mMenuAdapter2 = new PoPuMenuListCommonAdapter(getActivity(), demands1);

        workView.setAdapter(mMenuAdapter2);
        workView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mDropDownMenu.setTabText(demands.get(position).getName());
                mDropDownMenu.closeMenu();
//                mSortBy = demands.get(position).getKey();
//                stitch();
            }
        });
        types.add("职能");
        popupViews.add(workView);


        mDropDownMenu.setDropDownMenu(types, popupViews);
    }

    private void initTitle() {

        titleView.setTitle("全部职位");
        titleView.setTitleColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        mSearchView = (ImageView) titleView.addAction(new TitleBar.ImageAction(R.mipmap.ic_search) {
            @Override
            public void performAction(View view) {
//                Intent intent = new Intent(ShoppingAddressActivity.this, ShoppingAddressEditActivity.class);
//                startActivity(intent);
            }
        });
        titleView.setImmersive(true);
    }
}
