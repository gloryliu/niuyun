package com.niuyun.hire.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/10/13.
 */

public class CacheManager {

    private Context mContext;

    public CacheManager(Context context) {
        this.mContext = context;
    }

    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     *
     * @param directory
     */
    public void deleteFilesByDirectory(File directory) {
        if (directory.isFile()) {
            directory.delete();
            return;
        }

        if (directory.isDirectory()) {
            File[] childFiles = directory.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                directory.delete();
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                deleteFilesByDirectory(childFiles[i]);
            }
            directory.delete();
        }
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public String getAutoFileOrFilesSize() {
        File cache = new File(mContext.getCacheDir().getPath());
        long blockSize = 0;
        try {
            if (cache.isDirectory()) {
                blockSize = getFileSizes(cache);
            } else {
                blockSize = getFileSize(cache);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param
     * @return
     * @throws Exception
     */
    private long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

}
