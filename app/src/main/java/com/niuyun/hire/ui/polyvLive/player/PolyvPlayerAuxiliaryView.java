package com.niuyun.hire.ui.polyvLive.player;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.easefun.polyvsdk.live.video.PolyvLiveVideoView;
import com.easefun.polyvsdk.live.vo.PolyvLiveChannelVO;
import com.niuyun.hire.R;
import com.niuyun.hire.ui.polyvLive.util.AdmasterSdkUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 图片广告和图片片头视图，视频广告和视频片头在{@link com.easefun.polyvsdk.live.video.PolyvLiveVideoView}中已经处理
 *
 * @author TanQu 2016-11-15
 */
public class PolyvPlayerAuxiliaryView extends RelativeLayout {
    private Context mContext = null;
    private PolyvLiveVideoView mVideoView = null;
    private ImageView mAdvertisementImage = null;
    private ImageButton mStartBtn = null;
    private PolyvLiveChannelVO.ADMatter mADMatter = null;

    public PolyvPlayerAuxiliaryView(Context context) {
        this(context, null);
    }

    public PolyvPlayerAuxiliaryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolyvPlayerAuxiliaryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initViews();
    }

    public void setPolyvLiveVideoView(PolyvLiveVideoView mVideoView) {
        this.mVideoView = mVideoView;
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.polyv_player_auxiliary_view, this);
        mAdvertisementImage = (ImageView) findViewById(R.id.advertisement_image);
        mAdvertisementImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mADMatter == null) return;
                // 发送广告点击监测
                AdmasterSdkUtils.sendAdvertMonitor(mADMatter, AdmasterSdkUtils.MONITOR_CLICK);
                String path = mADMatter.getAddrUrl();
                if (!TextUtils.isEmpty(path)) {
                    try {
                        new URL(path);
                    } catch (MalformedURLException e) {
                        return;
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(path));
                    mContext.startActivity(intent);
                }
            }
        });

        mStartBtn = (ImageButton) findViewById(R.id.advertisement_start_btn);
        mStartBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mVideoView.start();
                hide();
            }
        });

    }

    /**
     * 设置图片并显示
     *
     * @param adMatter
     */
    public void show(PolyvLiveChannelVO.ADMatter adMatter) {
        mADMatter = adMatter;
        ImageLoadedrManager.getInstance().display(mContext,mADMatter.getMatterUrl(), mAdvertisementImage);
        //暂停图片广告不需要倒计时，是点击开始按钮继续
        if (PolyvLiveChannelVO.ADMatter.LOCATION_PAUSE.equals(adMatter.getLocation())) {
            mStartBtn.setVisibility(View.VISIBLE);
        } else {
            mStartBtn.setVisibility(View.GONE);
        }

        setVisibility(View.VISIBLE);
    }

    /**
     * 设置图片并显示
     *
     * @param url
     */
    public void show(String url) {
        mADMatter = null;
        ImageLoadedrManager.getInstance().display(mContext,url, mAdvertisementImage);
        mStartBtn.setVisibility(View.GONE);
        setVisibility(View.VISIBLE);
    }

    public boolean isPauseAdvert() {
        return mADMatter != null && PolyvLiveChannelVO.ADMatter.LOCATION_PAUSE.equals(mADMatter.getLocation());
    }

    /**
     * 隐藏
     */
    public void hide() {
        setVisibility(View.GONE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }
}
