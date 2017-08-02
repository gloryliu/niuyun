package com.niuyun.hire.utils.photoutils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.niuyun.hire.R;
import com.niuyun.hire.base.Constants;

import java.io.File;
import java.util.ArrayList;

import static com.darsh.multipleimageselect.helpers.Constants.limit;


/**
 * - 支持通过相机拍照获取图片
 * - 支持从相册选择图片
 * - 支持从文件选择图片
 * - 支持多图选择
 * - 支持批量图片裁切
 * - 支持批量图片压缩
 * - 支持对图片进行压缩
 * - 支持对图片进行裁剪
 * - 支持对裁剪及压缩参数自定义
 * - 提供自带裁剪工具(可选)
 * - 支持智能选取及裁剪异常处理
 * - 支持因拍照Activity被回收后的自动恢复
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.cboy.me
 * GitHub:https://github.com/crazycodeboy
 * Eamil:crazycodeboy@gmail.com
 */
public class TakeSimpleActivity extends TakePhotoActivity {
    private int Type = 1;//0为拍照获取，1为从相册获取
    private int number = 1;//图片数量
    private int takeFrom = 0;//从那里获取图片，0为从相册，1为从文件中
    private boolean isNeedCrop = false;//是否需要裁剪，0是，1否
    private boolean isNeedCompress = false;//是否需要压缩，0是，1否
    /**
     * 如果需要裁剪，配置尺寸大小
     */
    private int height = 800;
    private int width = 800;
    private boolean isUseTakePhotoCrop;//裁剪工具是否用takephoto自带的

    /**
     * 如果需要压缩，配置尺寸大小
     */
    private int compressHeight = 800;
    private int compressWidth = 800;
    private boolean isUseSystem = true;//压缩工具是否使用系统自带的，否的话会用luban压缩；
    private int compressMaxSize=2048;//图片压缩后的大小
    private boolean isShowCompressProgressBar = false;//是否显示压缩进度条

    private boolean isSafeOrigin=false;//拍照压缩后是否保存原图

    private boolean isCorrectTPDirection = true;//是否纠正拍照的照片的旋转角度
    private boolean isUseTPAlbun = false;//是否用takephoto的自带相册


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_result_layout, null);
        setContentView(contentView);
        Type = getIntent().getExtras().getInt("Type", 1);
        init();
    }

    private void init() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        configTakePhotoOption(getTakePhoto());
//        configCompress(getTakePhoto());
        if (Type == 0) {

            if (isNeedCrop) {
                getTakePhoto().onPickFromCaptureWithCrop(imageUri, getCropOptions());
            } else {
                getTakePhoto().onPickFromCapture(imageUri);
            }

        } else {
            if (limit > 1) {
                if (isNeedCrop) {
                    getTakePhoto().onPickMultipleWithCrop(limit, getCropOptions());
                } else {
                    getTakePhoto().onPickMultiple(limit);
                }
                return;
            }
            if (takeFrom == 1) {
                if (isNeedCrop) {
                    getTakePhoto().onPickFromDocumentsWithCrop(imageUri, getCropOptions());
                } else {
                    getTakePhoto().onPickFromDocuments();
                }
                return;
            } else {
                if (isNeedCrop) {
                    getTakePhoto().onPickFromGalleryWithCrop(imageUri, getCropOptions());
                } else {
                    getTakePhoto().onPickFromGallery();
                }
            }
        }


    }


    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
        Intent i = new Intent();
        ArrayList<String> list = new ArrayList<>();
        for (int j = 0; j < images.size(); j++) {
            list.add(images.get(j).getOriginalPath());
        }


        if (Type == 0) {
            //拍照
            i.putStringArrayListExtra("picture", list);
            setResult(Constants.resultCode_header_Camera, i);
        } else {
            //相册
            i.putStringArrayListExtra("photo", list);
            setResult(Constants.resultCode_header_Photos, i);
        }
        finish();
    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        if (isUseTPAlbun) {
            builder.setWithOwnGallery(true);
        }
        if (isCorrectTPDirection) {
            builder.setCorrectImage(true);
        }
        takePhoto.setTakePhotoOptions(builder.create());

    }

    /**
     * 压缩配置
     *
     * @param takePhoto
     */
    private void configCompress(TakePhoto takePhoto) {
        if (isNeedCompress) {
            takePhoto.onEnableCompress(null, false);
            return;
        }
        CompressConfig config;
        if (isUseSystem) {
            config = new CompressConfig.Builder()
                    .setMaxSize(compressMaxSize)
                    .setMaxPixel(compressWidth >= compressHeight ? compressWidth : compressHeight)
                    .enableReserveRaw(isSafeOrigin)
                    .create();
        } else {
            LubanOptions option = new LubanOptions.Builder()
                    .setMaxHeight(compressHeight)
                    .setMaxWidth(compressWidth)
                    .setMaxSize(compressMaxSize)
                    .create();
            config = CompressConfig.ofLuban(option);
            config.enableReserveRaw(isSafeOrigin);
        }
        takePhoto.onEnableCompress(config, isShowCompressProgressBar);


    }


    /**
     * 裁剪配置
     *
     * @return
     */
    private CropOptions getCropOptions() {

        CropOptions.Builder builder = new CropOptions.Builder();

        if (isNeedCrop) {
            builder.setAspectX(width).setAspectY(height);
        } else {
            builder.setOutputX(width).setOutputY(height);
        }
        builder.setWithOwnCrop(isUseTakePhotoCrop);
        return builder.create();
    }

}
