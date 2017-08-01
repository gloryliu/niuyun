package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.index.MainActivity;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindView;

/**
 * 企业认证
 * Created by chen.zhiwei on 2017-7-31.
 */

public class EnterPriseCertificationActivity extends BaseActivity {
    @BindView(R.id.title_view)
    TitleBar titleView;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_enterprise_certification;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
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

        titleView.setTitle("企业认证");
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
        titleView.addAction(new TitleBar.TextAction("跳过") {
            @Override
            public void performAction(View view) {
                startActivity(new Intent(EnterPriseCertificationActivity.this, MainActivity.class));
                finish();
            }
        });
        titleView.setImmersive(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UIUtil.showToast("您可以选择跳过");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
