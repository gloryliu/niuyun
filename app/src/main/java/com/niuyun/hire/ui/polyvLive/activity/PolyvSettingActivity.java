package com.niuyun.hire.ui.polyvLive.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.rtmp.core.login.IPolyvRTMPLoginListener;
import com.easefun.polyvsdk.rtmp.core.login.PolyvRTMPLoginErrorReason;
import com.easefun.polyvsdk.rtmp.core.login.PolyvRTMPLoginVerify;
import com.easefun.polyvsdk.rtmp.core.userinterface.PolyvAuthTypeSetting;
import com.easefun.polyvsdk.rtmp.core.video.PolyvRTMPDefinition;
import com.easefun.polyvsdk.rtmp.core.video.PolyvRTMPOrientation;
import com.easefun.polyvsdk.rtmp.core.video.PolyvRTMPRenderScreenSize;
import com.easefun.polyvsdk.rtmp.core.video.PolyvRTMPView;
import com.easefun.polyvsdk.rtmp.sopcast.video.effect.BeautyEffect;
import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.ui.adapter.LiveTagAdapter;
import com.niuyun.hire.ui.bean.CommonTagBean;
import com.niuyun.hire.ui.bean.CommonTagItemBean;
import com.niuyun.hire.ui.bean.GetBaseTagBean;
import com.niuyun.hire.ui.bean.SuperBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.ui.polyvLive.adapter.ResolutionItemAdapter;
import com.niuyun.hire.ui.polyvLive.bean.CreateLiveBean;
import com.niuyun.hire.ui.polyvLive.permission.PolyvPermission;
import com.niuyun.hire.ui.polyvLive.util.PolyvScreenUtils;
import com.niuyun.hire.ui.polyvLive.util.PolyvStatusBarUtil;
import com.niuyun.hire.ui.polyvLive.view.PolyvGrayImageView;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.utils.UploadFile;
import com.niuyun.hire.utils.photoutils.TakeSimpleActivity;
import com.niuyun.hire.view.MyDialog;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.niuyun.hire.base.Constants.LIVE_PASSWORD;
import static com.niuyun.hire.base.Constants.resultCode_header_Camera;
import static com.niuyun.hire.base.Constants.resultCode_header_Photos;

/**
 * 引导页Activity
 */
public class PolyvSettingActivity extends Activity implements View.OnClickListener {
    private PolyvRTMPView polyvRTMPView = null;
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    public static final String ACTION_FINISH = "polyv.activity.setting.receiver";
    private static final String LOGO_NAME = "polyvlivelogo";
    // logo
    private PolyvGrayImageView iv_logo;
    private ImageView iv_portrait, iv_landscape, iv_sc, iv_hd, iv_sd, iv_public, iv_password, iv_pay;
    private RelativeLayout rl_portrait, rl_landscape, rl_sc, rl_hd, rl_sd, rl_public, rl_password, rl_pay;
    private ImageView iv_wechat, iv_moments, iv_weibo, iv_qq, iv_qzone;
    // 标题编辑框
    private EditText et_title;
    // 注销按钮
    private ImageView iv_back;
    //切换分辨率
    private TextView tv_logoff;
    //切换摄像头，增加封面
    private ImageView iv_change_camera, iv_add_cover;
    // 开始按钮
    private Button bt_start;
    // 分享的平台
    private String sharePlatform;
    // 标题
    private String title;
    private PolyvPermission polyvPermission = null;

    private String mChannelId;
    private String userId;
    private String nickName;
    private int mOrientation = PolyvRTMPOrientation.SCREEN_ORIENTATION_PORTRAIT;
    private int mDefinition = PolyvRTMPDefinition.GAO_QING;
    // 观看认证类型
    private String authType = PolyvAuthTypeSetting.AUTHTYPE_NONE;
    // 密码
    private String code;
    // 价格
    private double price;
    private FinishBroadcastReceiver receiver;

    private class FinishBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && ACTION_FINISH.equals(intent.getAction())) {
                finish();
            }
        }
    }

    //注册广播接收者
    private void registerReceiver() {
        receiver = new FinishBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_FINISH);
        registerReceiver(receiver, filter);
    }

    private AlertDialog alertDialog = null;
    private static final int START = 1;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START:
