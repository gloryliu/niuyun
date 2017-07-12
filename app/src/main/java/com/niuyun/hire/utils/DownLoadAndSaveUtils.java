package com.niuyun.hire.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.niuyun.hire.base.BaseContext;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by 39157 on 2016/10/19.
 */

public class DownLoadAndSaveUtils {
    private static volatile DownLoadAndSaveUtils downLoadAndSaveUtils = null;
    private static final String dir = BaseContext.getInstance().getFilesDir().getAbsolutePath();// data/data目录  应用的内存目录
    private static final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
//    private static final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/invoice/发票";//保存的确切位置

    private DownLoadAndSaveUtils() {
    }

    public static DownLoadAndSaveUtils getInstance() {
        if (downLoadAndSaveUtils == null) {
            synchronized (DownLoadAndSaveUtils.class) {
                if (downLoadAndSaveUtils == null) {
                    downLoadAndSaveUtils = new DownLoadAndSaveUtils();
                }
            }
        }
        return downLoadAndSaveUtils;
    }


    public interface DownLoadIntreface {
        public void downFail(int statusCode, String errorMsg);

        public void downSuccess(byte[] data);
    }




     /**
     * 保存图片到 SD卡
     *
     * @param bm       图片的 bitmap
     * @param fileName 保存的图片的名字
     * @param path     保存到的 文件夹路径
     * @return
     */
    public File savePicToSD(Bitmap bm, String path, String fileName) throws IOException {
        File file = createFile(true, path, fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        closeOutStream(bos);
        return file;
    }

    /**
     * 保存文件到SD卡
     *
     * @param data     文件
     * @param fileName 文件名字
     * @param path     被保存到的文件夹路径
     * @return
     * @throws IOException
     */
    public File saveFileToSD(byte[] data, String path, String fileName) throws IOException {
        File file = createFile(true, path, fileName);
        FileOutputStream bos = new FileOutputStream(file);
        bos.write(data, 0, data.length);
        closeOutStream(bos);
        return file;
    }

    public File saveFileToInternalStorage(byte[] data, String path,String fileName ) throws IOException {
        File file = createFile(false, path, fileName);
        FileOutputStream bos = new FileOutputStream(file);
        bos.write(data, 0, data.length);
        closeOutStream(bos);
        return file;
    }


    public void closeOutStream(OutputStream ous) throws IOException {
        if (ous != null) {
            ous.flush();
            ous.close();
        }
    }

    /**
     * 创建文件
     *
     * @param path     文件根目录
     * @param fileName 文件名字
     * @return
     * @throws IOException
     */
    public File createFile(boolean isSdcard, String path, String fileName) throws IOException {
        File foder = null;
        if (isSdcard) {
            foder = new File(Environment.getExternalStorageDirectory(), path);
        } else {
            foder = new File(BaseContext.getInstance().getFilesDir(), path);
        }
        if (!foder.exists()) {
            foder.mkdir();
        }
        File file = new File(foder, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }


    public boolean permissionsManager(Activity activity, int requestCode) {
        boolean isNeedApply = false;
        boolean isSdcardPermissions = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!isSdcardPermissions) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            isNeedApply = true;
        }
        return isNeedApply;
    }


}
