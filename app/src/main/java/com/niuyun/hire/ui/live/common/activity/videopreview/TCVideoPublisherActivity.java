package com.niuyun.hire.ui.live.common.activity.videopreview;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UserInfoBean;
import com.niuyun.hire.ui.live.common.utils.TCConstants;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.tencent.ugc.TXUGCPublish;
import com.tencent.ugc.TXUGCPublishTypeDef;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-9-26.
 */

public class TCVideoPublisherActivity extends BaseActivity {
    private TXUGCPublish mVideoPublish;
    private String mCosSignature = "iLv6r3RwNb0xDo0Z+0M7XB8iXZNzZWNyZXRJZD1BS0lEOUM1bVUyc2RmVkNvRlVlVGdvbVpBWnB1dGtPcnJGQXYmY3VycmVudFRpbWVTdGFtcD0xNTA2NjUyNDc3JmV4cGlyZVRpbWU9MTUwNjgyNTI3NyZyYW5kb209NjE2NzMzNjE=";
    private String mVideoPath;
    private String mCoverPath;
    @BindView(R.id.tv_percent)
    TextView tv_percent;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    private String videoVid;

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
        getSign();

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void beginUpload() {
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
//                seekbar.setClickable(false);
                LogUtils.e((int) ((uploadBytes / totalBytes)*100) + "");
                seekbar.setProgress((int) ((uploadBytes / totalBytes)*100));
            }

            @Override
            public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult txPublishResult) {
                if (!TextUtils.isEmpty(txPublishResult.videoURL)){
                    videoVid=txPublishResult.videoURL;
                    BindUrl();
                }else {
                    UIUtil.showToast(txPublishResult.descMsg);
                }
            }
        });
        mVideoPublish.publishVideo(param);
    }

    private void getSign() {
        DialogUtils.showDialog(TCVideoPublisherActivity.this,"",false);
        Call<SuperBean<String>> getVideoUploadSign = RestAdapterManager.getApi().getVideoUploadSign();
        getVideoUploadSign.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    mCosSignature = response.body().getData();
                    beginUpload();
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                UIUtil.showToast("获取签名失败");
                DialogUtils.closeDialog();
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                UIUtil.showToast("获取签名失败");
                DialogUtils.closeDialog();
            }
        });
    }

    private void BindUrl() {
        Map<String, String> map = new HashMap<>();
        map.put("video", videoVid);
        map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
        Call<SuperBean<String>> commitVideoVid = RestAdapterManager.getApi().commitVideoVid(map);
        commitVideoVid.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                UIUtil.showToast(response.body().getMsg());
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    UserInfoBean infoBean=BaseContext.getInstance().getUserInfo();
                    UIUtil.showToast("上传成功");
                    infoBean.video=videoVid;
                    BaseContext.getInstance().updateUserInfo(infoBean);
                    Intent intent = new Intent();
                    intent.putExtra("videoVid", videoVid);
                    setResult(Constants.GET_VIDEO_VID, intent);
                    finish();
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("接口异常");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                DialogUtils.closeDialog();
                try {
                    ErrorMessageUtils.taostErrorMessage(TCVideoPublisherActivity.this,response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
