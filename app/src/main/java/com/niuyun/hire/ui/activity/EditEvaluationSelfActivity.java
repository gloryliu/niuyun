package com.niuyun.hire.ui.activity;

import android.graphics.Color;
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
import com.niuyun.hire.ui.bean.SuperBean;
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
 * Created by chen.zhiwei on 2017-8-9.
 */

public class EditEvaluationSelfActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.bt_save)
    Button bt_save;
    @BindView(R.id.et_describe)
    EditText et_describe;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_edit_evaluation_self;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        bt_save.setOnClickListener(this);
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

    private void initTitle() {

        titleView.setTitle("自我评价");
        titleView.setTitleColor(Color.WHITE);
        titleView.setLeftImageResource(R.mipmap.ic_title_back);
        titleView.setLeftText("返回");
        titleView.setLeftTextColor(Color.WHITE);
        titleView.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setActionTextColor(Color.WHITE);
        titleView.addAction(new TitleBar.TextAction("保存") {
            @Override
            public void performAction(View view) {
                UpPrepare();
            }
        });
        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_save:
                UpPrepare();
                break;
        }
    }

    private void UpPrepare() {
        if (!UIUtil.isFastDoubleClick()) {
            if (checkData()) {
                upLoadInfo();
            }
        }

    }

    private void upLoadInfo() {
        DialogUtils.showDialog(EditEvaluationSelfActivity.this, "上传中...", false);
        Map<String, String> map = new HashMap<>();
        map.put("specialty", et_describe.getText().toString());
        if (BaseContext.getInstance().getUserInfo() != null) {
            map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
        }

        Call<SuperBean<String>> addWorkExperienceCall = RestAdapterManager.getApi().addEvaluation(map);
        addWorkExperienceCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                try {
                    UIUtil.showToast(response.body().getMsg());
                } catch (Exception e) {
                }
                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.UPDATE_LIVE_RESUME));
                    finish();
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口错误");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口错误");
            }
        });
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(et_describe.getText().toString())) {
            UIUtil.showToast("描述不能为空");
            return false;
        }
        return true;
    }
}
