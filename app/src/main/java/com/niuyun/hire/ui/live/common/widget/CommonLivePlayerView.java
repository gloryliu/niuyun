package com.niuyun.hire.ui.live.common.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.niuyun.hire.R;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.ui.live.common.activity.videopreview.TCVideoPreviewActivity;
import com.niuyun.hire.ui.live.common.utils.TCConstants;
import com.niuyun.hire.utils.UIUtil;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.io.File;
import java.util.Locale;

/**
 * Created by chen.zhiwei on 2017-9-29.
 */

public class CommonLivePlayerView extends LinearLayout implements View.OnClickListener, ITXLivePlayListener {
    private int playModeCode;
    private boolean startNow;


    ImageView mStartPreview;
    boolean mVideoPlay = false;
    boolean mVideoPause = false;
    boolean mAutoPause = false;
    boolean mVideoShowOrPause = true;

    private String mVideoPath;
    private String mCoverImagePath;
    ImageView mImageViewBg;
    private TXLivePlayer mTXLivePlayer = null;
    private TXLivePlayConfig mTXPlayConfig = null;
    private TXCloudVideoView mTXCloudVideoView;
    private SeekBar mSeekBar;
    private TextView mProgressTime;
    private long mTrackingTouchTS = 0;
    private boolean mStartSeek = false;
    private Context context;
    private ImageView btnPlay;
    private ImageView btnHWDecode;
    private ImageView btnOrientation;
    private ImageView btnRenderMode;
    private int mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
    private int mCurrentRenderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
    private boolean mHWDecode = false;
    private RecyclerViewCommonInterface<String> mFullScreenLister;

    public RecyclerViewCommonInterface<String> getmFullScreenLister() {
        return mFullScreenLister;
    }

    public void setmFullScreenLister(RecyclerViewCommonInterface<String> mFullScreenLister) {
        this.mFullScreenLister = mFullScreenLister;
    }

    //错误消息弹窗
    private TCVideoPreviewActivity.ErrorDialogFragment mErrDlgFragment;
    //视频时长（ms）
    private int mVideoDuration;
    private FragmentTransaction transaction;

    public FragmentTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(FragmentTransaction transaction) {
        this.transaction = transaction;
    }

    public CommonLivePlayerView(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public CommonLivePlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }


    public void setmVideoPath(String mVideoPath) {
        this.mVideoPath = mVideoPath;
        prepareUrl();
    }


    public void setmCoverImagePath(String mCoverImagePath) {
        this.mCoverImagePath = mCoverImagePath;
    }

    public int getmVideoDuration() {
        return mVideoDuration;
    }

    public void setmVideoDuration(int mVideoDuration) {
        this.mVideoDuration = mVideoDuration;
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.commonVideoView);
            playModeCode = ta.getInt(R.styleable.commonVideoView_playModeCode, 4);
            startNow = ta.getBoolean(R.styleable.commonVideoView_startNow, false);
        }
        View.inflate(context, R.layout.activity_common_video_preview, this);


        mErrDlgFragment = new TCVideoPreviewActivity.ErrorDialogFragment();


        mStartPreview = (ImageView) findViewById(R.id.record_preview);
        btnPlay = (ImageView) findViewById(R.id.btnPlay);
        btnHWDecode = (ImageView) findViewById(R.id.btnHWDecode);
        btnOrientation = (ImageView) findViewById(R.id.btnOrientation);
        btnRenderMode = (ImageView) findViewById(R.id.btnRenderMode);
        btnHWDecode.getBackground().setAlpha(mHWDecode ? 255 : 100);
        mStartPreview.setOnClickListener(this);
        btnHWDecode.setOnClickListener(this);
        btnOrientation.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnRenderMode.setOnClickListener(this);
        mTXLivePlayer = new TXLivePlayer(context);
        mTXPlayConfig = new TXLivePlayConfig();
        mTXCloudVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        mTXCloudVideoView.disableLog(true);
        mTXCloudVideoView.setOnClickListener(this);
//        mVideoPath = getIntent().getStringExtra(TCConstants.VIDEO_RECORD_VIDEPATH);
//        mCoverImagePath = getIntent().getStringExtra(TCConstants.VIDEO_RECORD_COVERPATH);
//        mVideoDuration = getIntent().getIntExtra(TCConstants.VIDEO_RECORD_DURATION, 0);
//        Log.i(TAG, "onCreate: CouverImagePath = " + mCoverImagePath);
        mImageViewBg = (ImageView) findViewById(R.id.cover);
        mProgressTime = (TextView) findViewById(R.id.progress_time);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
    }

    private void prepareUrl() {
        if (mCoverImagePath != null && !mCoverImagePath.isEmpty()) {
            Glide.with(context).load(Uri.fromFile(new File(mCoverImagePath)))
                    .into(mImageViewBg);
        }
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean bFromUser) {
                if (mProgressTime != null) {
                    mProgressTime.setText(String.format(Locale.CHINA, "%02d:%02d/%02d:%02d", (progress) / 60, (progress) % 60, (seekBar.getMax()) / 60, (seekBar.getMax()) % 60));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mStartSeek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mTXLivePlayer != null) {
                    mTXLivePlayer.seek(seekBar.getProgress());
                }
                mTrackingTouchTS = System.currentTimeMillis();
                mStartSeek = false;
            }
        });
