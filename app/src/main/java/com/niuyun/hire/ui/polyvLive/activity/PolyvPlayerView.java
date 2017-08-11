package com.niuyun.hire.ui.polyvLive.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.srt.PolyvSRTItemVO;
import com.easefun.polyvsdk.video.PolyvMediaInfoType;
import com.easefun.polyvsdk.video.PolyvPlayErrorReason;
import com.easefun.polyvsdk.video.PolyvVideoView;
import com.easefun.polyvsdk.video.auxiliary.PolyvAuxiliaryVideoView;
import com.easefun.polyvsdk.video.listener.IPolyvOnCompletionListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureClickListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureLeftDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureLeftUpListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureRightDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureRightUpListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureSwipeLeftListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureSwipeRightListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnInfoListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnPreloadPlayListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnPreparedListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoPlayErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoSRTListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoStatusListener;
import com.niuyun.hire.R;
import com.niuyun.hire.ui.polyvLive.fragment.PolyvPlayerDanmuFragment;
import com.niuyun.hire.ui.polyvLive.player.PolyvPlayerLightView;
import com.niuyun.hire.ui.polyvLive.player.PolyvPlayerMediaControllerSignIn;
import com.niuyun.hire.ui.polyvLive.player.PolyvPlayerPreviewView;
import com.niuyun.hire.ui.polyvLive.player.PolyvPlayerProgressView;
import com.niuyun.hire.ui.polyvLive.player.PolyvPlayerVolumeView;

/**
 * Created by chen.zhiwei on 2017-7-28.
 */

public class PolyvPlayerView extends LinearLayout {

    private static final String TAG = PolyvPlayerActivity.class.getSimpleName();
    private PolyvPlayerDanmuFragment danmuFragment;//弹幕
    /**
     * 播放器的parentView
     */
    private RelativeLayout viewLayout = null;
    /**
     * 播放主视频播放器
     */
    private PolyvVideoView videoView = null;
    /**
     * 视频控制栏
     */
    private PolyvPlayerMediaControllerSignIn mediaController = null;
    /**
     * 字幕文本视图
     */
    private TextView srtTextView = null;
    /**
     * 普通问答界面
     */
//    private PolyvPlayerQuestionView questionView = null;
    /**
     * 语音问答界面
     */
//    private PolyvPlayerAuditionView auditionView = null;
    /**
     * 用于播放广告片头的播放器
     */
    private PolyvAuxiliaryVideoView auxiliaryVideoView = null;
    /**
     * 视频广告，视频片头加载缓冲视图
     */
    private ProgressBar auxiliaryLoadingProgress = null;
    /**
     * 图片广告界面
     */
//    private PolyvPlayerAuxiliaryView auxiliaryView = null;
    /**
     * 广告倒计时
     */
    private TextView advertCountDown = null;
    /**
     * 缩略图界面
     */
    private PolyvPlayerPreviewView firstStartView = null;
    /**
     * 手势出现的亮度界面
     */
    private PolyvPlayerLightView lightView = null;
    /**
     * 手势出现的音量界面
     */
    private PolyvPlayerVolumeView volumeView = null;
    /**
     * 手势出现的进度界面
     */
    private PolyvPlayerProgressView progressView = null;
    /**
     * 视频加载缓冲视图
     */
    private ProgressBar loadingProgress = null;

    private int fastForwardPos = 0;
    private boolean isPlay = false;

    private Context context;

    //播放模式
    private int playModeCode;

    private String vid = "";//视频编号

    private int bitrate;//比特率

    private boolean isMustFromLocal = false;

    private boolean startNow;//是否立即开始播放

    private Activity activity;

    private FragmentTransaction transaction;

    public PolyvPlayerView(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public PolyvPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.polyvPlayerView);
            playModeCode = ta.getInt(R.styleable.polyvPlayerView_playModeCode, 4);
            startNow = ta.getBoolean(R.styleable.polyvPlayerView_startNow, false);
        }
        View.inflate(context, R.layout.polyv_view_player, this);



        PlayMode playMode = PlayMode.getPlayMode(playModeCode);
        if (playMode == null)
            playMode = PlayMode.portrait;
