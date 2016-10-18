package com.anarchy.deviceretriever.retriever.utils;

import android.annotation.SuppressLint;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/18 14:29
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public class CommonUtils {
    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }
    public static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getMd5ByFile(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel()
                    .map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(byteBuffer);
            String hex = byteToHexString(digest.digest());
            return hex;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String byteToHexString(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                buffer.append(0);
            }
            buffer.append(hex);
        }
        return buffer.toString();
    }

    @SuppressLint("DefaultLocale")
    public static String formatIpInt(int ip){
        int[] bytes = new int[4];
        bytes[0] = ip&0xFF;
        bytes[1] = (ip >> 8) & 0xFF;
        bytes[2] = (ip >> 16) & 0xFF;
        bytes[3] = (ip >> 24) & 0xFF;
        return String.format("%d.%d.%d.%d",bytes[3],bytes[2],bytes[1],bytes[0]);
    }
}