//        startPlay();
    }


    private boolean startPlay() {
        mStartPreview.setBackgroundResource(R.drawable.icon_record_pause);
        mTXLivePlayer.setPlayerView(mTXCloudVideoView);
        mTXLivePlayer.setPlayListener(this);
        mTXLivePlayer.enableHardwareDecode(mHWDecode);
        mTXLivePlayer.setRenderRotation(mCurrentRenderRotation);
        mTXLivePlayer.setRenderMode(mCurrentRenderMode);

        mTXLivePlayer.setConfig(mTXPlayConfig);

        int result = mTXLivePlayer.startPlay(mVideoPath, TXLivePlayer.PLAY_TYPE_LOCAL_VIDEO); // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
        if (result != 0) {
            mStartPreview.setBackgroundResource(R.drawable.icon_record_start);
            return false;
        }
        mVideoPlay = true;
        return true;
    }

    private static ContentValues initCommonContentValues(File saveFile) {
        ContentValues values = new ContentValues();
        long currentTimeInSeconds = System.currentTimeMillis();
        values.put(MediaStore.MediaColumns.TITLE, saveFile.getName());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, saveFile.getName());
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, currentTimeInSeconds);
        values.put(MediaStore.MediaColumns.DATE_ADDED, currentTimeInSeconds);
        values.put(MediaStore.MediaColumns.DATA, saveFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.SIZE, saveFile.length());

        return values;
    }

    /**
     * 插入视频缩略图
     *
     * @param videoPath
     * @param coverPath
     */
    private void insertVideoThumb(String videoPath, String coverPath) {
        //以下是查询上面插入的数据库Video的id（用于绑定缩略图）
        //根据路径查询
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Thumbnails._ID},//返回id列表
                String.format("%s = ?", MediaStore.Video.Thumbnails.DATA), //根据路径查询数据库
                new String[]{videoPath}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String videoId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails._ID));
                //查询到了Video的id
                ContentValues thumbValues = new ContentValues();
                thumbValues.put(MediaStore.Video.Thumbnails.DATA, coverPath);//缩略图路径
                thumbValues.put(MediaStore.Video.Thumbnails.VIDEO_ID, videoId);//video的id 用于绑定
                //Video的kind一般为1
                thumbValues.put(MediaStore.Video.Thumbnails.KIND,
                        MediaStore.Video.Thumbnails.MINI_KIND);
                //只返回图片大小信息，不返回图片具体内容
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(coverPath, options);
                if (bitmap != null) {
                    thumbValues.put(MediaStore.Video.Thumbnails.WIDTH, bitmap.getWidth());//缩略图宽度
                    thumbValues.put(MediaStore.Video.Thumbnails.HEIGHT, bitmap.getHeight());//缩略图高度
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
                context.getContentResolver().insert(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, //缩略图数据库
                        thumbValues);
            }
            cursor.close();
        }
    }

    public void onResume() {
        mTXCloudVideoView.onResume();
        if (mVideoPlay && mAutoPause) {
            mTXLivePlayer.resume();
            mAutoPause = false;
        }
    }

    public void onPause() {
        mTXCloudVideoView.onPause();
        if (mVideoPlay && !mVideoPause) {
            mTXLivePlayer.pause();
            mAutoPause = true;
        }
    }


    public void onDestroy() {
        mTXCloudVideoView.onDestroy();
        stopPlay(true);
    }

    public void stopPlay(boolean clearLastFrame) {
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);
            mVideoPlay = false;
        }
    }

    public void onPlayEvent(int event, Bundle param) {
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.setLogText(null, param, event);
        }
        if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            if (mStartSeek) {
                return;
            }
            if (mImageViewBg.isShown()) {
                mImageViewBg.setVisibility(View.GONE);
            }
            int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
            int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);//单位为s
            long curTS = System.currentTimeMillis();
            // 避免滑动进度条松开的瞬间可能出现滑动条瞬间跳到上一个位置
            if (Math.abs(curTS - mTrackingTouchTS) < 500) {
                return;
            }
            mTrackingTouchTS = curTS;

            if (mSeekBar != null) {
                mSeekBar.setProgress(progress);
            }
            if (mProgressTime != null) {
                mProgressTime.setText(String.format(Locale.CHINA, "%02d:%02d/%02d:%02d", (progress) / 60, progress % 60, (duration) / 60, duration % 60));
            }

            if (mSeekBar != null) {
                mSeekBar.setMax(duration);
            }
        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {

            showErrorAndQuit(TCConstants.ERROR_MSG_NET_DISCONNECTED);

        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            if (mProgressTime != null) {
                mProgressTime.setText(String.format(Locale.CHINA, "%s", "00:00/00:00"));
            }
            if (mSeekBar != null) {
                mSeekBar.setProgress(0);
            }
            stopPlay(false);
            mImageViewBg.setVisibility(View.VISIBLE);
            startPlay();
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_preview:
            case R.id.btnPlay:
                if (mVideoPlay) {
                    if (mVideoPause) {
                        mTXLivePlayer.resume();
                        mStartPreview.setBackgroundResource(R.drawable.icon_record_pause);
                        btnPlay.setBackgroundResource(R.drawable.play_pause);
                        mVideoPause = false;
                    } else {
                        mTXLivePlayer.pause();
                        mStartPreview.setBackgroundResource(R.drawable.icon_record_start);
                        btnPlay.setBackgroundResource(R.drawable.play_start);
                        mVideoPause = true;
                    }
                } else {
                    btnPlay.setBackgroundResource(R.drawable.play_pause);
                    startPlay();
                }
                changeState();
                break;
            case R.id.btnRenderMode://填充
                if (mTXLivePlayer == null) {
                    return;
                }

                if (mCurrentRenderMode == TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN) {
                    mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
                    btnRenderMode.setBackgroundResource(R.drawable.fill_mode);
                    mCurrentRenderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
                } else if (mCurrentRenderMode == TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION) {
                    mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                    btnRenderMode.setBackgroundResource(R.drawable.adjust_mode);
                    mCurrentRenderMode = TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
                }
                break;
            case R.id.btnOrientation://横屏|竖屏

                if (mTXLivePlayer == null) {
                    return;
                }
                if (mCurrentRenderRotation == TXLiveConstants.RENDER_ROTATION_PORTRAIT) {
                    btnOrientation.setBackgroundResource(R.drawable.portrait);
                    mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_LANDSCAPE;
                } else if (mCurrentRenderRotation == TXLiveConstants.RENDER_ROTATION_LANDSCAPE) {
                    btnOrientation.setBackgroundResource(R.drawable.landscape);
                    mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
                }

                mTXLivePlayer.setRenderRotation(mCurrentRenderRotation);
                if (mFullScreenLister != null) {
                    mFullScreenLister.onClick(mCurrentRenderRotation+"");
                }
                break;
            case R.id.btnHWDecode://硬解码

                mHWDecode = !mHWDecode;
                btnHWDecode.getBackground().setAlpha(mHWDecode ? 255 : 100);

                if (mHWDecode) {
                    Toast.makeText(context, "已开启硬件解码加速，切换会重启播放流程!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "已关闭硬件解码加速，切换会重启播放流程!", Toast.LENGTH_SHORT).show();
                }

                if (mVideoPlay) {
//                    if (mLivePlayer != null) {
//                        mLivePlayer.enableHardwareDecode(mHWDecode);
//                    }
//                    stopPlayRtmp();
//                    mVideoPlay = startPlayRtmp();
                    if (mVideoPause) {
                        if (mTXCloudVideoView != null) {
                            mTXCloudVideoView.onResume();
                        }
                        mVideoPause = false;
                    }
                }

                break;
            case R.id.video_view:
                UIUtil.showToast("点击video");
                changeState();
                break;
            default:
                break;
        }
    }
