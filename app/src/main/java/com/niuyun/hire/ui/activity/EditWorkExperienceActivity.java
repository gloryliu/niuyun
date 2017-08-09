package com.niuyun.hire.ui.activity;

import android.graphics.Color;
import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindView;

/**
 * Created by chen.zhiwei on 2017-8-9.
 */

public class EditWorkExperienceActivity extends BaseActivity{
    @BindView(R.id.title_view)
    TitleBar titleView;
    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_edit_work_experience;
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

        titleView.setTitle("工作经历");
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
            }
        });
        titleView.setImmersive(true);
    }
}
