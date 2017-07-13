package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.TitleBar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 实名制
 * Created by chen.zhiwei on 2017-6-22.
 */

public class CommitRealNameActivity extends BaseActivity {
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.user_name)
    EditText user_name;
    @BindView(R.id.et_id_number)
    EditText et_id_number;
    @BindView(R.id.rl_commit)
    RelativeLayout rl_commit;
    @BindView(R.id.bt_commit)
    Button bt_commit;
    private Call<String> call;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_real_name;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        if (BaseContext.getInstance().getUserInfo() != null && !TextUtils.isEmpty(BaseContext.getInstance().getUserInfo().ID) && !TextUtils.isEmpty(BaseContext.getInstance().getUserInfo().name)) {
            et_id_number.setText(BaseContext.getInstance().getUserInfo().ID);
            user_name.setText(BaseContext.getInstance().getUserInfo().name);
            et_id_number.setClickable(false);
            user_name.setClickable(false);
            rl_commit.setVisibility(View.GONE);
        }

    }

    @Override
    public void loadData() {
        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    commitData();
                }
            }
        });
    }

    private void commitData() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", BaseContext.getInstance().getUserInfo().userId);
        map.put("name", user_name.getText().toString());
        map.put("ID", et_id_number.getText().toString());
        call = RestAdapterManager.getApi().commitRealName(map);
        call.enqueue(new JyCallBack<String>() {
            @Override
            public void onSuccess(Call<String> call, Response<String> response) {
                ErrorMessageUtils.taostErrorMessage(CommitRealNameActivity.this, response.body(), "");
                if (response != null && response.body() != null && !TextUtils.isEmpty(response.body())) {
                    if (response.body().contains("1000")) {
                        finish();
                    }
                }
            }

            @Override
            public void onError(Call<String> call, Throwable t) {
                UIUtil.showToast("认证失败，请稍后重试");
            }

            @Override
            public void onError(Call<String> call, Response<String> response) {
                try {
                    ErrorMessageUtils.taostErrorMessage(CommitRealNameActivity.this, response.errorBody().string(), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(user_name.getText())) {
            UIUtil.showToast("姓名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(et_id_number.getText())) {
            UIUtil.showToast("身份证号不能为空");
            return false;
        }
        if (!UIUtil.isRealIDCard(et_id_number.getText().toString())) {
            UIUtil.showToast("请输入正确的身份证号码");
            return false;
        }
        return true;
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

    @Override
    protected void onDestroy() {
        if (call != null) {
            call.cancel();
        }
        super.onDestroy();
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("实名认证");
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
}
