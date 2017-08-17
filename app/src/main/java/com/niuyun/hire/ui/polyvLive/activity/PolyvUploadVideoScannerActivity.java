package com.niuyun.hire.ui.polyvLive.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.easefun.polyvsdk.upload.IPolyvUploader;
import com.easefun.polyvsdk.upload.PolyvUploader;
import com.easefun.polyvsdk.upload.PolyvUploaderManager;
import com.easefun.polyvsdk.util.GetPathFromUri;
import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.bean.UserInfoBean;
import com.niuyun.hire.ui.polyvLive.bean.PolyvUploadInfo;
import com.niuyun.hire.ui.polyvLive.util.PolyvUploadSQLiteHelper;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.TitleBar;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/14.
 */

public class PolyvUploadVideoScannerActivity extends BaseActivity {
    private static PolyvUploadSQLiteHelper uploadSQLiteHelper;
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.bt_next)
    Button bt_next;
    @BindView(R.id.tv_size)
    TextView tv_size;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.tv_speed)
    TextView tv_speed;
    @BindView(R.id.pb_progress)
    ProgressBar pb_progress;
    @BindView(R.id.iv_pre)
    VideoView iv_pre;
    @BindView(R.id.iv_play)
    ImageView iv_play;
    private String filPaths;
    PolyvUploadInfo uploadInfo;
    private static Context context;
    private static final String UPLOADED = "已上传";
    private static final String UPLOADING = "正在上传";
    private static final String PAUSEED = "暂停上传";
    private static final int REFRESH_PROGRESS = 1;
    private static final int SUCCESS = 2;
    private static final int FAILURE = 3;
    private MyHandler handler;
    private String videoVid;

    private class MyHandler extends Handler {
        private final WeakReference<PolyvUploadInfo> wr_lv_upload;

        public MyHandler(PolyvUploadInfo uploadInfo) {
            this.wr_lv_upload = new WeakReference<PolyvUploadInfo>(uploadInfo);
        }

        @Override
        public void handleMessage(Message msg) {
            uploadInfo = wr_lv_upload.get();
            if (uploadInfo != null) {
                int position = msg.arg1;
                switch (msg.what) {
                    case REFRESH_PROGRESS:
                        int progress = msg.getData().getInt("progress");
                        long uploaded = msg.getData().getLong("uploaded");
                        pb_progress.setProgress(progress);
                        tv_speed.setText(Formatter.formatFileSize(context, uploaded));
                        break;
                    case SUCCESS:
                        tv_status.setText(UPLOADED);
                        tv_status.setSelected(false);
                        pb_progress.setVisibility(View.GONE);
                        tv_speed.setVisibility(View.GONE);
                        bt_next.setText(UPLOADED);
                        Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                        break;
                    case FAILURE:
//                        tv_status = (TextView) view.findViewById(R.id.tv_status);
                        tv_status.setText(PAUSEED);
                        tv_status.setSelected(true);
                        int category = (int) msg.obj;
                        switch (category) {
                            case PolyvUploader.FFILE:
                                Toast.makeText(context, "第" + (position + 1) + "个任务文件不存在，或者大小为0", Toast.LENGTH_SHORT).show();
                                break;
                            case PolyvUploader.FVIDEO:
                                Toast.makeText(context, "第" + (position + 1) + "个任务不是支持上传的视频格式", Toast.LENGTH_SHORT).show();
                                break;
                            case PolyvUploader.NETEXCEPTION:
                                Toast.makeText(context, "第" + (position + 1) + "个任务网络异常，请重试", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                }
            }
        }
    }

    private class MyUploadListener implements PolyvUploader.UploadListener {
        private final WeakReference<MyHandler> wr_handler;
        private PolyvUploadInfo uploadInfo;
        private int position;

        public MyUploadListener(MyHandler myHandler, PolyvUploadInfo uploadInfo, int position) {
            this.wr_handler = new WeakReference<MyHandler>(myHandler);
            this.uploadInfo = uploadInfo;
            this.position = position;
        }

        @Override
        public void fail(int category) {
            MyHandler myHandler = wr_handler.get();
            if (myHandler != null) {
                Message message = myHandler.obtainMessage(FAILURE);
                message.arg1 = position;
                message.obj = category;
                myHandler.sendMessage(message);
            }
        }

        @Override
        public void upCount(long count, long total) {
            // 已下载的百分比
            int progress = (int) (count * 100 / total);
            // 已下载的文件大小
            long uploaded = uploadInfo.getFilesize() * progress / 100;
            uploadInfo.setPercent(count);
            uploadInfo.setTotal(total);
            uploadSQLiteHelper.update(uploadInfo, count, total);
            MyHandler myHandler = wr_handler.get();
            if (myHandler != null) {
                Message message = myHandler.obtainMessage(REFRESH_PROGRESS);
                message.arg1 = position;
                Bundle bundle = new Bundle();
                bundle.putInt("progress", progress);
                bundle.putLong("uploaded", uploaded);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        }

        @Override
        public void success(long total, String vid) {
            uploadInfo.setPercent(total);
            uploadInfo.setTotal(total);
            uploadSQLiteHelper.update(uploadInfo, total, total);
            MyHandler myHandler = wr_handler.get();
            if (myHandler != null) {
                Message message = myHandler.obtainMessage(SUCCESS);
                message.arg1 = position;
                myHandler.sendMessage(message);
                LogUtils.e(vid);
                videoVid = vid;
                commitVid();
            }
        }
    }

    // 初始化上传器的监听器
    public void initUploader() {
        if (uploadInfo != null) {
//            PolyvUploadInfo uploadInfo = lists.get(i);
            String filepath = uploadInfo.getFilepath();
            String title = uploadInfo.getTitle();
            String desc = uploadInfo.getDesc();
            IPolyvUploader uploader = PolyvUploaderManager.getPolyvUploader(filepath, title, desc);
            uploader.setUploadListener(new MyUploadListener(handler, uploadInfo, 0));
        }
    }

    /**
     * 把任务从列表中移除
     */
    public void removeTask() {
//        PolyvUploadInfo uploadInfo = lists.remove(position);
        // 该方法会先暂停上传再移除任务
        PolyvUploaderManager.removePolyvUpload(uploadInfo.getFilepath());
        initUploader();
        uploadSQLiteHelper.delete(uploadInfo);
    }

    private class UploadOnClickListener implements View.OnClickListener {
        private PolyvUploadInfo uploadInfo;
        private Button iv_start;
        private TextView tv_status;

        public UploadOnClickListener(PolyvUploadInfo uploadInfo, Button iv_start, TextView tv_status) {
            this.uploadInfo = uploadInfo;
            this.iv_start = iv_start;
            this.tv_status = tv_status;
        }

        @Override
        public void onClick(View v) {
            String filepath = uploadInfo.getFilepath();
            String title = uploadInfo.getTitle();
            String desc = uploadInfo.getDesc();
            IPolyvUploader uploader = PolyvUploaderManager.getPolyvUploader(filepath, title, desc);
            if (tv_status.getText().equals(UPLOADED)) {
                finish();
                //...
            } else if (tv_status.getText().equals(UPLOADING)) {
                tv_status.setText(PAUSEED);
                iv_start.setText(PAUSEED);
                tv_status.setSelected(true);
//                iv_start.setImageResource(R.mipmap.polyv_btn_upload);
                uploader.pause();
            } else {
                tv_status.setText(UPLOADING);
                tv_status.setSelected(false);
//                iv_start.setImageResource(R.mipmap.polyv_btn_dlpause);
                iv_start.setText(UPLOADING);
                uploader.start();
            }
        }
    }

    // 获取视频的地址并添加到上传列表中
    private void handle(Uri... uris) {
        // 路径
        String filepath;
        for (int i = 0; i < uris.length; i++) {
            // 在图册中上传
            if (uris[i].toString().startsWith("content")) {
                filepath = GetPathFromUri.getPath(this, uris[i]);
            } else {
                // 在文件中选择
                filepath = uris[i].getPath().substring(uris[i].getPath().indexOf("/") + 1);
            }
            File file = new File(filepath);
            String fileName = file.getName();
            // 标题
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            // 描述
            String desc = title;
            // 大小
            long filesize = file.length();
            uploadInfo = new PolyvUploadInfo(title, desc, filesize, filepath);
            if (!uploadSQLiteHelper.isAdd(uploadInfo)) {
                uploadSQLiteHelper.insert(uploadInfo);
                handler = new MyHandler(uploadInfo);
                initUploader();

                if (uris[i].toString().startsWith("image/*")) {
                    UIUtil.showToast(uris[i] + "");
                } else {
                    MediaController mc = new MediaController(this);
                    mc.setAnchorView(iv_pre);
                    mc.setMediaPlayer(iv_pre);
                    iv_pre.setMediaController(mc);
                    iv_pre.setVideoURI(uris[i]);
                    iv_pre.start();
                }

            } else {
                uploadSQLiteHelper.delete(uploadInfo);

                uploadSQLiteHelper.insert(uploadInfo);
                handler = new MyHandler(uploadInfo);
                initUploader();

                if (uris[i].toString().startsWith("image/*")) {
                    UIUtil.showToast(uris[i] + "");
                } else {
                    MediaController mc = new MediaController(this);
                    mc.setAnchorView(iv_pre);
                    mc.setMediaPlayer(iv_pre);
                    iv_pre.setMediaController(mc);
                    iv_pre.setVideoURI(uris[i]);
                    iv_pre.start();
                }

//                PolyvUploadVideoScannerActivity.this.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        Toast.makeText(PolyvUploadVideoScannerActivity.this, "上传任务已经增加到队列", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        }
//        adapter.notifyDataSetChanged();
    }

    private void startUpload() {
        long percent = uploadInfo.getPercent();
        long total = uploadInfo.getTotal();
        String title = uploadInfo.getTitle();
        String filepath = uploadInfo.getFilepath();
        String desc = uploadInfo.getDesc();
        long filesize = uploadInfo.getFilesize();
        // 已上传的百分比
        int progress = 0;
        if (total != 0)
            progress = (int) (percent * 100 / total);
        IPolyvUploader uploader = PolyvUploaderManager.getPolyvUploader(filepath, title, desc);
        pb_progress.setVisibility(View.VISIBLE);
        tv_speed.setVisibility(View.VISIBLE);
        tv_status.setSelected(false);
//        fl_start.setEnabled(true);
        if (progress == 100) {
//            iv_start.setImageResource(R.drawable.polyv_btn_play);
//            fl_start.setEnabled(false);
            tv_status.setText(PAUSEED);
            pb_progress.setVisibility(View.GONE);
            tv_speed.setVisibility(View.GONE);
            bt_next.setText(UPLOADED);
        } else if (uploader.isUploading()) {
//            iv_start.setImageResource(R.mipmap.polyv_btn_dlpause);
            tv_status.setText(UPLOADING);
        } else {
//            iv_start.setImageResource(R.mipmap.polyv_btn_upload);
            tv_status.setText(PAUSEED);
            tv_status.setSelected(true);
        }
        tv_size.setText(Formatter.formatFileSize(context, filesize));
        tv_speed.setText(Formatter.formatFileSize(context, filesize * progress / 100));
        pb_progress.setProgress(progress);
        bt_next.setOnClickListener(new UploadOnClickListener(uploadInfo, bt_next, tv_status));
    }

    private void initData() {
        initData(null);
    }

    // 初始化分享上传的视频数据
    private void initData(Intent intent) {
        if (intent == null)
            intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("video/")) {
                Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (uri != null)
                    handle(uri);
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("video/") || type.startsWith("*/")) {
                ArrayList<Uri> uris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                if (uris != null)
                    handle(uris.toArray(new Uri[uris.size()]));
            }
        }
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_upload_video;
    }

    @Override
    public void initViewsAndEvents() {
        context = this;
        initTitle();
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_pre.start();
            }
        });
        uploadSQLiteHelper = PolyvUploadSQLiteHelper.getInstance(this);
//        bt_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (bt_next.getText().equals("立即拍摄")) {
//                    goRecording();
//                } else if (bt_next.getText().equals("立即上传")) {
//                    startUpload();
//                }
//            }
//        });
//        initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String type = bundle.getString("type");
            if (!TextUtils.isEmpty(type) && type.equals("local")) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(Intent.createChooser(intent, "完成操作需使用"), 12);
            } else {
                goRecording();
            }
        } else {
            goRecording();
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 12:
                if (resultCode == RESULT_OK && data != null) {
                    // 获取文件路径
                    Uri uri = data.getData();
                    if (uri != null) {
                        handle(uri);
                        startUpload();
                    } else {
                        Toast.makeText(PolyvUploadVideoScannerActivity.this, "视频获取失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else {
                    Toast.makeText(PolyvUploadVideoScannerActivity.this, "视频获取失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case Constants.VIDEO_RECORD_REQUEST:
                if (null != data) {
                    Uri uri = data.getData();
                    if (uri == null) {
                        Toast.makeText(PolyvUploadVideoScannerActivity.this, "视频获取失败", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
//                        Cursor c = getContentResolver().query(uri,
//                                new String[]{MediaStore.MediaColumns.DATA},
//                                null, null, null);
//                        if (c != null && c.moveToFirst()) {
//                            filPaths = c.getString(0);
//                            showUploadVideoDialog();
                        handle(uri);
//                        bt_next.setText("立即上传");
                        startUpload();
//                        }
                    }
                } else {
                    Toast.makeText(PolyvUploadVideoScannerActivity.this, "视频获取失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setTitle("上传视频");
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
        title_view.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        title_view.setActionTextColor(Color.WHITE);
        title_view.setImmersive(true);


    }


    private void goRecording() {
        Intent intent;
        intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);// 创建一个请求视频的意图
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);// 设置视频的质量，值为0-1，
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);// 设置视频的录制长度，s为单位
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 20 * 1024 * 1024L);// 设置视频文件大小，字节为单位
        startActivityForResult(intent, Constants.VIDEO_RECORD_REQUEST);// 设置请求码，在onActivityResult()方法中接收结果

    }

    private void commitVid() {
//        DialogUtils.showDialog(PolyvUploadVideoScannerActivity.this, "加载中...", false);
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
                UIUtil.showToast("接口异常");
            }
        });

    }
}
