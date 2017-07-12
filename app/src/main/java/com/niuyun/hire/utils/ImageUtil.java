package com.niuyun.hire.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import com.niuyun.hire.base.BaseContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 39157 on 2016/11/10.
 */

public class ImageUtil {


    public final static String uploadImagePath = "/upload_feedback/";//意见反馈

    public static Bitmap parseBitmapToSize(String path, int reqWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不占用内存加载图片
        BitmapFactory.decodeFile(path, options);
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;
        int inSampleSize = 1;
        if (bitmapHeight > reqWidth || bitmapWidth > reqWidth) {
            if (bitmapWidth > bitmapHeight) {
                inSampleSize = Math.round((float) bitmapHeight / (float) reqWidth);
            } else {
                inSampleSize = Math.round((float) bitmapWidth / (float) reqWidth);
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        LogUtils.log("---压缩后bitMap---getWidth->" + bitmap.getWidth() + "---getWidth-->" + bitmap.getHeight());
        return bitmap;
    }


    /**
     * 写图片文件到SD卡 并且压缩
     *
     * @return 处理后的图片路径
     */
    public static String saveImageToSD(Bitmap source, int quality, int bitmapOutSize) {
        FileOutputStream fos = null;
        ByteArrayOutputStream stream = null;
        Bitmap thumb = null;
        try {
            if (source != null) {
                String filePath = ImageUtil.setCameraImgPath(BaseContext.getInstance());
                fos = new FileOutputStream(filePath);
                stream = new ByteArrayOutputStream();
                int with = source.getWidth();//图片的宽
                int height = source.getHeight();//图片的高
                int max = with > height ? with : height;
                if (max > bitmapOutSize) {
                    //缩放
                    Matrix matrix = new Matrix();
                    float scale = bitmapOutSize * 1f / max;
                    matrix.setScale(scale, scale);
                    thumb = Bitmap.createBitmap(source, 0, 0, with, height, matrix, true);
                    thumb.compress(Bitmap.CompressFormat.JPEG, quality, stream);
                } else {
                    source.compress(Bitmap.CompressFormat.JPEG, quality, stream);
                }
                byte[] bytes = stream.toByteArray();
                fos.write(bytes);
                fos.flush();
                return filePath;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (!source.isRecycled()) {
                source.recycle();
            }
            if (thumb != null && thumb.isRecycled()) {
                thumb.recycle();
            }
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 设置保存 照片的路径
     *
     * @param context
     * @return 保存图片的路径
     */
    public static String setCameraImgPath(Context context) {
        String cameraImgPath = null;
        String foloder = ImageUtil.getCachePath(context) + uploadImagePath;
        File savedir = new File(foloder);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }
        String timeStamp = System.currentTimeMillis() + "";
        // 照片命名
        String picName = timeStamp + ".jpg";
        //  裁剪头像的绝对路径
        cameraImgPath = foloder + picName;
        return cameraImgPath;
    }

    /**
     * 获取缓存路径
     *
     * @param context
     * @return
     */
    public static String getCachePath(Context context) {
        File cacheDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                !Environment.isExternalStorageRemovable()) {
            cacheDir = context.getExternalCacheDir();
        } else {
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    /**
     * 删除数据
     *
     * @param context
     * @return
     */
    public static void deleteCachePath(Context context, String dataType) {
        String foloder = ImageUtil.getCachePath(context) + dataType;
        final File savedir = new File(foloder);
        if (savedir.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    delSDFile(savedir);
                }
            }).start();
        }
    }

    /**
     * 删除SD下制定文件
     *
     * @param file
     * @return
     */
    public static boolean delSDFile(File file) {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                if (file.exists()) {
                    File[] arrayOfFile;
                    arrayOfFile = file.listFiles();
                    int length = arrayOfFile.length;
                    for (int index = 0; index < length; index++) {
                        if (arrayOfFile[index].isDirectory()) {
                            delSDFile(arrayOfFile[index]);
                        } else {
                            arrayOfFile[index].delete();
                        }
                    }
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
