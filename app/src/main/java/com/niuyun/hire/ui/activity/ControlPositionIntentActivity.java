package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/12.
 */

public class ControlPositionIntentActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    @BindView(R.id.bt_add)
    Button bt_add;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_control_position_intent;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        bt_add.setOnClickListener(this);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
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

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("管理求职意向");
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
        title_view.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        title_view.setActionTextColor(Color.WHITE);
        title_view.setImmersive(true);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_add:
                startActivity(new Intent(this, EditPositionIntentActivity.class));
                break;
        }
    }
}
