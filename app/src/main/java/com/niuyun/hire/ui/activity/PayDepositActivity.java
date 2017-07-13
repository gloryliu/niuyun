package com.niuyun.hire.ui.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
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
import com.niuyun.hire.ui.adapter.PayMemberRankItemAdapter;
import com.niuyun.hire.ui.bean.MemberRankBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.listerner.PayMemberItemOnClickListerner;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.NetUtil;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 缴纳押金
 * Created by chen.zhiwei on 2017-6-27.
 */

public class PayDepositActivity extends BaseActivity {
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.tv_pay_number)
    TextView tv_pay_number;
    @BindView(R.id.bt_pay)
    Button bt_pay;
    private List<MemberRankBean> list = new ArrayList<>();
    private RecyclerView rv_list;
    private PayMemberRankItemAdapter adapter;
    private PopupWindow popupWindow;
    private Call<SuperBean<String>> getOrderIdcall;
    private MemberRankBean memberBean;
    private String orderId;
    private int payChannel = 0;//支付通道0为支付宝1为微信
    private int totalMoney;//订单总额单位是分
    private int orderType;//订单类型0为购买1租，2为会员押金支付
    private Call<SuperBean<String>> getRsaOrderCall;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_pay_deposit;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            list = (List<MemberRankBean>) bundle.getSerializable("RankList");
        }
    }

    @Override
    public void loadData() {
        tv_pay_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupWindow(tv_pay_number);
            }
        });
        bt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(tv_pay_number.getText())) {
                    if (!UIUtil.isFastDoubleClick()) {
                        setGetOrderId();
                    }
                } else {
                    UIUtil.showToast("请选择价格");
                }
            }
        });
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
     * 支付方式
     */
    public void payStyleDialog() {

        View view = View.inflate(PayDepositActivity.this, R.layout.dialog_pay_type, null);
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
                    } else {
                        getRSAOrderInfo();
                        myDialog.dismiss();
                    }
                }

            }
        });

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

    private void setGetOrderId() {
        if (!NetUtil.isNetworkConnected(this)) {
            UIUtil.showToast(R.string.net_state_error);
            return;
        }
        if (memberBean == null) {
            UIUtil.showToast("请选择支付金额");
            return;
        }
        DialogUtils.showDialog(this, "加载中", false);
        Map<String, String> map = new HashMap<>();
        map.put("gradeid", memberBean.getId() + "");
        map.put("grade", memberBean.getGrade() + "");
        map.put("name", memberBean.getName());
        map.put("money", memberBean.getMoney() + "");
        map.put("userid", BaseContext.getInstance().getUserInfo().userId);
        getOrderIdcall = RestAdapterManager.getApi().getDepositOrder(map);
        getOrderIdcall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null) {
                    if (response.body().getCode() == Constants.successCode && !TextUtils.isEmpty(response.body().getData())) {
                        orderId = response.body().getData();
                        payStyleDialog();
                    } else {

                        UIUtil.showToast("获取订单失败，请稍后重试");
                    }
                } else {
                    UIUtil.showToast("获取订单失败，请稍后重试");
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("获取订单失败，请稍后重试");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                UIUtil.showToast("获取订单失败，请稍后重试");
            }
        });
    }

    /**
     * 获取加密订单信息
     */
    private void getRSAOrderInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("userId", BaseContext.getInstance().getUserInfo().userId);
        map.put("payChannel", payChannel + "");
        map.put("totalMoney", memberBean.getMoney() + "");
        map.put("orderType", orderType + "");
        DialogUtils.showDialog(PayDepositActivity.this, "加载...", false);
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
     * 支付
     *
     * @param string
     */
    private void pay(String string) {
        JPay.getIntance(this).toPay(JPay.PayMode.ALIPAY, string, new JPay.JPayListener() {
            @Override
            public void onPaySuccess() {
                EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.PAY_MEMBER_SUCCESS));
                DialogUtils.closeDialog();
                Toast.makeText(PayDepositActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onPayError(int error_code, String message) {
                DialogUtils.closeDialog();
                Toast.makeText(PayDepositActivity.this, "支付失败>" + error_code + " " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayCancel() {
                DialogUtils.closeDialog();
                Toast.makeText(PayDepositActivity.this, "取消了支付", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showpopupWindow(View v) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.popwindow_layout, null);
        rv_list = (RecyclerView) view.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        if (adapter == null) {
            adapter = new PayMemberRankItemAdapter(this);
            rv_list.setAdapter(adapter);
            adapter.addList(list);
            adapter.setClickListerner(new PayMemberItemOnClickListerner() {
                @Override
                public void onClick(MemberRankBean bean) {
                    if (bean != null) {
                        memberBean = bean;
                        tv_pay_number.setText(bean.getMoney() / 100.00 + "");
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                }
            });
        }
        if (popupWindow == null) {
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        }
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_background));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.MyPopupWindow_anim_style);


        // PopupWindow弹出位置
        popupWindow.showAsDropDown(v, Gravity.CENTER, 0, 0);

//        backgroundAlpha(0.5f);
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//                backgroundAlpha(1f);
//            }
//        });
    }

    // 设置屏幕透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0~1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("缴纳押金");
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
        if (getOrderIdcall != null) {
            getOrderIdcall.cancel();
        }
        super.onDestroy();
    }
}
