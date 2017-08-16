package com.niuyun.hire.ui.polyvLive.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.activity.EditResumeActivity;
import com.niuyun.hire.view.TitleBar;

import butterknife.BindView;

public class PolyvUploadActivity extends BaseActivity {
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.pv_play)
    PolyvPlayerView pv_play;
    @BindView(R.id.bt_next)
    Button bt_next;
    @BindView(R.id.rl_video_content)
    RelativeLayout rl_video_content;
    @BindView(R.id.rl_online_resume_content)
    RelativeLayout rl_online_resume_content;
    @BindView(R.id.tv_percent)
    TextView tv_percent;
    @BindView(R.id.ll_online_resume_title)
    RelativeLayout ll_online_resume_title;

    private void initView() {
        bt_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PolyvUploadActivity.this, PolyvUploadVideoScannerActivity.class);
                startActivityForResult(intent, Constants.GET_VIDEO_VID);
            }
        });
        ll_online_resume_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PolyvUploadActivity.this, EditResumeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.polyv_activity_upload;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        initView();
        setVid("");
        setData();

    }

    @Override
    public void loadData() {

    }

    @Override
    public boolean isRegistEventBus() {
        return true;
    }

    @Override
    public void onMsgEvent(EventBusCenter eventBusCenter) {
        if (eventBusCenter != null) {
            if (eventBusCenter.getEvenCode() == Constants.UPDATE_LIVE_RESUME) {
                //更新简历
                setData();
            }
        }
    }

    @Override
    protected View isNeedLec() {
        return null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.GET_VIDEO_VID:
                if (null != data && data.getData() != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String vid = bundle.getString("videoVid");
//                        setVid(vid);
                        setData();
                    }
                }

                break;
        }
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("视频简历");
        title_view.setTitleColor(Color.WHITE);
//        title_view.setLeftImageResource(R.mipmap.ic_title_back);
//        title_view.setLeftText("返回");
//        title_view.setLeftTextColor(Color.WHITE);
//        title_view.setLeftClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        title_view.setActionTextColor(Color.WHITE);
        title_view.addAction(new TitleBar.TextAction("添加本地视频") {
            @Override
            public void performAction(View view) {
                Intent intent = new Intent(PolyvUploadActivity.this, PolyvUploadVideoScannerActivity.class);
                intent.putExtra("type", "local");
                startActivityForResult(intent, Constants.GET_VIDEO_VID);
            }
        });
        title_view.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        title_view.setActionTextColor(Color.WHITE);
        title_view.setImmersive(true);


    }

    @Override
    protected void onResume() {
        super.onResume();
        pv_play.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pv_play.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pv_play.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pv_play.destroy();
    }

    private void setVid(String vid) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        android.app.FragmentTransaction transaction=getFragmentManager().beginTransaction();
        pv_play.setTransaction(ft);
//        pv_play.setVid("c538856ddeb0abe3b875545932c82c59_c");
        pv_play.setVid(vid);
    }

    private void setData() {
        if (BaseContext.getInstance().getUserInfo() != null) {
            tv_percent.setText("完善度：" + BaseContext.getInstance().getUserInfo().completePercent + "%");
            if (!TextUtils.isEmpty(BaseContext.getInstance().getUserInfo().video)) {
                rl_video_content.setVisibility(View.VISIBLE);
                rl_online_resume_content.setVisibility(View.GONE);
                setVid(BaseContext.getInstance().getUserInfo().video);
            } else {
                rl_video_content.setVisibility(View.GONE);
                rl_online_resume_content.setVisibility(View.VISIBLE);
            }
        }
    }
}
