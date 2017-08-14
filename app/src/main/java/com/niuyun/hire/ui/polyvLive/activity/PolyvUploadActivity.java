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
    private String filPaths;
    PolyvUploadInfo uploadInfo;
    private static Context context;
    private static final String UPLOADED = "已上传";
    private static final String UPLOADING = "正在上传";
    private static final String PAUSEED = "暂停上传";
    private static final int REFRESH_PROGRESS = 1;
    private static final int SUCCESS = 2;
    private static final int FAILURE = 3;


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
                Intent intent = new Intent(PolyvUploadActivity.this, PolyvUploadVideoScannerActivity.class);
                startActivityForResult(intent, Constants.GET_VIDEO_VID);
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

                if (uris[i].toString().startsWith("image/*")) {
//                    ImageView view = new ImageView(this);
//                    iv_pre.setImageURI(uris[i]);
                    UIUtil.showToast(uris[i] + "");
//                    view.setLayoutParams(layoutParams);
//                    relativeLayout.addView(view);
                } else {
                    MediaController mc = new MediaController(this);
//                    VideoView view = new VideoView(this);
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
        setVid("");
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
            case Constants.GET_VIDEO_VID:
                if (null != data && data.getData() != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String vid = bundle.getString("videoVid");
                        setVid(vid);
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

    private void setVid(String vid) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        android.app.FragmentTransaction transaction=getFragmentManager().beginTransaction();
        pv_play.setTransaction(ft);
//        pv_play.setVid("c538856ddeb0abe3b875545932c82c59_c");
        pv_play.setVid(vid);
    }

}
