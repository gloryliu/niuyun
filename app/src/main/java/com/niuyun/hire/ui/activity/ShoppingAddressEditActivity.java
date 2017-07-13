package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.TitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 编辑收货地址
 * Created by chen.zhiwei on 2017-6-23.
 */

public class ShoppingAddressEditActivity extends BaseActivity {
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.et_address_name)
    EditText etAddressName;
    @BindView(R.id.et_address_phone)
    EditText etAddressPhone;
    @BindView(R.id.et_address_detail)
    EditText etAddressDetail;
    private String tag = "0";//0新增，1编辑
    private String id = "";
    Call<String> call;
    @BindView(R.id.tv_commit)
    Button tv_commit;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_add_shopping_address;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tag = bundle.getString("flag", "0");

            etAddressName.setText(bundle.getString("name", ""));
            etAddressPhone.setText(bundle.getString("phone", ""));
            etAddressDetail.setText(bundle.getString("detail", ""));
            id = bundle.getString("id", "");
        }
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    commitData();
                }
            }
        });

    }

    @Override
    public void loadData() {

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

    private void commitData() {
        DialogUtils.showDialog(ShoppingAddressEditActivity.this, "上传中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("phone", etAddressPhone.getText().toString());
        map.put("name", etAddressName.getText().toString());
        map.put("detail", etAddressDetail.getText().toString());
        map.put("userId", BaseContext.getInstance().getUserInfo().userId);
        if (tag.equals("1")) {

            map.put("id", id);//如果是编辑的话是之前的地址id
            call = RestAdapterManager.getApi().editAddress(map);
        } else {
            call = RestAdapterManager.getApi().addAddress(map);
        }
        call.enqueue(new JyCallBack<String>() {
            @Override
            public void onSuccess(Call<String> call, Response<String> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null) {
                    if (response.body().contains("1000")) {
                        if (tag.equals("1")) {
                            //编辑
                            UIUtil.showToast("编辑成功");
                        } else {
                            //增加
                            UIUtil.showToast("增加成功");
                        }
                        EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.AddressUpdateSuccess));
                        finish();
                    } else {
                        UIUtil.showToast("修改失败，请稍后重试");
                    }
                } else {
                    UIUtil.showToast("修改失败，请稍后重试");
                }
            }

            @Override
            public void onError(Call<String> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("修改失败，请稍后重试");
            }

            @Override
            public void onError(Call<String> call, Response<String> response) {
                DialogUtils.closeDialog();
                UIUtil.showToast("修改失败，请稍后重试");
            }
        });
    }


    private boolean checkData() {
        if (TextUtils.isEmpty(etAddressName.getText().toString())) {
            UIUtil.showToast("姓名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(etAddressPhone.getText().toString())) {
            UIUtil.showToast("联系方式不能为空");
            return false;
        }
        if (TextUtils.isEmpty(etAddressDetail.getText().toString())) {
            UIUtil.showToast("详细地址不能为空");
            return false;
        }
        return true;
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("编辑地址");
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
