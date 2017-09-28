package com.niuyun.hire.ui.live.common.activity.videopreview;

import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.live.common.utils.TCConstants;
import com.niuyun.hire.utils.UIUtil;
import com.tencent.ugc.TXUGCPublish;
import com.tencent.ugc.TXUGCPublishTypeDef;

import butterknife.BindView;

/**
 * Created by chen.zhiwei on 2017-9-26.
 */

public class TCVideoPublisherActivity extends BaseActivity {
    private TXUGCPublish mVideoPublish;
    private String mCosSignature = "6LNhCBZZ0XwOIoa4fGa1bpL+FtdzZWNyZXRJZD1BS0lEOUM1bVUyc2RmVkNvRlVlVGdvbVpBWnB1dGtPcnJGQXYmY3VycmVudFRpbWVTdGFtcD0xNTA2NDc1NTk3JmV4cGlyZVRpbWU9MTUwNjY0ODM5NyZyYW5kb209MzkyMzQ5MjQ1";
    private String mVideoPath;
    private String mCoverPath;
    @BindView(R.id.tv_percent)
    TextView tv_percent;
    @BindView(R.id.seekbar)
    SeekBar seekbar;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_video_upload;
    }

    @Override
    public void initViewsAndEvents() {
        mVideoPath = getIntent().getStringExtra(TCConstants.VIDEO_RECORD_VIDEPATH);
        mCoverPath = getIntent().getStringExtra(TCConstants.VIDEO_RECORD_COVERPATH);
    }

    @Override
    public void loadData() {
        mVideoPublish = new TXUGCPublish(TCVideoPublisherActivity.this.getApplicationContext());
// 如果需要使用断点续传功能，需要传入一个字符串类型的 userId 作为唯一标识, 建议使用登录帐号
// mVideoPublish = new TXUGCPublish(TCVideoPublisherActivity.this.getApplicationContext(), userId);
        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        param.signature = mCosSignature;                        // 需要填写第四步中计算的上传签名
// 录制生成的视频文件路径, ITXVideoRecordListener 的 onRecordComplete 回调中可以获取
        param.videoPath = mVideoPath;
// 录制生成的视频首帧预览图，ITXVideoRecordListener 的 onRecordComplete 回调中可以获取
        param.coverPath = mCoverPath;

        mVideoPublish.setListener(new TXUGCPublishTypeDef.ITXVideoPublishListener() {
            @Override
            public void onPublishProgress(long uploadBytes, long totalBytes) {
                Log.e("Text", uploadBytes + "");
                tv_percent.setText(uploadBytes + "/" + totalBytes);
                seekbar.setClickable(false);
                seekbar.setProgress((int) (uploadBytes / totalBytes));
            }

            @Override
            public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult txPublishResult) {
                UIUtil.showToast("上传成功");
            }
        });
        mVideoPublish.publishVideo(param);
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

}
