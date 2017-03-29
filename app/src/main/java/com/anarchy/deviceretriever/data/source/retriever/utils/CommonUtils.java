package com.anarchy.deviceretriever.data.source.retriever.utils;

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
 * <p/>
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
    public static String formatIpInt(int address){
        byte[] addr = new byte[4];

        addr[0] = (byte) ((address >>> 24) & 0xFF);
        addr[1] = (byte) ((address >>> 16) & 0xFF);
        addr[2] = (byte) ((address >>> 8) & 0xFF);
        addr[3] = (byte) (address & 0xFF);
        return String.format("%1$d.%2$d.%3$d.%4$d",addr[3]&0xFF,addr[2]&0xFF,addr[1]&0xFF,addr[0]&0xFF);
    }
}