//            String vid = getIntent().getStringExtra("value");
//            int bitrate = getIntent().getIntExtra("bitrate", PolyvBitRate.ziDong.getNum());//比特率

//            boolean isMustFromLocal = getIntent().getBooleanExtra("isMustFromLocal", false);//不知道是啥


    }

    /**
     * 设置视频播放地址
     *
     * @return
     */
    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
        play(vid, bitrate, startNow, isMustFromLocal);
    }

    public int getPlayModeCode() {
        return playModeCode;
    }

    /**
     * 设置播放方向
     * @param playModeCode
     */
    public void setPlayModeCode(int playModeCode) {
        switch (playModeCode) {
            case 3:
                mediaController.changeToLandscape();
                break;
            case 4:
                mediaController.changeToPortrait();
                break;
        }
        this.playModeCode = playModeCode;
    }


    private void addFragment() {
        danmuFragment = new PolyvPlayerDanmuFragment();
//        FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_danmu,danmuFragment,"danmuFragment");
        // 网校的在线视频才添加下面的控件
//        if (!getIntent().getBooleanExtra(PolyvMainActivity.IS_VLMS_ONLINE, false)) {
//            ft.commit();
//            return;
//        }
//        topFragment = new PolyvPlayerTopFragment();
//        topFragment.setArguments(getIntent().getExtras());
//        tabFragment = new PolyvPlayerTabFragment();
//        viewPagerFragment = new PolyvPlayerViewPagerFragment();
//        ft.add(R.id.fl_top, topFragment, "topFragmnet");
//        ft.add(R.id.fl_tab, tabFragment, "tabFragment");
//        ft.add(R.id.fl_viewpager, viewPagerFragment, "viewPagerFragment");
        transaction.commit();
    }

    public FragmentTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(FragmentTransaction transaction) {
        this.transaction = transaction;
        addFragment();
        findIdAndNew();
        initView();
        switch (playModeCode) {
            case 3:
                mediaController.changeToLandscape();
                break;
            case 4:
                mediaController.changeToPortrait();
                break;
        }
    }

    private void findIdAndNew() {
        viewLayout = (RelativeLayout) findViewById(R.id.view_layout);
        videoView = (PolyvVideoView) findViewById(R.id.polyv_video_view);
        mediaController = (PolyvPlayerMediaControllerSignIn) findViewById(R.id.polyv_player_media_controller);
        srtTextView = (TextView) findViewById(R.id.srt);
//        questionView = (PolyvPlayerQuestionView) findViewById(R.id.polyv_player_question_view);
//        auditionView = (PolyvPlayerAuditionView) findViewById(R.id.polyv_player_audition_view);
        auxiliaryVideoView = (PolyvAuxiliaryVideoView) findViewById(R.id.polyv_auxiliary_video_view);
        auxiliaryLoadingProgress = (ProgressBar) findViewById(R.id.auxiliary_loading_progress);
//        auxiliaryView = (PolyvPlayerAuxiliaryView) findViewById(R.id.polyv_player_auxiliary_view);
        advertCountDown = (TextView) findViewById(R.id.count_down);
        firstStartView = (PolyvPlayerPreviewView) findViewById(R.id.polyv_player_first_start_view);
        lightView = (PolyvPlayerLightView) findViewById(R.id.polyv_player_light_view);
        volumeView = (PolyvPlayerVolumeView) findViewById(R.id.polyv_player_volume_view);
        progressView = (PolyvPlayerProgressView) findViewById(R.id.polyv_player_progress_view);
        loadingProgress = (ProgressBar) findViewById(R.id.loading_progress);

        mediaController.initConfig(viewLayout);
        mediaController.setDanmuFragment(danmuFragment);
//        questionView.setPolyvVideoView(videoView);
//        auditionView.setPolyvVideoView(videoView);
        auxiliaryVideoView.setPlayerBufferingIndicator(auxiliaryLoadingProgress);
//        auxiliaryView.setPolyvVideoView(videoView);
//        auxiliaryView.setDanmakuFragment(danmuFragment);
        videoView.setMediaController(mediaController);
        videoView.setAuxiliaryVideoView(auxiliaryVideoView);
        videoView.setPlayerBufferingIndicator(loadingProgress);
    }

    private void initView() {
        videoView.setOpenAd(true);
        videoView.setOpenTeaser(true);
        videoView.setOpenQuestion(true);
        videoView.setOpenSRT(true);
        videoView.setOpenPreload(true, 2);
        videoView.setAutoContinue(true);
        videoView.setNeedGestureDetector(true);

        videoView.setOnPreparedListener(new IPolyvOnPreparedListener2() {
            @Override
            public void onPrepared() {
                mediaController.preparedView();
                // 没开预加载在这里开始弹幕
                // danmuFragment.start();
            }
        });

        videoView.setOnPreloadPlayListener(new IPolyvOnPreloadPlayListener() {
            @Override
            public void onPlay() {
                // 开启预加载在这里开始弹幕
                danmuFragment.start();
            }
        });

        videoView.setOnInfoListener(new IPolyvOnInfoListener2() {
            @Override
            public boolean onInfo(int what, int extra) {
                switch (what) {
                    case PolyvMediaInfoType.MEDIA_INFO_BUFFERING_START:
                        danmuFragment.pause(false);
                        break;
                    case PolyvMediaInfoType.MEDIA_INFO_BUFFERING_END:
                        danmuFragment.resume(false);
                        break;
                }

                return true;
            }
        });

        videoView.setOnVideoStatusListener(new IPolyvOnVideoStatusListener() {
            @Override
            public void onStatus(int status) {
                if (status < 60) {
                    Toast.makeText(context, String.format("状态错误 %d", status), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, String.format("状态正常 %d", status));
                }
            }
        });

        videoView.setOnVideoPlayErrorListener(new IPolyvOnVideoPlayErrorListener2() {
            @Override
            public boolean onVideoPlayError(@PolyvPlayErrorReason.PlayErrorReason int playErrorReason) {
                String message = "";
                switch (playErrorReason) {
                    case PolyvPlayErrorReason.NETWORK_DENIED:
                        message = "无法连接网络，请连接网络后播放";
                        break;
                    case PolyvPlayErrorReason.OUT_FLOW:
                        message = "流量超标";
                        break;
                    case PolyvPlayErrorReason.TIMEOUT_FLOW:
                        message = "账号过期";
                        break;
                    case PolyvPlayErrorReason.LOCAL_VIEWO_ERROR:
                        message = "本地视频文件损坏，请重新下载";
                        break;
                    case PolyvPlayErrorReason.START_ERROR:
                        message = "播放异常，请重新播放";
                        break;
                    case PolyvPlayErrorReason.NOT_PERMISSION:
                        message = "非法播放";
                        break;
                    case PolyvPlayErrorReason.USER_TOKEN_ERROR:
                        message = "请先设置播放凭证，再进行播放";
                        break;
                    case PolyvPlayErrorReason.VIDEO_STATUS_ERROR:
                        message = "视频状态异常，无法播放";
                        break;
                    case PolyvPlayErrorReason.VID_ERROR:
                        message = "视频id不正确，请设置正确的视频id进行播放";
                        break;
                    case PolyvPlayErrorReason.BITRATE_ERROR:
                        message = "清晰度不正确，请设置正确的清晰度进行播放";
                        break;
                    case PolyvPlayErrorReason.VIDEO_NULL:
                        message = "视频信息加载失败，请重新播放";
                        break;
                    case PolyvPlayErrorReason.MP4_LINK_NUM_ERROR:
                        message = "MP4 播放地址服务器数据错误";
                        break;
                    case PolyvPlayErrorReason.M3U8_LINK_NUM_ERROR:
                        message = "HLS 播放地址服务器数据错误";
                        break;
                    case PolyvPlayErrorReason.HLS_SPEED_TYPE_NULL:
                        message = "请设置播放速度";
                        break;
                    case PolyvPlayErrorReason.NOT_LOCAL_VIDEO:
                        message = "找不到本地下载的视频文件，请连网后重新下载";
                        break;
                    case PolyvPlayErrorReason.HLS_15X_INDEX_EMPTY:
                        message = "视频不支持1.5倍自动清晰度播放";
                        break;
                    case PolyvPlayErrorReason.HLS_15X_ERROR:
                        message = "视频不支持1.5倍当前清晰度播放";
                        break;
                    case PolyvPlayErrorReason.HLS_15X_URL_ERROR:
                        message = "1.5倍当前清晰度视频正在编码中";
                        break;
                    case PolyvPlayErrorReason.M3U8_15X_LINK_NUM_ERROR:
                        message = "HLS 1.5倍播放地址服务器数据错误";
                        break;
                    case PolyvPlayErrorReason.CHANGE_EQUAL_BITRATE:
                        message = "切换清晰度相同，请选择其它清晰度";
                        break;
                    case PolyvPlayErrorReason.CHANGE_EQUAL_HLS_SPEED:
                        message = "切换播放速度相同，请选择其它播放速度";
                        break;
                    case PolyvPlayErrorReason.CAN_NOT_CHANGE_BITRATE:
                        message = "未开始播放视频不能切换清晰度，请先播放视频";
                        break;
                    case PolyvPlayErrorReason.CAN_NOT_CHANGE_HLS_SPEED:
                        message = "未开始播放视频不能切换播放速度，请先播放视频";
                        break;
                    case PolyvPlayErrorReason.QUESTION_ERROR:
                        message = "视频问答数据加载失败，请重新播放";
                        break;
                    case PolyvPlayErrorReason.CHANGE_BITRATE_NOT_EXIST:
                        message = "视频没有这个清晰度，请切换其它清晰度";
                        break;
                    case PolyvPlayErrorReason.HLS_URL_ERROR:
                        message = "播放地址异常，无法播放";
                        break;
                    case PolyvPlayErrorReason.LOADING_VIDEO_ERROR:
                        message = "视频信息加载中出现异常，请重新播放";
                        break;
                    case PolyvPlayErrorReason.HLS2_URL_ERROR:
                        message = "播放地址异常，无法播放";
                        break;
                    case PolyvPlayErrorReason.TOKEN_NULL:
                        message = "播放授权获取失败，请重新播放";
                        break;
                    case PolyvPlayErrorReason.EXCEPTION_COMPLETION:
                        message = "视频异常结束，请重新播放";
                        break;
                    case PolyvPlayErrorReason.WRITE_EXTERNAL_STORAGE_DENIED:
                        message = "检测到拒绝读取存储设备，请先为应用程序分配权限，再重新播放";
                        break;
                    default:
                        message = "播放异常，请联系管理员或者客服";
                        break;
                }

                message += "(error code " + playErrorReason + ")";
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("错误");
                builder.setMessage(message);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                builder.show();
                return true;
            }
        });

        videoView.setOnErrorListener(new IPolyvOnErrorListener2() {
            @Override
            public boolean onError() {
                Toast.makeText(context, "视频异常，请重新播放", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

//        videoView.setOnAdvertisementOutListener(new IPolyvOnAdvertisementOutListener2() {
//            @Override
//            public void onOut(@NonNull PolyvADMatterVO adMatter) {
//                auxiliaryView.show(adMatter);
//            }
//        });

//        videoView.setOnAdvertisementCountDownListener(new IPolyvOnAdvertisementCountDownListener() {
//            @Override
//            public void onCountDown(int num) {
//                advertCountDown.setText(String.format("广告也精彩：%d秒", num));
//                advertCountDown.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onEnd() {
//                advertCountDown.setVisibility(View.GONE);
//                auxiliaryView.hide();
//            }
//        });

//        videoView.setOnAdvertisementEventListener(new IPolyvOnAdvertisementEventListener2() {
//            @Override
//            public void onShow(PolyvADMatterVO adMatter) {
//                Log.i(TAG, "开始播放视频广告");
//            }
//
//            @Override
//            public void onClick(PolyvADMatterVO adMatter) {
//                if (!TextUtils.isEmpty(adMatter.getAddrUrl())) {
//                    try {
//                        new URL(adMatter.getAddrUrl());
//                    } catch (MalformedURLException e1) {
//                        Log.e(TAG, PolyvSDKUtil.getExceptionFullMessage(e1, -1));
//                        return;
//                    }
//
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(adMatter.getAddrUrl()));
//                    context.startActivity(intent);
//                }
//            }
//        });

//        videoView.setOnQuestionOutListener(new IPolyvOnQuestionOutListener2() {
//            @Override
//            public void onOut(@NonNull PolyvQuestionVO questionVO) {
//                switch (questionVO.getType()) {
//                    case PolyvQuestionVO.TYPE_QUESTION:
//                        questionView.show(questionVO);
//                        break;
//
//                    case PolyvQuestionVO.TYPE_AUDITION:
//                        auditionView.show(questionVO);
//                        break;
//                }
//            }
//        });

//        videoView.setOnTeaserOutListener(new IPolyvOnTeaserOutListener() {
//            @Override
//            public void onOut(@NonNull String url) {
//                auxiliaryView.show(url);
//            }
//        });
//
//        videoView.setOnTeaserCountDownListener(new IPolyvOnTeaserCountDownListener() {
//            @Override
//            public void onEnd() {
//                auxiliaryView.hide();
//            }
//        });

//        videoView.setOnQuestionAnswerTipsListener(new IPolyvOnQuestionAnswerTipsListener() {
//
//            @Override
//            public void onTips(@NonNull String msg) {
//                questionView.showAnswerTips(msg);
//            }
//        });

        videoView.setOnCompletionListener(new IPolyvOnCompletionListener2() {
            @Override
            public void onCompletion() {
                danmuFragment.pause();
            }
        });

        videoView.setOnVideoSRTListener(new IPolyvOnVideoSRTListener() {
            @Override
            public void onVideoSRT(@Nullable PolyvSRTItemVO subTitleItem) {
                if (subTitleItem == null) {
                    srtTextView.setText("");
                } else {
                    srtTextView.setText(subTitleItem.getSubTitle());
                }

                srtTextView.setVisibility(View.VISIBLE);
            }
        });

        videoView.setOnGestureLeftUpListener(new IPolyvOnGestureLeftUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftUp %b %b brightness %d", start, end, videoView.getBrightness()));
                int brightness = videoView.getBrightness() + 5;
                if (brightness > 100) {
                    brightness = 100;
                }

                videoView.setBrightness(brightness);
                lightView.setViewLightValue(brightness, end);
            }
        });

        videoView.setOnGestureLeftDownListener(new IPolyvOnGestureLeftDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftDown %b %b brightness %d", start, end, videoView.getBrightness()));
                int brightness = videoView.getBrightness() - 5;
                if (brightness < 0) {
                    brightness = 0;
                }

                videoView.setBrightness(brightness);
                lightView.setViewLightValue(brightness, end);
            }
        });

        videoView.setOnGestureRightUpListener(new IPolyvOnGestureRightUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightUp %b %b volume %d", start, end, videoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = videoView.getVolume() + 10;
                if (volume > 100) {
                    volume = 100;
                }

                videoView.setVolume(volume);
                volumeView.setViewVolumeValue(volume, end);
            }
        });

        videoView.setOnGestureRightDownListener(new IPolyvOnGestureRightDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightDown %b %b volume %d", start, end, videoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = videoView.getVolume() - 10;
                if (volume < 0) {
                    volume = 0;
                }

                videoView.setVolume(volume);
                volumeView.setViewVolumeValue(volume, end);
            }
        });

        videoView.setOnGestureSwipeLeftListener(new IPolyvOnGestureSwipeLeftListener() {

            @Override
            public void callback(boolean start, boolean end) {
                // 左滑事件
                Log.d(TAG, String.format("SwipeLeft %b %b", start, end));
                if (fastForwardPos == 0) {
                    fastForwardPos = videoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos < 0)
                        fastForwardPos = 0;
                    videoView.seekTo(fastForwardPos);
                    danmuFragment.seekTo();
                    if (videoView.isCompletedState()) {
                        videoView.start();
                        danmuFragment.resume();
                    }
                    fastForwardPos = 0;
                } else {
                    fastForwardPos -= 10000;
                    if (fastForwardPos <= 0)
                        fastForwardPos = -1;
                }
                progressView.setViewProgressValue(fastForwardPos, videoView.getDuration(), end, false);
            }
        });

        videoView.setOnGestureSwipeRightListener(new IPolyvOnGestureSwipeRightListener() {

            @Override
            public void callback(boolean start, boolean end) {
                // 右滑事件
                Log.d(TAG, String.format("SwipeRight %b %b", start, end));
                if (fastForwardPos == 0) {
                    fastForwardPos = videoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos > videoView.getDuration())
                        fastForwardPos = videoView.getDuration();
                    videoView.seekTo(fastForwardPos);
                    danmuFragment.seekTo();
                    if (videoView.isCompletedState()) {
                        videoView.start();
                        danmuFragment.resume();
                    }
                    fastForwardPos = 0;
                } else {
                    fastForwardPos += 10000;
                    if (fastForwardPos > videoView.getDuration())
                        fastForwardPos = videoView.getDuration();
                }
                progressView.setViewProgressValue(fastForwardPos, videoView.getDuration(), end, true);
            }
        });

        videoView.setOnGestureClickListener(new IPolyvOnGestureClickListener() {
            @Override
            public void callback(boolean start, boolean end) {
                if (videoView.isInPlaybackState() && mediaController != null)
                    if (mediaController.isShowing())
                        mediaController.hide();
                    else
                        mediaController.show();
            }
        });
    }

    /**
     * 播放模式
     *
     * @author TanQu
     */
    public enum PlayMode {
        /**
         * 横屏
         */
        landScape(3),
        /**
         * 竖屏
         */
        portrait(4);

        private final int code;

        private PlayMode(int code) {
            this.code = code;
        }

        /**
         * 取得类型对应的code
         *
         * @return
         */
        public int getCode() {
            return code;
        }

        public static PlayMode getPlayMode(int code) {
            switch (code) {
                case 3:
                    return landScape;
                case 4:
                    return portrait;
            }

            return null;
        }
    }

    public void play(final String vid, final int bitrate, boolean startNow, final boolean isMustFromLocal) {
        if (TextUtils.isEmpty(vid)) return;

        videoView.release();
        srtTextView.setVisibility(View.GONE);
        mediaController.hide();
        loadingProgress.setVisibility(View.GONE);
//        questionView.hide();
//        auditionView.hide();
        auxiliaryVideoView.hide();
        auxiliaryLoadingProgress.setVisibility(View.GONE);
//        auxiliaryView.hide();
        advertCountDown.setVisibility(View.GONE);
        firstStartView.hide();

        danmuFragment.setVid(vid, videoView);
        if (startNow) {
            videoView.setVid(vid, bitrate, isMustFromLocal);
        } else {
            firstStartView.setCallback(new PolyvPlayerPreviewView.Callback() {

                @Override
                public void onClickStart() {
                    videoView.setVid(vid, bitrate, isMustFromLocal);
                }
            });

            firstStartView.show(vid);
        }
    }

    public void resume() {
        //回来后继续播放
        if (isPlay) {
            videoView.onActivityResume();
            danmuFragment.resume();
//            if (auxiliaryView.isPauseAdvert()) {
//                auxiliaryView.hide();
//            }
        }
        mediaController.resume();
    }

    public void pause() {
        mediaController.pause();
        progressView.hide();
        volumeView.hide();
        lightView.hide();
    }

    public void stop() {
        //弹出去暂停
        isPlay = videoView.onActivityStop();
        danmuFragment.pause();
    }

    public void destroy() {
        videoView.destroy();
//        questionView.hide();
//        auditionView.hide();
//        auxiliaryView.hide();
        firstStartView.hide();
        mediaController.disable();
    }
}