//                    polyvRTMPView.capture();
                    break;
            }
        }
    };
    private PopupWindow popupWindow;
    private RecyclerView rv_list;
    private ResolutionItemAdapter adapter;
    private List<String> resolutionList = new ArrayList<>();


    private Call<SuperBean<String>> upLoadImageCall;
    private String headimg;
    List<String> list = new ArrayList<>();
    private CommonTagBean commonTagBean;
    private RecyclerView an_tags;
    private LiveTagAdapter tagAdapter;
    private List<CommonTagItemBean> tagSelectedList = new ArrayList<>();
    private NestedScrollView nsv_scroll;
    private EditText et_company_name;
    private EditText et_host_name;
    private TextView tv_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polyv_activity_setting);
        // edittext->adjustPan(否则部分手机会有延迟的白色背景)
        try {
            PolyvStatusBarUtil.setColor(this, getResources().getColor(R.color.translucence_share), 0);
        } catch (Exception e) {
            // ignore
        }
//        title = getIntent().getStringExtra("title");
        findId();
        initView();
        registerReceiver();
//        mChannelId = getIntent().getStringExtra("channelId");

        polyvPermission = new PolyvPermission();
        polyvPermission.setResponseCallback(new PolyvPermission.ResponseCallback() {
            @Override
            public void callback() {
                gotoPlay();
            }
        });

        initOrientation();
        polyvRTMPView = (PolyvRTMPView) findViewById(R.id.polyv_rtmp_view1);
        initPolyvRTMPView();
        getTagItmes();
    }

    private void initOrientation() {
        switch (mOrientation) {
            case PolyvRTMPOrientation.SCREEN_ORIENTATION_PORTRAIT:
                PolyvScreenUtils.setPortrait(this);
                break;

            case PolyvRTMPOrientation.SCREEN_ORIENTATION_LANDSCAPE:
                PolyvScreenUtils.setLandscape(this);
                break;
        }
    }

    private void initPolyvRTMPView() {
//        polyvRTMPView.setOnPreparedListener(new IPolyvRTMPOnPreparedListener() {
//            @Override
//            public void onPrepared() {
//                handler.sendEmptyMessageDelayed(START, 1000);
//            }
//        });
//        polyvRTMPView.setOnErrorListener(new IPolyvRTMPOnErrorListener() {
//            @Override
//            public void onError(PolyvRTMPErrorReason errorReason) {
//                String message = "";
//                switch (errorReason.getType()) {
//                    case PolyvRTMPErrorReason.GET_NGB_PUSH_URL_EMPTY:
//                        message = "获取NGB推流地址为空，请重试 (error code " + PolyvRTMPErrorReason.GET_NGB_PUSH_URL_EMPTY + ")";
//                        break;
//                    case PolyvRTMPErrorReason.NETWORK_DENIED:
//                        message = "请连接网络 (error code " + PolyvRTMPErrorReason.NETWORK_DENIED + ")";
//                        break;
//                    case PolyvRTMPErrorReason.NOT_CAMERA:
//                        message = "没有摄像头，请更换设备 (error coee " + PolyvRTMPErrorReason.NOT_CAMERA + ")";
//                        break;
//                    case PolyvRTMPErrorReason.AUDIO_AEC_ERROR:
//                        message = "不支持音频aec (error code " + PolyvRTMPErrorReason.AUDIO_AEC_ERROR + ")";
//                        break;
//                    case PolyvRTMPErrorReason.AUDIO_CONFIGURATION_ERROR:
//                        message = "音频编解码器配置错误 (error code " + PolyvRTMPErrorReason.AUDIO_CONFIGURATION_ERROR + ")";
//                        break;
//                    case PolyvRTMPErrorReason.AUDIO_ERROR:
//                        message = "不能记录音频 (error code " + PolyvRTMPErrorReason.AUDIO_ERROR + ")";
//                        break;
//                    case PolyvRTMPErrorReason.AUDIO_TYPE_ERROR:
//                        message = "音频类型错误 (error code " + PolyvRTMPErrorReason.AUDIO_TYPE_ERROR + ")";
//                        break;
//                    case PolyvRTMPErrorReason.CAMERA_DISABLED:
//                        message = "摄相机被禁用 (error code " + PolyvRTMPErrorReason.CAMERA_DISABLED + ")";
//                        break;
//                    case PolyvRTMPErrorReason.CAMERA_ERROR:
//                        message = "摄像机没有开启 (error code " + PolyvRTMPErrorReason.CAMERA_ERROR + ")";
//                        break;
//                    case PolyvRTMPErrorReason.CAMERA_NOT_SUPPORT:
//                        message = "摄相机不支持 (error code " + PolyvRTMPErrorReason.CAMERA_NOT_SUPPORT + ")";
//                        break;
//                    case PolyvRTMPErrorReason.CAMERA_OPEN_FAILED:
//                        message = "摄相机打开失败 (error code " + PolyvRTMPErrorReason.CAMERA_OPEN_FAILED + ")";
//                        break;
//                    case PolyvRTMPErrorReason.SDK_VERSION_ERROR:
//                        message = "Android sdk 版本低于18（Android 4.3.1）(error code " + PolyvRTMPErrorReason.SDK_VERSION_ERROR + ")";
//                        break;
//                    case PolyvRTMPErrorReason.VIDEO_CONFIGURATION_ERROR:
//                        message = "视频编解码器配置错误 (error code " + PolyvRTMPErrorReason.VIDEO_CONFIGURATION_ERROR + ")";
//                        break;
//                    case PolyvRTMPErrorReason.VIDEO_TYPE_ERROR:
//                        message = "视频类型错误 (error code " + PolyvRTMPErrorReason.VIDEO_TYPE_ERROR + ")";
//                        break;
//                    case PolyvRTMPErrorReason.NOT_LOGIN:
//                        message = "请先登录 (error code " + PolyvRTMPErrorReason.NOT_LOGIN + ")";
//                        break;
//                    case PolyvRTMPErrorReason.RELOGIN_FAIL:
//                        message = "请重新登陆 (error code " + PolyvRTMPErrorReason.RELOGIN_FAIL + ")";
//                        break;
//                }
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(PolyvSettingActivity.this);
//                builder.setTitle("错误");
//                builder.setMessage(message);
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                    }
//                });
//
//                builder.show();
//            }
//        });

