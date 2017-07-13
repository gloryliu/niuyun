package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.javen205.jpay.JPay;
import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.GoodsListBean;
import com.niuyun.hire.ui.bean.ShoppingAddressListItemBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.NetUtil;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.utils.timepicker.TimePickerView;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

import static com.niuyun.hire.base.Constants.ADD_REQUEST_CODE;

/**
 * 提交订单
 * Created by chen.zhiwei on 2017-6-21.
 */

public class CommitOrderActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.title_view)
    TitleBar title_view;
    //    private BuyGoodsFragment buyGoodsFragment;
//    private RentGoodsFragment rentGoodsFragment;
    @BindView(R.id.tv_goods_num)
    TextView tvNum;
    @BindView(R.id.btn_des_goods_num)
    TextView tvDes;
    @BindView(R.id.btn_add_goods_num)
    TextView tvIns;
    @BindView(R.id.iv_goods)
    ImageView iv_goods;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_price_all)
    TextView tv_price_all;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.ll_add_addresss)
    LinearLayout ll_add_addresss;
    @BindView(R.id.ll_address)
    LinearLayout ll_address;
    @BindView(R.id.tv_address_name)
    TextView tv_address_name;
    @BindView(R.id.tv_address_phone)
    TextView tv_address_phone;
    @BindView(R.id.tv_address_detail)
    TextView tv_address_detail;
    @BindView(R.id.bt_commit)
    Button bt_commit;
    @BindView(R.id.rl_number)
    RelativeLayout rl_number;
    @BindView(R.id.vv_v1)
    View vv_v1;
    @BindView(R.id.vv_v2)
    View vv_v2;
    @BindView(R.id.rl_rent_days)
    RelativeLayout rl_rent_days;
    @BindView(R.id.ll_end_time)
    LinearLayout ll_end_time;
    @BindView(R.id.ll_begin_time)
    LinearLayout ll_begin_time;
    @BindView(R.id.tv_num_price)
    TextView tv_num_price;
    @BindView(R.id.tv_delivery_type)
    TextView tv_delivery_type;
    @BindView(R.id.rl_delivery_type)
    RelativeLayout rl_delivery_type;
    @BindView(R.id.tv_begin_time)
    TextView tv_begin_time;
    @BindView(R.id.tv_end_time)
    TextView tv_end_time;
    @BindView(R.id.tv_during_days)
    TextView tv_during_days;
    @BindView(R.id.tv_end_week)
    TextView tv_end_week;
    @BindView(R.id.tv_begin_week)
    TextView tv_begin_week;


    private String tag;//rent,sale
    private String id;
    private int price;
    private int count = 1;
    private int days;
    private int mxCount = 200;
    private GoodsListBean goodsBean;
    private Date beginDate;
    private Date endDate;
    private int payChannel = 0;//支付通道0为支付宝1为微信
    private int totalMoney;//订单总额单位是分
    private int orderType;//订单类型0为购买1租
    private int deliverytype = 1;//0为快递配送，1为店铺自取
    private Call<SuperBean<String>> getRsaOrderCall;
    private String orderId;
    Call<SuperBean<String>> commitOrderCall;
    Call<SuperBean<String>> commitRentCall;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_commit_order;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            goodsBean = (GoodsListBean) bundle.getSerializable("detail");
            tag = bundle.getString("type");
        }
        if (tag.equals("rent")) {
            rl_number.setVisibility(View.GONE);
            vv_v1.setVisibility(View.GONE);
            rl_rent_days.setVisibility(View.VISIBLE);
            vv_v2.setVisibility(View.VISIBLE);
            orderType = 1;
            setRentPrice();
        } else {
            rl_number.setVisibility(View.VISIBLE);
            vv_v1.setVisibility(View.VISIBLE);
            rl_rent_days.setVisibility(View.GONE);
            vv_v2.setVisibility(View.GONE);
            mxCount = goodsBean.getStock();
            orderType = 0;
            setSalePrice();
        }
        setDeliveryType();
        setDefaultTimeArea();
        ll_add_addresss.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        bt_commit.setOnClickListener(this);
        ll_begin_time.setOnClickListener(this);
        ll_end_time.setOnClickListener(this);
        rl_delivery_type.setOnClickListener(this);


        tvDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = Integer.valueOf(tvNum.getText().toString());
                if (count == 1) {
                    tvDes.setEnabled(false);
                    return;

                }
                tvIns.setEnabled(true);

                if (count > 1) {

                    if (count == 2) {
                        tvDes.setEnabled(false);
                    }
                    count--;
                    tvNum.setText(count + "");
                    setSalePrice();
                }
            }
        });
        tvIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDes.setEnabled(true);
                count = Integer.valueOf(tvNum.getText().toString());
                if (count < mxCount) {

                    if (count == mxCount - 1) {
                        tvIns.setEnabled(false);
                    }
                    count++;
                    tvNum.setText(count + "");
                    setSalePrice();
                }


            }
        });
    }

    private void setRentPrice() {
        if (BaseContext.getInstance().getUserInfo().vipgrade > 0) {
            price = goodsBean.getVipprice() * days + goodsBean.getDeposit();
        } else {
            price = goodsBean.getPrice() * days + goodsBean.getDeposit();
        }

        tv_num_price.setText("共计1件商品，合计￥" + price / 100.00);
        tv_price_all.setText("￥" + price / 100.00);
    }

    private void setSalePrice() {
        price = goodsBean.getPurchase() * count;
        tv_num_price.setText("共计" + count + "件商品，合计￥" + price / 100.00);
        tv_price_all.setText("￥" + price / 100.00);
    }

    @Override
    public void loadData() {
        setValueDefault();
    }

    private void setValueDefault() {
        ImageLoadedrManager.getInstance().display(this, goodsBean.getGoodsImg(), iv_goods);
        tv_title.setText(goodsBean.getName());
        if (tag.equals("rent")) {
            if (BaseContext.getInstance().getUserInfo().vipgrade > 0) {

                tv_price.setText("￥" + goodsBean.getVipprice() / 100.00);
            } else {
                tv_price.setText("￥" + goodsBean.getPrice() / 100.00);
            }
        } else {
            tv_price.setText("￥" + goodsBean.getPurchase() / 100.00);
        }


    }

    @Override
    public boolean isRegistEventBus() {
        return false;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (resultCode == Constants.ADD_REQUEST_CODE) {
//获取收货地址
                setAddress((ShoppingAddressListItemBean) data.getExtras().getSerializable("addressBean"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected View isNeedLec() {
        return null;
    }


    private void setAddress(ShoppingAddressListItemBean bean) {
        if (bean != null && !TextUtils.isEmpty(bean.getName()) && !TextUtils.isEmpty(bean.getPhone()) && !TextUtils.isEmpty(bean.getDetail())) {
            tv_address_name.setText(bean.getName());
            tv_address_phone.setText(bean.getPhone());
            tv_address_detail.setText(bean.getDetail());
            ll_address.setVisibility(View.VISIBLE);
            ll_add_addresss.setVisibility(View.GONE);
        } else {
            //
            ll_address.setVisibility(View.GONE);
            ll_add_addresss.setVisibility(View.VISIBLE);
            UIUtil.showToast("地址获取失败");
        }
    }

    private void goNext() {
        if (!TextUtils.isEmpty(orderId)) {
            Intent intent = new Intent(this, OrderDetailsActivity.class);
            intent.putExtra("orderId", orderId);
            startActivity(intent);
        } else {
            UIUtil.showToast("订单id为空");
        }

    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("提交订单");
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


    /**
     * 支付方式
     */
    public void payStyleDialog() {

        View view = View.inflate(CommitOrderActivity.this, R.layout.dialog_pay_type, null);
        showdialog(view);


        final CheckBox aliCheck = (CheckBox) view.findViewById(R.id.cb_ali);
        final CheckBox wxCheck = (CheckBox) view.findViewById(R.id.cb_wx);
        final CheckBox offLineCheck = (CheckBox) view.findViewById(R.id.cb_off);
        final Button commit = (Button) view.findViewById(R.id.bt_commit);

        aliCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    wxCheck.setChecked(false);
                    offLineCheck.setChecked(false);
                    payChannel = 0;
                } else {
                    aliCheck.setChecked(false);
                    payChannel = 3;
                }
            }
        });
        wxCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    aliCheck.setChecked(false);
                    offLineCheck.setChecked(false);
                    payChannel = 1;
                } else {
                    wxCheck.setChecked(false);
                    payChannel = 3;
                }
            }
        });
        offLineCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    wxCheck.setChecked(false);
                    aliCheck.setChecked(false);
                    payChannel = 2;
                } else {
                    offLineCheck.setChecked(false);
                    payChannel = 3;
                }
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UIUtil.isFastDoubleClick()) {
                    if (payChannel == 3) {
                        UIUtil.showToast("请选择支付方式");
                    } else if (payChannel == 2) {
                        myDialog.dismiss();
                        goNext();
                        finish();
                    } else {
                        getRSAOrderInfo();
                        myDialog.dismiss();
                    }
                }

            }
        });

    }

    /**
     * 配送方式
     */
    public void sendDialog() {

        View view = View.inflate(CommitOrderActivity.this, R.layout.dialog_publish_photo, null);
        showdialog(view);

        final TextView photo = (TextView) view.findViewById(R.id.photo);
        final TextView picture = (TextView) view.findViewById(R.id.picture);
        photo.setText("到店自取");
        picture.setText("快递配送");
        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deliverytype = 1;
                setDeliveryType();
                myDialog.dismiss();

            }
        });
        picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deliverytype = 0;
                setDeliveryType();
                myDialog.dismiss();
            }
        });

    }

    private void setDeliveryType() {
        if (deliverytype == 0) {
            tv_delivery_type.setText("快递配送");
        } else {
            tv_delivery_type.setText("店铺自取");
        }
    }

    private MyDialog myDialog;

    /**
     * 弹出dialog
     *
     * @param view
     */
    private void showdialog(View view) {

        myDialog = new MyDialog(this, 0, 0, view, R.style.dialog);
        myDialog.show();
        myDialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_add_addresss:
            case R.id.ll_address:
                Intent intent = new Intent(this, ShoppingAddressActivity.class);
                intent.putExtra("type", "getAddress");
                startActivityForResult(intent, ADD_REQUEST_CODE);
                break;
            case R.id.bt_commit:
                if (checkData())
                    if (!UIUtil.isFastDoubleClick()) {
                        if (tag.equals("rent")) {
                            commitRentOrder();
                        } else {
                            commitOrder();
                        }
                    }
                break;
            case R.id.ll_end_time:
                DialogUtils.showTimePcikerDialog(this, TimePickerView.Type.YEAR_MONTH_DAY, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        endDate = date;
//                        UIUtil.showToast(UIUtil.getTime(date));
                        setTimeArea();
                    }
                });
                break;
            case R.id.ll_begin_time:
                DialogUtils.showTimePcikerDialog(this, TimePickerView.Type.YEAR_MONTH_DAY, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        beginDate = date;
//                        UIUtil.showToast(UIUtil.getTime(date));
                        setTimeArea();
                    }
                });
                break;
            case R.id.rl_delivery_type:
                //
                sendDialog();
                break;

        }
    }

    private void setTimeArea() {
        if (beginDate != null) {
            tv_begin_time.setText(UIUtil.getTime(beginDate));
            tv_begin_week.setText(UIUtil.getWeekOfDate(beginDate));
        }
        if (endDate != null) {
            tv_end_time.setText(UIUtil.getTime(endDate));
            tv_end_week.setText(UIUtil.getWeekOfDate(endDate));
        }

        if (beginDate != null && endDate != null) {
            if (UIUtil.twoDateDistance(beginDate, endDate) < 0) {
                //
                beginDate = endDate;
                tv_begin_time.setText(UIUtil.getTime(beginDate));
                tv_begin_week.setText(UIUtil.getWeekOfDate(beginDate));
                UIUtil.showToast("请选择正确的时间");
            }
            days = (int) (UIUtil.twoDateDistance(beginDate, endDate));
            setRentPrice();
            tv_during_days.setText((UIUtil.twoDateDistance(beginDate, endDate)) + "天");
        }
    }

    private void setDefaultTimeArea() {
        if (beginDate == null) {
            beginDate = new Date();
        }
        if (endDate == null) {
            endDate = new Date();
        }
        tv_begin_time.setText(UIUtil.getTime(beginDate));
        tv_begin_week.setText(UIUtil.getWeekOfDate(beginDate));
        tv_end_time.setText(UIUtil.getTime(endDate));
        tv_end_week.setText(UIUtil.getWeekOfDate(endDate));
        if (beginDate != null && endDate != null) {
            tv_during_days.setText((UIUtil.twoDateDistance(beginDate, endDate)) + "天");
            days = (int) (UIUtil.twoDateDistance(beginDate, endDate));
            setRentPrice();
        }
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(tv_address_name.getText().toString())) {
            UIUtil.showToast("请选择收货地址");
            return false;
        }
        return true;
    }

    /**
     * 获取加密订单信息
     */
    private void getRSAOrderInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("userId", BaseContext.getInstance().getUserInfo().userId);
        map.put("payChannel", payChannel + "");
        map.put("totalMoney", price + "");
        map.put("orderType", orderType + "");
        DialogUtils.showDialog(CommitOrderActivity.this, "加载...", false);
        getRsaOrderCall = RestAdapterManager.getApi().getRsaOrderInfo(map);
        getRsaOrderCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    LogUtils.e(response.body().getMsg());
                    pay(response.body().getData());
                } else {
                    UIUtil.showToast("支付失败");
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("支付失败");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                UIUtil.showToast("支付失败");
            }
        });
    }

    /**
     * 提交购买订单
     */
    private void commitOrder() {
        if (!NetUtil.isNetworkConnected(this)) {
            UIUtil.showToast(R.string.net_state_error);
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("ordername", tv_address_name.getText().toString());
        map.put("orderphone", tv_address_phone.getText().toString());
        map.put("orderaddress", tv_address_detail.getText().toString());
        map.put("goodsid", goodsBean.getId() + "");
        map.put("goodsprice", goodsBean.getPrice() + "");
        map.put("count", tvNum.getText().toString());
        map.put("deliverytype", deliverytype + "");
        map.put("payType", orderType + "");
        map.put("totalmoney", price + "");
        map.put("userid", BaseContext.getInstance().getUserInfo().userId);
        Call<SuperBean<String>> commitOrderCall;
        DialogUtils.showDialog(CommitOrderActivity.this, "获取订单...", false);
        commitOrderCall = RestAdapterManager.getApi().getCommitOrder(map);
        commitOrderCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    LogUtils.e(response.body().getMsg());
                    DialogUtils.closeDialog();
                    orderId = response.body().getData();
                    payStyleDialog();

                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast(t.getMessage());
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                try {
                    UIUtil.showToast(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 提交租赁订单
     */
    private void commitRentOrder() {
        if (!NetUtil.isNetworkConnected(this)) {
            UIUtil.showToast(R.string.net_state_error);
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("ordername", tv_address_name.getText().toString());
        map.put("orderphone", tv_address_phone.getText().toString());
        map.put("orderaddress", tv_address_detail.getText().toString());
        map.put("goodsid", goodsBean.getId() + "");
        map.put("price", goodsBean.getPrice() + "");
        map.put("count", "1");
        map.put("starttime", UIUtil.getTime(beginDate));
        map.put("endtime", UIUtil.getTime(endDate));
        map.put("deliverytype", deliverytype + "");
        map.put("payType", orderType + "");
        map.put("totalmoney", price + "");
        map.put("userid", BaseContext.getInstance().getUserInfo().userId);
        map.put("deposit", goodsBean.getDeposit() + "");
        DialogUtils.showDialog(CommitOrderActivity.this, "获取订单...", false);
        commitRentCall = RestAdapterManager.getApi().getRentOrder(map);
        commitRentCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    LogUtils.e(response.body().getMsg());
                    DialogUtils.closeDialog();
                    orderId = response.body().getData();
                    payStyleDialog();

                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast(t.getMessage());
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                try {
                    UIUtil.showToast(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 支付
     *
     * @param string
     */
    private void pay(String string) {
        JPay.getIntance(this).toPay(JPay.PayMode.ALIPAY, string, new JPay.JPayListener() {
            @Override
            public void onPaySuccess() {
                DialogUtils.closeDialog();
                goNext();
                finish();
                Toast.makeText(CommitOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayError(int error_code, String message) {
                DialogUtils.closeDialog();
                Toast.makeText(CommitOrderActivity.this, "支付失败>" + error_code + " " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayCancel() {
                DialogUtils.closeDialog();
                Toast.makeText(CommitOrderActivity.this, "取消了支付", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (commitRentCall != null) {
            commitRentCall.cancel();
        }
        if (commitOrderCall != null) {
            commitOrderCall.cancel();
        }
        super.onDestroy();
    }
}
