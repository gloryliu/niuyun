package com.niuyun.hire.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by sun.luwei on 2016/12/20.
 */

public class ChannelUtil {

    public static String OFFICAL = "22";

    public static String getChannel(Context context) {
        String channel = null;

        SharedPreferences sharedPreferences = context.getSharedPreferences("UMSAPPCHANNEL", Context.MODE_PRIVATE);
        channel = sharedPreferences.getString("UMSAPPCHANNEL", null);
        if (channel != null) {
            return channel;
        }

        final String start_flag = "META-INF/jyall_health_channel_";
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.contains(start_flag)) {
                    channel = entryName.replace(start_flag, "");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (channel == null || channel.length() <= 0) {
            channel = OFFICAL;//读不到渠道号就默认官方渠道
        }
        sharedPreferences.edit().putString("UMSAPPCHANNEL", channel).commit();
        return channel;
    }
}
