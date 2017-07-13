package com.niuyun.hire.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.api.JyCallBack;
import com.niuyun.hire.api.RestAdapterManager;
import com.niuyun.hire.base.BaseActivity;
import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.base.EventBusCenter;
import com.niuyun.hire.ui.bean.UserInfoBean;
import com.niuyun.hire.utils.DialogUtils;
import com.niuyun.hire.utils.ErrorMessageUtils;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.utils.LogUtils;
import com.niuyun.hire.utils.UIUtil;
import com.niuyun.hire.utils.UploadFile;
import com.niuyun.hire.utils.photoTool.PhotoPicActivity;
import com.niuyun.hire.utils.photoTool.TakingPicturesActivity;
import com.niuyun.hire.utils.timepicker.TimePickerView;
import com.niuyun.hire.view.CircularImageView;
import com.niuyun.hire.view.MyDialog;
import com.niuyun.hire.view.TitleBar;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen.zhiwei on 2017-6-22.
 */

public class PersonInformationActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_view)
    TitleBar title_view;
    @BindView(R.id.tv_sick_name)
    EditText tv_sick_name;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
    @BindView(R.id.bt_commit)
    Button bt_commit;
    @BindView(R.id.iv_head)
    CircularImageView iv_head;
    private Call<String> upLoadImageCall;
    private Call<String> upLoadInfoCall;
    private String headimg;
    private String nickname;
    private String sex;
    private String birthday;

    List<String> list = new ArrayList<String>();

    private static final int resultCode_Photos = 10;//跳转到相册
    private static final int resultCode_Camera = 11;//跳转到相机

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_person_information;
    }

    @Override
    public void initViewsAndEvents() {
        initTitle();
        bt_commit.setOnClickListener(this);
        tv_sick_name.setOnClickListener(this);
        tv_sex.setOnClickListener(this);
        tv_birthday.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        tv_sick_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                nickname = tv_sick_name.getText().toString();
            }
        });
    }

    @Override
    public void loadData() {
        if (BaseContext.getInstance().getUserInfo() != null) {

            tv_sick_name.setText(BaseContext.getInstance().getUserInfo().nickname);
            if (!TextUtils.isEmpty(BaseContext.getInstance().getUserInfo().sex)) {
                tv_sex.setText(BaseContext.getInstance().getUserInfo().sex.equals("1") ? "男" : "女");
            }
            tv_birthday.setText(UIUtil.timeStamp2Date(BaseContext.getInstance().getUserInfo().birthday));
            ImageLoadedrManager.getInstance().displayNoFilter(this, BaseContext.getInstance().getUserInfo().headimg, iv_head);
        }
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

    private void upLoadInfo() {

        if (TextUtils.isEmpty(nickname) && TextUtils.isEmpty(sex) && TextUtils.isEmpty(birthday) && TextUtils.isEmpty(headimg)) {
            UIUtil.showToast("信息没有更改");
            return;
        }
        DialogUtils.showDialog(this, "上传中", false);
        Map<String, String> map = new HashMap<>();
        map.put("userId", BaseContext.getInstance().getUserInfo().userId);
        map.put("nickname", nickname);
        map.put("sex", sex);
        map.put("birthday", birthday);
        map.put("headimg", headimg);
        upLoadInfoCall = RestAdapterManager.getApi().upLoadInfo(map);
        upLoadInfoCall.enqueue(new JyCallBack<String>() {
            @Override
            public void onSuccess(Call<String> call, Response<String> response) {
                DialogUtils.closeDialog();
                if (response != null && response.body() != null) {
                    if (response.body().contains("1000")) {
                        UIUtil.showToast("修改成功");
                        UserInfoBean userInfo = BaseContext.getInstance().getUserInfo();
                        if (userInfo != null) {
                            if (!TextUtils.isEmpty(sex)) {
                                userInfo.sex = sex;
                            }
                            if (!TextUtils.isEmpty(headimg)) {
                                userInfo.headimg = headimg;
                            }
                            if (!TextUtils.isEmpty(nickname)) {
                                userInfo.nickname = nickname;
                            }
                            if (!TextUtils.isEmpty(birthday)) {
                                userInfo.birthday = birthday;
                            }
                            BaseContext.getInstance().updateUserInfo(userInfo);
                        }
                        sex = "";
                        headimg = "";
                        nickname = "";
                        birthday = "";
                        EventBus.getDefault().post(new EventBusCenter<Integer>(Constants.LOGIN_SUCCESS));
                    } else UIUtil.showToast("修改失败");
                } else {
                    UIUtil.showToast("修改失败");
                }


            }

            @Override
            public void onError(Call<String> call, Throwable t) {
                DialogUtils.closeDialog();
                UIUtil.showToast("修改失败");
            }

            @Override
            public void onError(Call<String> call, Response<String> response) {
                UIUtil.showToast("修改失败");
                DialogUtils.closeDialog();
            }
        });
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
                        upLoadInfo();
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
                    ErrorMessageUtils.taostErrorMessage(PersonInformationActivity.this, response.errorBody().string(), "");
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

    /**
     * 初始化标题
     */
    private void initTitle() {
        title_view.setLeftImageResource(R.mipmap.ic_title_back);
        title_view.setLeftText("返回");
        title_view.setLeftTextColor(Color.WHITE);
        title_view.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title_view.setBackgroundColor(getResources().getColor(R.color.color_ff6900));
        title_view.setImmersive(true);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_commit:
                //保存
                if (list.size() > 0) {
                    if (!TextUtils.isEmpty(headimg)) {
                        upLoadInfo();
                    } else
                        upLoadImage();
                } else {
                    upLoadInfo();
                }
                break;
            case R.id.tv_sex:
//性别
                sexDialog();
                break;
            case R.id.tv_birthday:
                //出生年月日
                DialogUtils.showTimePcikerDialog(this, new TimePickerView.OnTimeSelectListener() {

                    @Override
                    public void onTimeSelect(Date date) {
                        if (date != null) {
                            birthday = getTime(date);
                            tv_birthday.setText(birthday);
                        }
                    }
                });
                break;
            case R.id.iv_head:
                photodialog();
                break;
        }
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 选择图片dialog
     */
    public void photodialog() {

        View view = View.inflate(PersonInformationActivity.this, R.layout.dialog_publish_photo, null);
        showdialog(view);

        final TextView photo = (TextView) view.findViewById(R.id.photo);
        final TextView picture = (TextView) view.findViewById(R.id.picture);
        // 从图库中选择
        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (photo.getText().toString().contains(getResources().getString(R.string.publish_photo))) {

                    Intent intent = new Intent(PersonInformationActivity.this, PhotoPicActivity.class);
                    intent.putExtra("max", 9);

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
                    Intent intent = new Intent(PersonInformationActivity.this, TakingPicturesActivity.class);

                    startActivityForResult(intent, resultCode_Camera);
                    myDialog.dismiss();
                }
            }
        });

    }

    /**
     * 性别dialog
     */
    public void sexDialog() {

        View view = View.inflate(PersonInformationActivity.this, R.layout.dialog_publish_photo, null);
        showdialog(view);

        final TextView photo = (TextView) view.findViewById(R.id.photo);
        final TextView picture = (TextView) view.findViewById(R.id.picture);
        photo.setText("男");
        picture.setText("女");
        // 从图库中选择
        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setSexValue("男");
                myDialog.dismiss();

            }
        });
        // 拍照
        picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setSexValue("女");
                myDialog.dismiss();
            }
        });

    }

    private void setSexValue(String sex) {

        if (!TextUtils.isEmpty(sex)) {
            this.sex = ("男".equals(sex) ? "1" : "0");
            tv_sex.setText(sex);
        }
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
}