//    private Handler handler = new Handler();


    private Runnable runnableRef = new Runnable() {
        public void run() {
            mStartPreview.setVisibility(GONE);
            mVideoShowOrPause = false;
        }
    };

    private void changeState() {
        mStartPreview.removeCallbacks(runnableRef);
        if (mVideoPause) {
            mVideoShowOrPause = true;
            mStartPreview.setVisibility(VISIBLE);
        } else {
            if (!mVideoShowOrPause) {
                mVideoShowOrPause = true;
                mStartPreview.setVisibility(VISIBLE);
                mStartPreview.postDelayed(runnableRef, 2000);
            } else {
                mVideoShowOrPause = false;
                mStartPreview.setVisibility(GONE);
            }
        }
    }

    public static class ErrorDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.ConfirmDialogStyle)
                    .setCancelable(true)
                    .setTitle(getArguments().getString("errorMsg"))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

//                            getActivity().finish();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            return alertDialog;
        }
    }

    protected void showErrorAndQuit(String errorMsg) {

        if (!mErrDlgFragment.isAdded() && !((Activity) context).isFinishing()) {
            Bundle args = new Bundle();
            args.putString("errorMsg", errorMsg);
            mErrDlgFragment.setArguments(args);
            mErrDlgFragment.setCancelable(false);

            //此处不使用用.show(...)的方式加载dialogfragment，避免IllegalStateException
//            FragmentTransaction transaction = transaction;
            if (transaction != null) {
                transaction.add(mErrDlgFragment, "loading");
                transaction.commitAllowingStateLoss();
            }

        }
    }
}
