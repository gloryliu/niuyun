package com.niuyun.hire.ui.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.niuyun.hire.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.BaseFragment;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.activity.GoodsDetailsActivity;
import com.niuyun.hire.ui.adapter.MainListItemAdapter;
import com.niuyun.hire.ui.adapter.PoPuMenuListAdapter;
import com.niuyun.hire.ui.adapter.PoPuMenuListShopAdapter;
import com.niuyun.hire.ui.bean.GoodsFilterBean;
import com.niuyun.hire.ui.bean.GoodsListBean;
import com.niuyun.hire.ui.bean.ShopsFilterBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.bannerBean;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.DropDownMenu;
import com.niuyun.hire.view.MaxHeighListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-6-19.
 */

public class IndexFragment extends BaseFragment {
    @BindView(R.id.sf_listview)
    RecyclerView sf_listview;
    @BindView(R.id.convenientBanner)
    ConvenientBanner kanner;
    @BindView(R.id.dropDownMenu)
    DropDownMenu dropDownMenu;
    @BindView(R.id.swiperefreshlayout)
    TwinklingRefreshLayout swiperefreshlayout;
    @BindView(R.id.edit_search)
    EditText edit_search;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.ll_empty)
    LinearLayout ll_empty;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.bt_search)
    TextView bt_search;

    List<bannerBean> list = new ArrayList<>();
    List<GoodsListBean> adList = new ArrayList<>();
    //    private DropDownMenu dropDownMenu;
    //筛选标题list
    private List<String> types = new ArrayList<>();
    //筛选view的list
    private List<View> popupViews = new ArrayList<>();
    //    private List<SelectedBean> demands = new ArrayList<>();
    private List<ShopsFilterBean> shopsList = new ArrayList<>();
    private List<GoodsFilterBean> typesList = new ArrayList<>();
    private PoPuMenuListShopAdapter mMenuAdapter2;
    private PoPuMenuListAdapter mMenuAdapter;
    private MainListItemAdapter listAdapter;

    private Call<SuperBean<List<ShopsFilterBean>>> shopsCall;
    private Call<SuperBean<List<GoodsFilterBean>>> typesCall;
    private Call<SuperBean<List<GoodsListBean>>> goodsListCall;
    private Call<SuperBean<List<GoodsListBean>>> adListCall;


    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

    //筛选的关键字
    private String goodstype;
    private String shop;
    private String keyWord;
    private int pageNum = 1;
    private int pageSize = 10;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_index_layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initViewsAndEvents() {
        initTitle();


        sf_listview.setLayoutManager(linearLayoutManager);
        sf_listview.setNestedScrollingEnabled(false);
        swiperefreshlayout.setFloatRefresh(true);
        ProgressLayout headerView = new ProgressLayout(getActivity());
        swiperefreshlayout.setHeaderView(headerView);
        swiperefreshlayout.setEnableLoadmore(false);
//        BallPulseView footView = new BallPulseView(getActivity());
//        swiperefreshlayout.setBottomView(footView);

        swiperefreshlayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {

                getList();
//                getAdList();
//                UIUtil.showToast("刷新数据~");
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                getList();
//                UIUtil.showToast("加载更多");
            }
        });

        listAdapter = new MainListItemAdapter(getActivity());
        sf_listview.setAdapter(listAdapter);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edit_search.getText().toString())) {
                    keyWord = edit_search.getText().toString();
                    getList();
                    UIUtil.ShowOrHideSoftInput(getActivity(), false);
                }
            }
        });
        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_search.setText("");
                keyWord = "";
                getList();
            }
        });
    }

    @Override
    protected void loadData() {
        getAdList();
        getFilterList();
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
            if (eventBusCenter.getEvenCode() == Constants.LOGIN_FAILURE || eventBusCenter.getEvenCode() == Constants.LOGIN_SUCCESS) {
                loadData();
            }
        }

    }

    private void getAdList() {
        String userId = "0";
        list.clear();
        adList.clear();
        if (BaseContext.getInstance().getUserInfo() != null) {
            userId = BaseContext.getInstance().getUserInfo().userId;
        }
        adListCall = RestAdapterManager.getApi().getAdList(userId);
        adListCall.enqueue(new JyCallBack<SuperBean<List<GoodsListBean>>>() {
            @Override
            public void onSuccess(Call<SuperBean<List<GoodsListBean>>> call, Response<SuperBean<List<GoodsListBean>>> response) {
                //初始化广告栏
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    adList.addAll(response.body().getData());
                    for (int i = 0; i < adList.size(); i++) {
                        bannerBean bannerBean = new bannerBean();
                        bannerBean.setImage(adList.get(i).getGoodsImg());
                        list.add(bannerBean);
                    }
                } else {
                    try {
                        UIUtil.showToast(response.body().getMsg());
                    } catch (Exception E) {

                    }
                }
                if (list.size() > 0) {
                    kanner.setVisibility(View.VISIBLE);
                    initAD(list);
                } else {
                    kanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Call<SuperBean<List<GoodsListBean>>> call, Throwable t) {
                kanner.setVisibility(View.GONE);
            }

            @Override
            public void onError(Call<SuperBean<List<GoodsListBean>>> call, Response<SuperBean<List<GoodsListBean>>> response) {
                kanner.setVisibility(View.GONE);
            }
        });
    }

    private void getList() {
        Map<String, String> map = new HashMap<>();
//        map.put("pageNum", pageNum + "");
//        map.put("pageSize", pageSize + "");
        map.put("shop", shop);
        map.put("goodstype", goodstype);
        if (!TextUtils.isEmpty(keyWord)) {

            map.put("name", keyWord);
        }
        if (BaseContext.getInstance().getUserInfo() != null) {
            map.put("userId", BaseContext.getInstance().getUserInfo().userId);
        } else {
            map.put("userId", "0");
        }
        goodsListCall = RestAdapterManager.getApi().getGoodsList(map);
        goodsListCall.enqueue(new JyCallBack<SuperBean<List<GoodsListBean>>>() {
            @Override
            public void onSuccess(Call<SuperBean<List<GoodsListBean>>> call, Response<SuperBean<List<GoodsListBean>>> response) {
                swiperefreshlayout.finishRefreshing();
                swiperefreshlayout.onFinishLoadMore();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    if (response.body().getData().size() > 0) {
                        ll_empty.setVisibility(View.GONE);
                        sf_listview.setVisibility(View.VISIBLE);
                        listAdapter.ClearData();
                        listAdapter.addList(response.body().getData());
                        pageNum++;
                    } else {
                        if (!TextUtils.isEmpty(keyWord)) {
                            //搜索数据为空
                            tv_no_data.setText("没有“" + keyWord + "”的搜索结果");
                            ll_empty.setVisibility(View.VISIBLE);
                            sf_listview.setVisibility(View.GONE);
                            listAdapter.ClearData();
                        } else {
                            ll_empty.setVisibility(View.GONE);
                            sf_listview.setVisibility(View.VISIBLE);
                            if (pageNum == 1) {
                                //无数据
                                listAdapter.ClearData();
                            } else {
                                //加载完全部数据
                                UIUtil.showToast("已加载完全部数据");
                                swiperefreshlayout.setEnableLoadmore(false);
                            }
                        }

                    }

                } else {
                    try {
                        UIUtil.showToast(response.body().getMsg());
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onError(Call<SuperBean<List<GoodsListBean>>> call, Throwable t) {
                if (swiperefreshlayout != null) {
                    swiperefreshlayout.finishRefreshing();
                    swiperefreshlayout.onFinishLoadMore();
                }

            }

            @Override
            public void onError(Call<SuperBean<List<GoodsListBean>>> call, Response<SuperBean<List<GoodsListBean>>> response) {
                if (swiperefreshlayout != null) {
                    swiperefreshlayout.finishRefreshing();
                    swiperefreshlayout.onFinishLoadMore();
                }
            }
        });
    }

    //筛选
    private void setFilter() {
        types.clear();
        popupViews.clear();
        //益智类
        final MaxHeighListView sortView = new MaxHeighListView(getActivity());
        sortView.setDividerHeight(0);
        sortView.setMaxHeight(199);
        mMenuAdapter = new PoPuMenuListAdapter(getActivity(), typesList);
        sortView.setAdapter(mMenuAdapter);
        sortView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dropDownMenu.setTabText(typesList.get(position).getTypename());
                dropDownMenu.closeMenu();
                goodstype = typesList.get(position).getId() + "";
                getList();
            }
        });
        types.add("全部");
        popupViews.add(sortView);


        final MaxHeighListView softView = new MaxHeighListView(getActivity());
        softView.setDividerHeight(0);
        softView.setMaxHeight(199);
        mMenuAdapter2 = new PoPuMenuListShopAdapter(getActivity(), shopsList);
        softView.setAdapter(mMenuAdapter2);
        softView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dropDownMenu.setTabText(shopsList.get(position).getShopname());
                dropDownMenu.closeMenu();
                shop = shopsList.get(position).getId() + "";
                getList();
            }
        });
        types.add("全部");
        popupViews.add(softView);
        dropDownMenu.setDropDownMenu(types, popupViews);
    }

    private void getFilterList() {
        shopsCall = RestAdapterManager.getApi().getAllFilterShops();
        shopsCall.enqueue(new JyCallBack<SuperBean<List<ShopsFilterBean>>>() {
            @Override
            public void onSuccess(Call<SuperBean<List<ShopsFilterBean>>> call, Response<SuperBean<List<ShopsFilterBean>>> response) {

                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {

                    shopsList.clear();
                    shopsList.addAll(response.body().getData());
                    shopsList.add(0, new ShopsFilterBean(0, "全部"));
                    typesCall = RestAdapterManager.getApi().getAllFilterType();
                    typesCall.enqueue(new JyCallBack<SuperBean<List<GoodsFilterBean>>>() {
                        @Override
                        public void onSuccess(Call<SuperBean<List<GoodsFilterBean>>> call, Response<SuperBean<List<GoodsFilterBean>>> response) {
                            if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                                typesList.clear();
                                typesList.addAll(response.body().getData());
                                typesList.add(0, new GoodsFilterBean(0, "全部"));
                                setFilter();
                            } else {
                                try {
                                    UIUtil.showToast(response.body().getMsg());
                                } catch (Exception e) {
                                }
                            }
                        }

                        @Override
                        public void onError(Call<SuperBean<List<GoodsFilterBean>>> call, Throwable t) {
                            LogUtils.e(t.getMessage());
                        }

                        @Override
                        public void onError(Call<SuperBean<List<GoodsFilterBean>>> call, Response<SuperBean<List<GoodsFilterBean>>> response) {
                            try {
                                LogUtils.e(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    try {
                        UIUtil.showToast(response.body().getMsg());
                    } catch (Exception e) {
                    }
                }


            }

            @Override
            public void onError(Call<SuperBean<List<ShopsFilterBean>>> call, Throwable t) {
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<SuperBean<List<ShopsFilterBean>>> call, Response<SuperBean<List<ShopsFilterBean>>> response) {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.color_ff6900);

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getActivity().getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 广告栏定义图片地址
     */
    public class LocalImageHolderView implements Holder<bannerBean> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, bannerBean data) {
            ImageLoadedrManager.getInstance().display(getActivity(), data.getImage(), imageView);
        }
    }

    private void initAD(final List<bannerBean> list) {
//        View header = LayoutInflater.from(getActivity()).inflate(R.layout.kanner_index, sf_listview, false);//headview,广告栏


//自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        kanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, list)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.dot_blur, R.mipmap.dot_black})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL).startTurning(3000);
        //设置翻页的效果，不需要翻页效果可用不设
        //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
//        convenientBanner.setManualPageable(false);//设置不能手动影响

        kanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), GoodsDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("detail", adList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
//增加headview
//        sf_listview.addHeaderView(header);
//        sf_listview.add
    }

    @Override
    public void onDestroy() {
        if (adListCall != null) {
            adListCall.cancel();
        }
        if (shopsCall != null) {
            shopsCall.cancel();
        }
        if (typesCall != null) {
            typesCall.cancel();
        }
        if (goodsListCall != null) {
            goodsListCall.cancel();
        }
        super.onDestroy();
    }
}
