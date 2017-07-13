package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.OrderDetailBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.NetUtil;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-6-22.
 */

public class OrderDetailsActivity extends BaseActivity {
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.tv_address_name)
    TextView tv_address_name;
    @BindView(R.id.tv_address_phone)
    TextView tv_address_phone;
    @BindView(R.id.tv_address_detail)
    TextView tv_address_detail;
    @BindView(R.id.tv_goods_name)
    TextView tv_goods_name;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_count)
    TextView tv_count;
    @BindView(R.id.tv_real_pay)
    TextView tv_real_pay;
    @BindView(R.id.tv_order_number)
    TextView tv_order_number;
    @BindView(R.id.tv_order_create_time)
    TextView tv_order_create_time;
    @BindView(R.id.tv_order_pay_time)
    TextView tv_order_pay_time;
    @BindView(R.id.iv_goods)
    ImageView iv_goods;


    private String orderId;
    Call<SuperBean<OrderDetailBean>> call;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        orderId = getIntent().getExtras().getString("orderId");
    }

    @Override
    public void loadData() {
        getOrderDetail();
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
     * 配置信息接口
     */
    private void initDate(OrderDetailBean orderDetailBean) {
        if (orderDetailBean != null) {

            ImageLoadedrManager.getInstance().display(this, orderDetailBean.getGoodsImg(), iv_goods);
            tv_address_name.setText(orderDetailBean.getOrdername());
            tv_address_phone.setText(orderDetailBean.getOrderphone());
            tv_address_detail.setText(orderDetailBean.getOrderaddress());
            tv_goods_name.setText(orderDetailBean.getGoodsName());
            tv_price.setText("￥" + orderDetailBean.getGoodsprice() / 100.00);
            tv_count.setText("x" + orderDetailBean.getCount());
            tv_real_pay.setText("￥" + orderDetailBean.getPayamount() / 100.00);
            tv_order_number.setText(orderDetailBean.getOrderpayno());
            tv_order_create_time.setText(UIUtil.timeStamp2Date(orderDetailBean.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
            tv_order_pay_time.setText(UIUtil.timeStamp2Date(orderDetailBean.getPaytime(), "yyyy-MM-dd HH:mm:ss"));
        }
    }

    /**
     * 获取数据接口
     */
    private void getOrderDetail() {
        if (!NetUtil.isNetworkConnected(this)) {
            UIUtil.showToast(R.string.net_state_error);
            return;
        }
        DialogUtils.showDialog(this, "加载中", false);
        call = RestAdapterManager.getApi().getOrderDetails(orderId);
        call.enqueue(new JyCallBack<SuperBean<OrderDetailBean>>() {
            @Override
            public void onSuccess(Call<SuperBean<OrderDetailBean>> call, Response<SuperBean<OrderDetailBean>> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null) {
                    if (response.body().getCode() == Constants.successCode) {
                        try {
                            initDate(response.body().getData());
                        } catch (Exception e) {
                        }

                    } else {
                        UIUtil.showToast(response.body().getMsg());
                    }
                } else {
                    UIUtil.showToast("获取数据异常");
                }
            }

            @Override
            public void onError(Call<SuperBean<OrderDetailBean>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("获取数据异常");
            }

            @Override
            public void onError(Call<SuperBean<OrderDetailBean>> call, Response<SuperBean<OrderDetailBean>> response) {
                DialogUtils.closeDialog();
                UIUtil.showToast("获取数据异常");
            }
        });

    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("订单详情");
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

    }


    @Override
    protected void onDestroy() {
        if (call != null) {
            call.cancel();
        }
        super.onDestroy();
    }
}
