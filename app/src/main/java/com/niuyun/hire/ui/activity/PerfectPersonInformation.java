package com.niuyun.hire.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.utils.UploadFile;
import com.niuyun.hire.utils.photoTool.PhotoPicActivity;
import com.niuyun.hire.utils.photoTool.TakingPicturesActivity;
import com.niuyun.hire.view.CircularImageView;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-7-21.
 */

public class PerfectPersonInformation extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar titleView;
    @BindView(R.id.bt_next)
    Button bt_next;
    @BindView(R.id.rl_first)
    RelativeLayout rl_first;
    @BindView(R.id.ll_second)
    LinearLayout ll_second;
    @BindView(R.id.iv_intent_line)
    ImageView iv_intent_line;
    @BindView(R.id.iv_head)
    CircularImageView iv_head;
    @BindView(R.id.tv_intent_title)
    TextView tv_intent_title;
    private Call<String> upLoadImageCall;
    List<String> list = new ArrayList<String>();
    private String headimg;
    private static final int resultCode_Photos = 10;//跳转到相册
    private static final int resultCode_Camera = 11;//跳转到相机

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_perfect_person_information;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        bt_next.setOnClickListener(this);
        iv_head.setOnClickListener(this);
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

    private void initTitle() {

        titleView.setTitle("完善信息");
        titleView.setTitleColor(Color.WHITE);
        titleView.setBackgroundColor(getResources().getColor(R.color.color_e20e0e));
        titleView.setImmersive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_next:
                //下一步
                if (bt_next.getText().equals("下一步")) {
                    ll_second.setVisibility(View.VISIBLE);
                    rl_first.setVisibility(View.GONE);
                    bt_next.setText("完成");
                    tv_intent_title.setTextColor(getResources().getColor(R.color.color_ea0000));
                    iv_intent_line.setImageResource(R.mipmap.ic_line_right_red);
                    upLoadImage();
                } else {
//保存数据
                }
                break;
            case R.id.iv_head:
                //上传头像
                photodialog();
                break;
        }
    }

    /**
     * 选择图片dialog
     */
    public void photodialog() {

        View view = View.inflate(PerfectPersonInformation.this, R.layout.dialog_publish_photo, null);
        showdialog(view);

        final TextView photo = (TextView) view.findViewById(R.id.photo);
        final TextView picture = (TextView) view.findViewById(R.id.picture);
        // 从图库中选择
        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (photo.getText().toString().contains(getResources().getString(R.string.publish_photo))) {

                    Intent intent = new Intent(PerfectPersonInformation.this, PhotoPicActivity.class);
                    intent.putExtra("max", 1);

                    startActivityForResult(intent, resultCode_Photos);
                    myDialog.dismiss();
                }

            }
        });
        // 拍照
        picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (picture.getText().toString().contains(getResources().getString(R.string.publish_picture))) {
                    Intent intent = new Intent(PerfectPersonInformation.this, TakingPicturesActivity.class);

                    startActivityForResult(intent, resultCode_Camera);
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
    private void showdialog(View view) {

        myDialog = new MyDialog(this, 0, 0, view, R.style.dialog);
        myDialog.show();
    }

    private void upLoadImage() {
        DialogUtils.showDialog(this, "上传中", false);
        List<MultipartBody.Part> parts = UploadFile.filesToMultipartBody(list);
        upLoadImageCall = RestAdapterManager.getApi().uploadFile(parts.get(0));
        upLoadImageCall.enqueue(new JyCallBack<String>() {
            @Override
            public void onSuccess(Call<String> call, Response<String> response) {
//                UIUtil.showToast(response.body());
                DialogUtils.closeDialog();
                if (response != null && response.body() != null) {
                    if (!TextUtils.isEmpty(response.body())) {
                        //上传图片成功
                        headimg = response.body();
                        LogUtils.e(headimg);
                        UIUtil.showToast("成功" + headimg);
//                        upLoadInfo();
                    }
                }
            }

            @Override
            public void onError(Call<String> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("上传头像失败");
            }

            @Override
            public void onError(Call<String> call, Response<String> response) {
                try {
                    DialogUtils.closeDialog();
                    ErrorMessageUtils.taostErrorMessage(PerfectPersonInformation.this, response.errorBody().string(), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        list.clear();
        if (resultCode == resultCode_Camera) {
            //相机返回图片
            Bundle b = data.getExtras();
            String fileName = b.getString("picture");
            list.add(fileName);
        } else if (resultCode == resultCode_Photos) {
            // 图库中选择
            if (data == null || "".equals(data)) {
                return;
            }
            list = data.getExtras().getStringArrayList("photo");
            LogUtils.e("image路径--" + list.get(0));
        }
//        headIsChange = true;
        ImageLoadedrManager.getInstance().displayNoFilter(this, list.get(0), iv_head);
    }

}
