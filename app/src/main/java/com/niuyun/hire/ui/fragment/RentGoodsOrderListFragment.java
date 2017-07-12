package com.niuyun.hire.ui.fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.RentOrderListAdapter;
import com.niuyun.hire.ui.bean.BuyOrderListItemBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.utils.UIUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 租赁订单
 * Created by chen.zhiwei on 2017-6-21.
 */

public class RentGoodsOrderListFragment extends BaseFragment {
    @BindView(R.id.sf_listview)
    RecyclerView sf_listview;
    @BindView(R.id.swiperefreshlayout)
    TwinklingRefreshLayout swiperefreshlayout;
    private RentOrderListAdapter rentOrderListAdapter;
    private Call<SuperBean<BuyOrderListItemBean>> orderListCall;
    private int pageNum = 1;
    private int pageSize = 10;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_rent_order_list;
    }

    @Override
    protected void initViewsAndEvents() {
        sf_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        swiperefreshlayout.setFloatRefresh(true);
        ProgressLayout headerView = new ProgressLayout(getActivity());
        swiperefreshlayout.setHeaderView(headerView);
        swiperefreshlayout.setEnableLoadmore(true);
//        BallPulseView footView = new BallPulseView(getActivity());
//        swiperefreshlayout.setBottomView(footView);
        rentOrderListAdapter = new RentOrderListAdapter(getActivity());
        sf_listview.setAdapter(rentOrderListAdapter);
        swiperefreshlayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swiperefreshlayout.setEnableLoadmore(true);
                    }
                }, 10);

                pageNum = 1;
                getOrderList();
//                UIUtil.showToast("刷新数据~");
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                getOrderList();
//                UIUtil.showToast("加载更多");
            }
        });
    }

    @Override
    protected void loadData() {
getOrderList();
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

    private void getOrderList() {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", pageSize + "");
        map.put("userid", BaseContext.getInstance().getUserInfo().userId);
        orderListCall = RestAdapterManager.getApi().getRentOrderList(map);
        orderListCall.enqueue(new JyCallBack<SuperBean<BuyOrderListItemBean>>() {
            @Override
            public void onSuccess(Call<SuperBean<BuyOrderListItemBean>> call, Response<SuperBean<BuyOrderListItemBean>> response) {
                if (swiperefreshlayout != null) {
                    swiperefreshlayout.finishRefreshing();
                    swiperefreshlayout.finishLoadmore();
                }
                if (response != null && response.body() != null) {
                    if (response.body().getData().getData().size() > 0) {
                        if (pageNum == 1) {
                            rentOrderListAdapter.ClearData();
                        }
                        rentOrderListAdapter.addList(response.body().getData().getData());
                        pageNum++;
                    } else {
                        if (pageNum == 1) {
//无数据
                        } else {
                            UIUtil.showToast("已加载完全部数据");
                            swiperefreshlayout.setEnableLoadmore(false);
                        }

                    }
                }
            }

            @Override
            public void onError(Call<SuperBean<BuyOrderListItemBean>> call, Throwable t) {
                if (swiperefreshlayout != null) {
                    swiperefreshlayout.finishRefreshing();
                    swiperefreshlayout.finishLoadmore();
                }
            }

            @Override
            public void onError(Call<SuperBean<BuyOrderListItemBean>> call, Response<SuperBean<BuyOrderListItemBean>> response) {
                if (swiperefreshlayout != null) {
                    swiperefreshlayout.finishRefreshing();
                    swiperefreshlayout.finishLoadmore();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (orderListCall != null) {
            orderListCall.cancel();
        }
    }
}
