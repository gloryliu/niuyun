package com.niuyun.hire.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.niuyun.hire.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.adapter.GoodsDetailItemAdapter;
import com.niuyun.hire.ui.bean.GoodsListBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.bannerBean;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-6-21.
 */

public class GoodsDetailsActivity extends BaseActivity {

    @BindView(R.id.sf_listview)
    RecyclerView sf_listview;
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.tv_goods_detail_describe)
    TextView tv_goods_detail_describe;
    private ConvenientBanner kanner;
    List<bannerBean> list = new ArrayList<>();
    private GoodsDetailItemAdapter listAdapter;
    private Button bt_buy;
    private Button bt_exchange_state;
    private GoodsListBean goodsBean;
    private TextView tv_goods_name;
    private TextView tv_price_title;
    private TextView tv_member_price;
    private TextView tv_member_price_title;
    private TextView tv_price;
    private Call<SuperBean<GoodsListBean>> call;
    private boolean isNewData = true;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_goods_details;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();

        kanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        bt_buy = (Button) findViewById(R.id.bt_buy);
        bt_exchange_state = (Button) findViewById(R.id.bt_exchange_state);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_price_title = (TextView) findViewById(R.id.tv_price_title);
        tv_member_price = (TextView) findViewById(R.id.tv_member_price);
        tv_member_price_title = (TextView) findViewById(R.id.tv_member_price_title);
        tv_price = (TextView) findViewById(R.id.tv_price);


        goodsBean = (GoodsListBean) getIntent().getExtras().getSerializable("detail");

//        sf_listview.setSwipeEnable(true);//open swipe
        sf_listview.setLayoutManager(new LinearLayoutManager(this));
        sf_listview.setNestedScrollingEnabled(false);

        listAdapter = new GoodsDetailItemAdapter(this);

        sf_listview.setAdapter(listAdapter);


    }

    @Override
    public void loadData() {
        setData();
    }

    @Override
    public boolean isRegistEventBus() {
        return true;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {
        if (eventBusCenter != null) {
            if (eventBusCenter.getEvenCode() == Constants.LOGIN_SUCCESS) {
                isNewData = false;
                getGoodsDetail();
            }
        }
    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    private void getGoodsDetail() {
        if (goodsBean == null) {
            return;
        }
        DialogUtils.showDialog(this, "加载中", false);
        if (BaseContext.getInstance().getUserInfo() != null) {
            call = RestAdapterManager.getApi().getGoodsDetail(goodsBean.getId() + "", BaseContext.getInstance().getUserInfo().userId);
        } else {
            call = RestAdapterManager.getApi().getGoodsDetail(goodsBean.getId() + "", "");
        }
        call.enqueue(new JyCallBack<SuperBean<GoodsListBean>>() {
            @Override
            public void onSuccess(Call<SuperBean<GoodsListBean>> call, Response<SuperBean<GoodsListBean>> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    goodsBean = response.body().getData();
                    setData();
                    isNewData = true;
                } else {
                    try {
                        UIUtil.showToast(response.body().getMsg());
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onError(Call<SuperBean<GoodsListBean>> call, Throwable t) {
                DialogUtils.closeDialog();
            }

            @Override
            public void onError(Call<SuperBean<GoodsListBean>> call, Response<SuperBean<GoodsListBean>> response) {
                DialogUtils.closeDialog();
            }
        });
    }

    private void setData() {
        if (goodsBean != null) {
            if (goodsBean.getBinners() != null) {
                //广告位
                for (int i = 0; i < goodsBean.getBinners().size(); i++) {
                    bannerBean bannerBean = new bannerBean();
                    bannerBean.setImage(goodsBean.getBinners().get(i));
                    list.add(bannerBean);
                }
                //初始化广告栏
                initAD(list);
            }
            //大图
            if (goodsBean.getDetails() != null) {
                listAdapter.addList(goodsBean.getDetails());
            }

        }
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("玩呗");
        title_view.setTitleColor(Color.WHITE);
        title_view.setLeftImageResource(R.mipmap.ic_title_back);
        title_view.setLeftText("返回");
        title_view.setLeftTextColor(Color.WHITE);
        title_view.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title_view.setBackgroundColor(getResources().getColor(R.color.color_ff6900));
        title_view.setImmersive(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.color_ff6900);

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
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
            ImageLoadedrManager.getInstance().display(GoodsDetailsActivity.this, data.getImage(), imageView);
        }
    }

    private void initAD(final List<bannerBean> list) {


        setPriceValue();
        bt_exchange_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_buy.getText().equals("立即租赁")) {
                    bt_exchange_state.setText("切换至租赁");
                    bt_buy.setText("立即购买");
                    setPriceValue();
                } else {
                    bt_exchange_state.setText("切换至购买");
                    bt_buy.setText("立即租赁");
                    setPriceValue();
                }
            }
        });

        bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseContext.getInstance().getUserInfo() == null) {
                    startActivity(new Intent(GoodsDetailsActivity.this, LoginActivity.class));
                } else {
                    if (!isNewData) {
                        getGoodsDetail();
                        return;
                    }
                    if (checkbuyOrRentValiable()) {
                        Intent intent = new Intent(GoodsDetailsActivity.this, CommitOrderActivity.class);
                        Bundle bundle = new Bundle();

                        if (bt_buy.getText().equals("立即租赁")) {
//                        if (goodsBean.getVipprice() > 0) {
                            bundle.putString("type", "rent");
//                        }
                        } else {
//                        if (goodsBean.getPrice() > 0) {
                            bundle.putString("type", "sale");
//                        }
                        }
                        bundle.putSerializable("detail", goodsBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }


                }
            }
        });


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

    }

    private boolean checkbuyOrRentValiable() {
        if (bt_buy.getText().equals("立即租赁")) {
            if (TextUtils.isEmpty(BaseContext.getInstance().getUserInfo().phone) || TextUtils.isEmpty(BaseContext.getInstance().getUserInfo().ID)) {
                UIUtil.showToast("请先绑定手机号码并实名制");
                return true;
            }
        } else {
            if (TextUtils.isEmpty(BaseContext.getInstance().getUserInfo().phone)) {
                UIUtil.showToast("请先绑定手机号码");
                return false;
            }
        }
        return true;
    }

    private void setPriceValue() {
        tv_goods_name.setText(goodsBean.getName());//标题
        tv_goods_detail_describe.setText(goodsBean.getGoodsdetail());
        if (bt_buy.getText().equals("立即租赁")) {
            if (goodsBean.getPrice() > 0) {
                tv_price_title.setText("￥" + goodsBean.getPrice() / 100.00);
                tv_price.setVisibility(View.VISIBLE);
            } else {
                tv_price_title.setText("");
                tv_price.setVisibility(View.GONE);
            }
            if (goodsBean.getVipprice() > 0) {
                tv_member_price.setText("￥" + goodsBean.getVipprice() / 100.00);
                tv_member_price_title.setVisibility(View.VISIBLE);
            } else {
                tv_member_price.setText("");
                tv_member_price_title.setVisibility(View.GONE);
            }
        } else {
            if (goodsBean.getPrice() > 0) {
                tv_price_title.setText("￥" + goodsBean.getPrice() / 100.00);
                tv_price.setVisibility(View.GONE);
            } else {
                tv_price_title.setText("");
                tv_price.setVisibility(View.GONE);
            }
            tv_member_price.setText("");
            tv_member_price_title.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        if (call != null) {
            call.cancel();
        }
        super.onDestroy();
    }
}