//        polyvRTMPView.setOnOpenCameraSuccessListener(new IPolyvRTMPOnOpenCameraSuccessListener() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(PolyvSettingActivity.this, "打开摄像机成功", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        polyvRTMPView.setOnCameraChangeListener(new IPolyvRTMPOnCameraChangeListener() {
//            @Override
//            public void onChange() {
//                Toast.makeText(PolyvSettingActivity.this, "切换摄像机", Toast.LENGTH_SHORT).show();
//            }
//        });

        polyvRTMPView.setConfiguration(mDefinition, mOrientation);
        polyvRTMPView.setRenderScreenSize(PolyvRTMPRenderScreenSize.AR_ASPECT_FILL_PARENT);
        polyvRTMPView.setEffect(new BeautyEffect(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        polyvRTMPView.resume();
    }

    private void findId() {
        iv_portrait = (ImageView) findViewById(R.id.iv_portrait);
        iv_landscape = (ImageView) findViewById(R.id.iv_landscape);
        iv_sc = (ImageView) findViewById(R.id.iv_sc);
        iv_hd = (ImageView) findViewById(R.id.iv_hd);
        iv_sd = (ImageView) findViewById(R.id.iv_sd);
        iv_public = (ImageView) findViewById(R.id.iv_public);
        iv_password = (ImageView) findViewById(R.id.iv_password);
        iv_pay = (ImageView) findViewById(R.id.iv_pay);

        rl_portrait = (RelativeLayout) findViewById(R.id.rl_portrait);
        rl_landscape = (RelativeLayout) findViewById(R.id.rl_landscape);
        rl_sc = (RelativeLayout) findViewById(R.id.rl_sc);
        rl_hd = (RelativeLayout) findViewById(R.id.rl_hd);
        rl_sd = (RelativeLayout) findViewById(R.id.rl_sd);
        rl_public = (RelativeLayout) findViewById(R.id.rl_public);
        rl_password = (RelativeLayout) findViewById(R.id.rl_password);
        rl_pay = (RelativeLayout) findViewById(R.id.rl_pay);

        iv_logo = (PolyvGrayImageView) findViewById(R.id.iv_logo);
        iv_wechat = (ImageView) findViewById(R.id.iv_wechat);
        iv_moments = (ImageView) findViewById(R.id.iv_moments);
        iv_weibo = (ImageView) findViewById(R.id.iv_weibo);
        iv_qq = (ImageView) findViewById(R.id.iv_qq);
        iv_qzone = (ImageView) findViewById(R.id.iv_qzone);
        bt_start = (Button) findViewById(R.id.bt_start);
        et_title = (EditText) findViewById(R.id.et_title);
        tv_logoff = (TextView) findViewById(R.id.tv_logoff);
        tv_more = (TextView) findViewById(R.id.tv_more);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_add_cover = (ImageView) findViewById(R.id.iv_add_cover);
        iv_change_camera = (ImageView) findViewById(R.id.iv_change_camera);
        an_tags = (RecyclerView) findViewById(R.id.an_tags);
        nsv_scroll = (NestedScrollView) findViewById(R.id.nsv_scroll);
        et_company_name = (EditText) findViewById(R.id.et_company_name);
        et_host_name = (EditText) findViewById(R.id.et_host_name);


    }

    private void initView() {
        rl_portrait.setOnClickListener(this);
        rl_landscape.setOnClickListener(this);
        rl_sc.setOnClickListener(this);
        rl_hd.setOnClickListener(this);
        rl_sd.setOnClickListener(this);
        rl_public.setOnClickListener(this);
        rl_password.setOnClickListener(this);
        rl_pay.setOnClickListener(this);
        iv_logo.setOnClickListener(this);
        iv_wechat.setOnClickListener(this);
        iv_moments.setOnClickListener(this);
        iv_weibo.setOnClickListener(this);
        iv_qq.setOnClickListener(this);
        iv_qzone.setOnClickListener(this);
        bt_start.setOnClickListener(this);
        tv_logoff.setOnClickListener(this);
        iv_add_cover.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_change_camera.setOnClickListener(this);
        tv_more.setOnClickListener(this);
//        et_title.setText(title);
//        et_title.setSelection(et_title.length());

//        resetDefinition(PolyvRTMPDefinition.CHAO_QING);
//        resetMode(PolyvRTMPOrientation.SCREEN_ORIENTATION_LANDSCAPE);
        iv_public.setSelected(true);

//        final String logoPath = getSharedPreferences(LOGO_NAME, MODE_PRIVATE).getString(LOGO_NAME, null);
//        if (!TextUtils.isEmpty(logoPath) && new File(logoPath).exists()) {
//            iv_logo.setImageDrawable(Drawable.createFromPath(logoPath), true);
//            canShowDeleteLogoDialog(logoPath);
//        }
    }

    private void canShowDeleteLogoDialog(final String logoPath) {
        iv_logo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(v.getContext()).setTitle("提示")
                        .setMessage("是否要删除logo")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getSharedPreferences(LOGO_NAME, MODE_PRIVATE).edit().remove(LOGO_NAME).commit();
                                new File(logoPath).delete();
                                iv_logo.setImageResource(R.drawable.polyv_setting_iv_pressed);
                                iv_logo.changeDrawColor(false);
                                iv_logo.setOnLongClickListener(null);
                            }
                        })
                        .setNegativeButton("取消", null).show();
                return true;
            }
        });
    }

    private void resetMode(int orientation) {
        switch (orientation) {
            case PolyvRTMPOrientation.SCREEN_ORIENTATION_PORTRAIT:
                iv_landscape.setSelected(false);
                iv_portrait.setSelected(true);
                break;
            case PolyvRTMPOrientation.SCREEN_ORIENTATION_LANDSCAPE:
                iv_portrait.setSelected(false);
                iv_landscape.setSelected(true);
                break;
        }

        mOrientation = orientation;
    }

    private void resetDefinition(int definition) {
        switch (definition) {
            case PolyvRTMPDefinition.CHAO_QING:
                iv_hd.setSelected(false);
                iv_sd.setSelected(false);
                iv_sc.setSelected(true);
                break;
            case PolyvRTMPDefinition.GAO_QING:
                iv_sc.setSelected(false);
                iv_sd.setSelected(false);
                iv_hd.setSelected(true);
                break;
            case PolyvRTMPDefinition.LIU_CHANGE:
                iv_sc.setSelected(false);
                iv_hd.setSelected(false);
                iv_sd.setSelected(true);
                break;
        }

        mDefinition = definition;
    }

    private void resetPermission(final int permission) {
        code = null;
        price = 0;
        switch (permission) {
            case 0:
                iv_password.setSelected(false);
                iv_pay.setSelected(false);
                iv_public.setSelected(true);
                authType = PolyvAuthTypeSetting.AUTHTYPE_NONE;
                break;
            case 1:
                final EditText editText = new EditText(this);
                editText.setKeyListener(DigitsKeyListener.getInstance("123456789"));
                new AlertDialog.Builder(this).setTitle("请输入密码")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(editText.getText().toString())) {
                                    Toast.makeText(PolyvSettingActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                iv_public.setSelected(false);
                                iv_pay.setSelected(false);
                                iv_password.setSelected(true);
                                code = editText.getText().toString();
                                authType = PolyvAuthTypeSetting.AUTHTYPE_CODE;
                            }
                        }).setNegativeButton("取消", null).show();
                break;
            case 2:
                final EditText pEditText = new EditText(this);
                pEditText.setKeyListener(DigitsKeyListener.getInstance("123456789."));
                new AlertDialog.Builder(this).setTitle("请输入价格")
                        .setView(pEditText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(pEditText.getText().toString())) {
                                    Toast.makeText(PolyvSettingActivity.this, "价格不能为空", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                iv_public.setSelected(false);
                                iv_password.setSelected(false);
                                iv_pay.setSelected(true);
                                price = Double.parseDouble(pEditText.getText().toString());
                                authType = PolyvAuthTypeSetting.AUTHTYPE_PAY;
                            }
                        }).setNegativeButton("取消", null).show();
                break;
        }
    }

    private void selectLogo() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    Toast.makeText(this, "Cannot retrieve selected image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        } else if (resultCode == resultCode_header_Camera) {
            //相机返回图片
//                Bundle b = data.getExtras();
//                String fileName = b.getString("picture");
            list.clear();
            list = data.getStringArrayListExtra("picture");
        } else if (resultCode == resultCode_header_Photos) {
            // 图库中选择
            if (data == null || "".equals(data)) {
                return;
            }
            list.clear();
            list = data.getStringArrayListExtra("photo");
        }
        if (list.size() > 0) {
            ImageLoadedrManager.getInstance().displayNoFilter(this, list.get(0), iv_add_cover);
        }
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = LOGO_NAME + ".jpg";
        int w = Math.max(300, iv_logo.getMeasuredWidth());
        int h = Math.max(300, iv_logo.getMeasuredHeight());
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getDiskCacheDir(this), destinationFileName)))
                .withMaxResultSize(w, h)
                .withAspectRatio(1, 1);
        uCrop.start(this);
    }

    private File getDiskCacheDir(Context context) {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) ? context.getExternalCacheDir() : context.getCacheDir();
    }

    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Unexpected error", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            iv_logo.setImageDrawable(Drawable.createFromPath(resultUri.getPath()), true);
            getSharedPreferences(LOGO_NAME, MODE_PRIVATE).edit().putString(LOGO_NAME, resultUri.getPath()).commit();
            canShowDeleteLogoDialog(resultUri.getPath());
        } else {
            Toast.makeText(this, "Cannot retrieve cropped image", Toast.LENGTH_SHORT).show();
        }
    }

    private void start() {
        polyvPermission.applyPermission(PolyvSettingActivity.this, PolyvPermission.OperationType.play);
    }

    private void logoff() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_portrait:
                resetMode(PolyvRTMPOrientation.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case R.id.rl_landscape:
                resetMode(PolyvRTMPOrientation.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case R.id.rl_sc:
                resetDefinition(PolyvRTMPDefinition.CHAO_QING);
                break;
            case R.id.rl_hd:
                resetDefinition(PolyvRTMPDefinition.GAO_QING);
                break;
            case R.id.rl_sd:
                resetDefinition(PolyvRTMPDefinition.LIU_CHANGE);
                break;
            case R.id.rl_public:
                resetPermission(0);
                break;
            case R.id.rl_password:
                // 待完善
//                resetPermission(1);
                break;
            case R.id.rl_pay:
                // 待完善
//                resetPermission(2);
                break;
            case R.id.iv_logo:
                // 待完善
//                selectLogo();
                break;
            case R.id.bt_start:
                start();
                break;
            case R.id.iv_back:
                logoff();
                break;
            case R.id.tv_logoff:
                //切换画质
                showpopupWindow(tv_logoff);
                break;
            case R.id.iv_change_camera:
                //切换摄像头
                polyvRTMPView.toggleFrontBackCamera();
                break;
            case R.id.iv_add_cover:
                //增加封面
                photodialog();
                break;
            case R.id.tv_more:
                //展示更多标签
                if (tagAdapter != null && commonTagBean != null && commonTagBean.getData() != null && commonTagBean.getData().getQS_jobtag() != null) {
                    tagAdapter.ClearData();
                    tagAdapter.addList(commonTagBean.getData().getQS_jobtag());
                    tv_more.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_wechat:
                break;
            case R.id.iv_moments:
                break;
            case R.id.iv_weibo:
                break;
            case R.id.iv_qq:
                break;
            case R.id.iv_qzone:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        handler.removeMessages(START);
        polyvRTMPView.destroy();
        LogUtils.e("setting-------destroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        polyvRTMPView.stop();
        LogUtils.e("setting-------onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        polyvRTMPView.pause();
        LogUtils.e("setting-------onPause");
    }

    private void gotoPlay() {

        if (list.size() <= 0) {
            UIUtil.showToast("请上传封面");
        }
        String title = et_title.getText().toString();
        if (title.trim().length() == 0) {
            Toast.makeText(this, "标题不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(et_company_name.getText().toString())) {
            UIUtil.showToast("公司名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(et_host_name.getText().toString())) {
            UIUtil.showToast("主持人不能为空");
            return;
        }
        if (tagSelectedList.size() <= 0) {
            UIUtil.showToast("请选择标签");
            return;
        }
        // 更新活动标题
//        new PolyvTitleUpdate().updateTitle(mChannelId, title, null);
        // 待完善
//        new PolyvAuthTypeSetting().setAuthType(mChannelId, authType, code, price, null);
        if (!TextUtils.isEmpty(headimg)) {
            if (!TextUtils.isEmpty(mChannelId)) {
                prepareLive();
            } else {
                commitBase();
            }
        } else {
            upLoadImage();
        }
    }

    /**
     * This is the method that is hit after the user accepts/declines the
     * permission you requested. For the purpose of this example I am showing a "success" header
     * when the user accepts the permission and a snackbar when the user declines it.  In your application
     * you will want to handle the accept/decline in a way that makes sense.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (polyvPermission.operationHasPermission(requestCode)) {
            gotoPlay();
        } else {
            polyvPermission.makePostRequestSnack();
        }
    }

    private void showpopupWindow(View v) {
        if (resolutionList.size() == 0) {
            resolutionList.add("超清");
            resolutionList.add("高清");
            resolutionList.add("标清");
        }
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.popwindow_layout, null);
        rv_list = (RecyclerView) view.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        if (adapter == null) {
            adapter = new ResolutionItemAdapter(this);
            rv_list.setAdapter(adapter);
            adapter.addList(resolutionList);
            adapter.setClickListerner(new RecyclerViewCommonInterface() {
                @Override
                public void onClick(Object bean) {

                    if (bean != null) {
                        String type = (String) bean;
                        if (type.equals("标清")) {
                            mDefinition = PolyvRTMPDefinition.LIU_CHANGE;
                        } else if (type.equals("高清")) {
                            mDefinition = PolyvRTMPDefinition.GAO_QING;
                        } else if (type.equals("超清")) {
                            mDefinition = PolyvRTMPDefinition.CHAO_QING;
                        }
                        tv_logoff.setText(type);
                    }
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
            });
        }
        if (popupWindow == null) {
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        }
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_background));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.MyPopupWindow_anim_style);


        // PopupWindow弹出位置
        popupWindow.showAsDropDown(v, 0, 0, Gravity.CENTER);

//        backgroundAlpha(0.5f);
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//                backgroundAlpha(1f);
//            }
//        });
    }
    ///////////////////////////////////////////////////////////////////上传头像///////////////////////////////////////////////////////

    /**
     * 选择图片dialog
     */
    public void photodialog() {

        View view = View.inflate(PolyvSettingActivity.this, R.layout.dialog_publish_photo, null);
        showdialog1(view);

        final TextView photo = (TextView) view.findViewById(R.id.photo);
        final TextView picture = (TextView) view.findViewById(R.id.picture);
        // 从图库中选择
        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (photo.getText().toString().contains(getResources().getString(R.string.publish_photo))) {

                    Intent intent = new Intent(PolyvSettingActivity.this, TakeSimpleActivity.class);
                    intent.putExtra("Type", 1);
                    //头像
                    startActivityForResult(intent, resultCode_header_Photos);
                    myDialog.dismiss();
                }

            }
        });
        // 拍照
        picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (picture.getText().toString().contains(getResources().getString(R.string.publish_picture))) {
                    Intent intent = new Intent(PolyvSettingActivity.this, TakeSimpleActivity.class);
                    intent.putExtra("Type", 0);
                    //头像
                    startActivityForResult(intent, resultCode_header_Camera);

                    myDialog.dismiss();
                }
            }
        });

    }

    private MyDialog myDialog;


    /**
     * 弹出dialog
     *
     * @param view
     */
    private void showdialog1(View view) {

        myDialog = new MyDialog(this, 0, 0, view, R.style.dialog);
        myDialog.show();
    }

    /**
     * 上传头像
     */
    private void upLoadImage() {
        List<MultipartBody.Part> parts = UploadFile.filesToMultipartBody(list);
        if (parts.size() <= 0) {
            return;
        }
        DialogUtils.showDialog(this, "准备中...", false);
        upLoadImageCall = RestAdapterManager.getApi().uploadCover(parts.get(0));
        upLoadImageCall.enqueue(new JyCallBack<SuperBean<String>>() {
            @Override
            public void onSuccess(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
//                UIUtil.showToast(response.body());
//                DialogUtils.closeDialog();
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    //上传图片成功
                    headimg = response.body().getData();
                    LogUtils.e(headimg);
//                        UIUtil.showToast("成功" + headimg);
                    commitBase();
                } else {
                    UIUtil.showToast(response.body().getMsg());
                    DialogUtils.closeDialog();
                }
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("上传图片失败");
            }

            @Override
            public void onError(Call<SuperBean<String>> call, Response<SuperBean<String>> response) {
                try {
                    DialogUtils.closeDialog();
                    ErrorMessageUtils.taostErrorMessage(PolyvSettingActivity.this, response.errorBody().string(), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void commitBase() {
        Map<String, String> map = new HashMap<>();
        map.put("coverImage", headimg);
        map.put("id", "");
        map.put("liveDescribe", et_title.getText().toString());
        map.put("name", et_company_name.getText().toString());
        map.put("publisher", et_host_name.getText().toString());
        String tag = "";
        String tagCn = "";
        for (int i = 0; i < tagSelectedList.size(); i++) {
            tag += tagSelectedList.get(i).getCId() + ",";
            tagCn += tagSelectedList.get(i).getCName() + ",";
        }
        if (!TextUtils.isEmpty(tag)) {
            tag = tag.substring(0, tag.length() - 1);
        }
        if (!TextUtils.isEmpty(tagCn)) {
            tagCn = tagCn.substring(0, tag.length() - 1);
        }
        map.put("tag", tag);
        map.put("tagCn", tagCn);
        if (BaseContext.getInstance().getUserInfo() != null) {
            map.put("uid", BaseContext.getInstance().getUserInfo().uid + "");
        }
        Call<CreateLiveBean> call = RestAdapterManager.getApi().createLive(map);
        call.enqueue(new JyCallBack<CreateLiveBean>() {
            @Override
            public void onSuccess(Call<CreateLiveBean> call, Response<CreateLiveBean> response) {

                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    mChannelId = response.body().getData().getChannelId() + "";
                    userId = response.body().getData().getUserId() + "";
                    nickName = response.body().getData().getPublisher() + "";
//                    new PolyvTitleUpdate().updateTitle(mChannelId, title, null);
                    prepareLive();
                } else {
                    try {
                        UIUtil.showToast(response.body().getMsg());
                        DialogUtils.closeDialog();
                    } catch (Exception e) {
                    }
                }

            }

            @Override
            public void onError(Call<CreateLiveBean> call, Throwable t) {
                DialogUtils.closeDialog();
            }

            @Override
            public void onError(Call<CreateLiveBean> call, Response<CreateLiveBean> response) {
                DialogUtils.closeDialog();
            }
        });
    }

    private void prepareLive() {
        PolyvRTMPLoginVerify.verify(mChannelId, LIVE_PASSWORD, new IPolyvRTMPLoginListener() {
            @Override
            public void onError(PolyvRTMPLoginErrorReason errorReason) {
                switch (errorReason.getType()) {
                    case PolyvRTMPLoginErrorReason.SERVER_STATUS_EMPTY:
                        UIUtil.showToast("服务返回状态为空");
                        break;
                    case PolyvRTMPLoginErrorReason.SERVER_STATUS_FAIL:
                        UIUtil.showToast(errorReason.getMsg());
                        break;
                    case PolyvRTMPLoginErrorReason.NETWORK_DENIED:
                        UIUtil.showToast("无法连接网络");
                        break;
                    case PolyvRTMPLoginErrorReason.DATA_ERROR:
                        UIUtil.showToast("数据错误");
                        break;
                    case PolyvRTMPLoginErrorReason.CHANNEL_ID_EMPTY:
                        UIUtil.showToast("请输入直播频道ID");
                        break;
                    case PolyvRTMPLoginErrorReason.PASSWORD_EMPTY:
                        UIUtil.showToast("请输入直播密码");
                        break;
                    case PolyvRTMPLoginErrorReason.REQUEST_SERVER_FAIL:
                        UIUtil.showToast("登陆失败");
                        break;
                }
            }

            @Override
            public void onSuccess(String[] preview_nickname_avatar) {
                PolyvScreenUtils.setDecorVisible(PolyvSettingActivity.this);
                // 初始化分享配置
//                PolyvShareFragment.initShareConfig(preview_nickname_avatar[0], preview_nickname_avatar[2], null, null);
//
//                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(LAST_ACCOUNT_ID_KEY, channelId);
//                editor.commit();
//
//                Set<String> accountIdList = sharedPreferences.getStringSet(ACCOUNT_ID_LIST_KEY, new HashSet<String>());
//                if (!accountIdList.contains(channelId)) {
//                    accountIdList.add(channelId);
//                    editor = sharedPreferences.edit();
//                    editor.putStringSet(ACCOUNT_ID_LIST_KEY, accountIdList);
//                    editor.commit();
//                }
//
//                if (!isSavePasswordCB.isChecked()) {
//                    if (sharedPreferences.contains(channelId)) {
//                        editor = sharedPreferences.edit();
//                        editor.remove(channelId);
//                        editor.commit();
//                    }
//
//                    if (sharedPreferences.contains(getCheckSelectKey(channelId))) {
//                        editor = sharedPreferences.edit();
//                        editor.remove(getCheckSelectKey(channelId));
//                        editor.commit();
//                    }
//                } else {
//                    editor = sharedPreferences.edit();
//                    editor.putString(channelId, password);
//                    editor.commit();
//
//                    if (!sharedPreferences.contains(getCheckSelectKey(channelId))) {
//                        editor = sharedPreferences.edit();
//                        editor.putBoolean(getCheckSelectKey(channelId), true);
//                        editor.commit();
//                    }
//                }
//                polyvRTMPView.pause();
                polyvRTMPView.dispose();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.closeDialog();
                        Intent intent = new Intent(PolyvSettingActivity.this, PolyvMainActivity.class);
                        intent.putExtra("channelId", mChannelId);
                        intent.putExtra("orientation", mOrientation);
                        intent.putExtra("definition", mDefinition);
                        intent.putExtra("avatarUrl", headimg);
                        intent.putExtra("userId", userId);
                        intent.putExtra("nickName", nickName);
                        startActivity(intent);
                    }
                }, 1000);
            }
        }, this);
    }

    /**
     * 根据对应的分类id获取对应的数据
     */

    private void getTagItmes() {
        List<String> list = new ArrayList<>();
        list.add("QS_jobtag");
        GetBaseTagBean tagBean = new GetBaseTagBean();
        tagBean.setAlias(list);
        Call<CommonTagBean> commonTagBeanCall = RestAdapterManager.getApi().getWorkAgeAndResume(tagBean);
        commonTagBeanCall.enqueue(new JyCallBack<CommonTagBean>() {
            @Override
            public void onSuccess(Call<CommonTagBean> call, Response<CommonTagBean> response) {
                LogUtils.e(response.body().getMsg());
                if (response != null && response.body() != null && response.body().getCode() == Constants.successCode) {
                    commonTagBean = response.body();
                    setTagData();
                } else {
                    UIUtil.showToast(response.body().getMsg());
                }
            }

            @Override
            public void onError(Call<CommonTagBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onError(Call<CommonTagBean> call, Response<CommonTagBean> response) {

            }
        });
    }

    private void setTagData() {
        nsv_scroll.setNestedScrollingEnabled(false);
        tagSelectedList.clear();
        if (commonTagBean != null && commonTagBean.getData() != null && commonTagBean.getData().getQS_jobtag() != null && commonTagBean.getData().getQS_jobtag().size() > 0) {
            an_tags.setLayoutManager(new GridLayoutManager(this, 4));
            List<CommonTagItemBean> items;
            if (commonTagBean.getData().getQS_jobtag().size() > 8) {
                items = commonTagBean.getData().getQS_jobtag().subList(0, 8);
            } else {
                items = commonTagBean.getData().getQS_jobtag();
            }
            tagAdapter = new LiveTagAdapter(this, items);
            an_tags.setAdapter(tagAdapter);
            tagAdapter.setCommonInterface(new RecyclerViewCommonInterface() {
                @Override
                public void onClick(Object bean) {
                    CommonTagItemBean tagBean = (CommonTagItemBean) bean;
                    if (tagSelectedList.size() <= 0) {
                        tagSelectedList.add(tagBean);
                    } else {
                        if (tagSelectedList.contains(tagBean)) {
                            tagSelectedList.remove(tagBean);
                        } else {
                            if (tagSelectedList.size() >= 3) {
                                UIUtil.showToast("最多可选择三个标签");
                            } else {
                                tagSelectedList.add(tagBean);
                            }
                        }
                    }
                    tagAdapter.setSelectedList(tagSelectedList);
                }
            });
        }


    }
}
