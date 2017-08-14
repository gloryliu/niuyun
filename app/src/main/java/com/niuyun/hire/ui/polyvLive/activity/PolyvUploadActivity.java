package com.niuyun.hire.ui.polyvLive.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.easefun.polyvsdk.upload.IPolyvUploader;
import com.easefun.polyvsdk.upload.PolyvUploader;
import com.easefun.polyvsdk.upload.PolyvUploaderManager;
import com.easefun.polyvsdk.util.GetPathFromUri;
import com.niuyun.hire.R;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.polyvLive.bean.PolyvUploadInfo;
import com.niuyun.hire.ui.polyvLive.util.PolyvUploadSQLiteHelper;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.view.TitleBar;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;

public class PolyvUploadActivity extends BaseActivity {
    // 上传的listView
//    private ListView lv_upload;
//    private PolyvUploadListViewAdapter adapter;
//    private List<PolyvUploadInfo> lists;
    // 底部选择本地视频按钮
    private RelativeLayout rl_bot;
    private static PolyvUploadSQLiteHelper uploadSQLiteHelper;
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.pv_play)
    PolyvPlayerView pv_play;
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
//                View view = lv_upload.getChildAt(position - lv_upload.getFirstVisiblePosition());
//                if (view == null)
//                    return;
//                TextView tv_status = null;
//                ImageView iv_start = null;
//                FrameLayout fl_start = null;
//                ProgressBar pb_progress = null;
//                TextView tv_speed = null;
                switch (msg.what) {
                    case REFRESH_PROGRESS:
//                        pb_progress = (ProgressBar) view.findViewById(pb_progress);
//                        tv_speed = (TextView) view.findViewById(R.id.tv_speed);
                        int progress = msg.getData().getInt("progress");
                        long uploaded = msg.getData().getLong("uploaded");
                        pb_progress.setProgress(progress);
                        tv_speed.setText(Formatter.formatFileSize(context, uploaded));
                        break;
                    case SUCCESS:
//                        tv_status = (TextView) view.findViewById(R.id.tv_status);
//                        tv_speed = (TextView) view.findViewById(R.id.tv_speed);
                        tv_status.setText(UPLOADED);
                        tv_status.setSelected(false);
                        pb_progress.setVisibility(View.GONE);
                        tv_speed.setVisibility(View.GONE);
                        Toast.makeText(context, "第" + (position + 1) + "个任务上传成功", Toast.LENGTH_SHORT).show();
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

    private static class MyUploadListener implements PolyvUploader.UploadListener {
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
//                LogUtil.e(vid);
            }
        }
    }

    // 初始化上传器的监听器
    public void initUploader() {
        if (uploadInfo!=null) {
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
    public void removeTask(int position) {
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
                iv_start.setText(UPLOADED);

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

    private void findIdAndNew() {
//        lv_upload = (ListView) findViewById(lv_upload);
        rl_bot = (RelativeLayout) findViewById(R.id.rl_bot);
//        lists = new ArrayList<>();
        uploadSQLiteHelper = PolyvUploadSQLiteHelper.getInstance(this);
    }

    private void initView() {
//        lists.addAll(PolyvUploadSQLiteHelper.getInstance(this).getAll());
//        adapter = new PolyvUploadListViewAdapter(this, lists, lv_upload);
//        lv_upload.setAdapter(adapter);
//        lv_upload.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                new AlertDialog.Builder(PolyvUploadActivity.this).setTitle("提示").setMessage("是否从列表中移除该任务")
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                adapter.removeTask(position);
//                            }
//                        }).setNegativeButton(android.R.string.cancel, null).show();
//                return true;
//            }
//        });
        rl_bot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(Intent.createChooser(intent, "完成操作需使用"), 12);
            }
        });
        bt_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_next.getText().equals("立即拍摄")) {

                    goRecording();
                } else if (bt_next.getText().equals("立即上传")) {
                    startUpload();
                }
            }
        });
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
//                lists.add(uploadInfo);
//                adapter.initUploader();
                handler = new MyHandler(uploadInfo);
                initUploader();

                if (uris[i].toString().startsWith("image/*")) {
//                    ImageView view = new ImageView(this);
//                    iv_pre.setImageURI(uris[i]);
                    UIUtil.showToast(uris[i]+"");
//                    view.setLayoutParams(layoutParams);
//                    relativeLayout.addView(view);
                } else {
                    MediaController mc = new MediaController(this);
//                    VideoView view = new VideoView(this);
                    mc.setAnchorView(iv_pre);
                    mc.setMediaPlayer(iv_pre);
                    iv_pre.setMediaController(mc);
                    iv_pre.setVideoURI(uris[i]);
                    iv_pre.start();
//                    view.setLayoutParams(layoutParams);
//                    relativeLayout.addView(view);
                }

//                iv_pre.setImageURI(uris[0]);
            } else {
                PolyvUploadActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(PolyvUploadActivity.this, "上传任务已经增加到队列", Toast.LENGTH_SHORT).show();
                    }
                });
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
            tv_status.setText(UPLOADED);
            pb_progress.setVisibility(View.GONE);
            tv_speed.setVisibility(View.GONE);
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
        return R.layout.polyv_activity_upload;
    }

    @Override
    public void initViewsAndEvents() {
        context = this;
        initTitle();
        findIdAndNew();
        initView();
        initData();


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        android.app.FragmentTransaction transaction=getFragmentManager().beginTransaction();
        pv_play.setTransaction(ft);
        pv_play.setVid("c538856ddeb0abe3b875545932c82c59_c");
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 12:
                if (resultCode == RESULT_OK && data != null) {
                    // 获取文件路径
                    Uri uri = data.getData();
                    handle(uri);
                } else {
                    Toast.makeText(PolyvUploadActivity.this, "视频获取失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case Constants.VIDEO_RECORD_REQUEST:
                if (null != data) {
                    Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    } else {
//                        Cursor c = getContentResolver().query(uri,
//                                new String[]{MediaStore.MediaColumns.DATA},
//                                null, null, null);
//                        if (c != null && c.moveToFirst()) {
//                            filPaths = c.getString(0);
//                            showUploadVideoDialog();
                        handle(uri);
                        bt_next.setText("立即上传");
//                        }
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

    private void goRecording() {
        Intent intent;
        intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);// 创建一个请求视频的意图
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);// 设置视频的质量，值为0-1，
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);// 设置视频的录制长度，s为单位
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);// 设置视频的录制长度，s为单位
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 20 * 1024 * 1024L);// 设置视频文件大小，字节为单位
        startActivityForResult(intent, Constants.VIDEO_RECORD_REQUEST);// 设置请求码，在onActivityResult()方法中接收结果

    }
}
