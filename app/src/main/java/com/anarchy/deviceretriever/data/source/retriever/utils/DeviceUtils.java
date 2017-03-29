package com.anarchy.deviceretriever.data.source.retriever.utils;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/14 16:20
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public class DeviceUtils {
    private static long sMaxCpuFreq = 0;
    private static int sTotalMemory = 0;
    //手机CPU主频大小
    public static long getMaxCpuFreq() {
        if (sMaxCpuFreq > 0) {
            return sMaxCpuFreq;
        }
        ProcessBuilder cmd;
        String cpuFreq = "";
        try {
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            java.lang.Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                cpuFreq = cpuFreq + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            cpuFreq = "";
        }
        cpuFreq = cpuFreq.trim();
        if (cpuFreq == null || cpuFreq.length() == 0) {
            // 某些机器取到的是空字符串，如：OPPO U701
            sMaxCpuFreq = 1;
        } else {
            try {
                sMaxCpuFreq = Long.parseLong(cpuFreq);
            } catch (NumberFormatException e) {
                sMaxCpuFreq = 1;
                e.printStackTrace();
            }
        }
        return sMaxCpuFreq;
    }

    public static int getTotalMemoryInMb() {
        return getTotalMemoryInKb() >> 10;
    }

    public static int getTotalMemoryInKb() {
        if (sTotalMemory > 0) {
            return sTotalMemory;
        }
        String str1 = "/proc/meminfo";
        String str2 = "";
        String[] arrayOfString;
        FileReader fr = null;
        BufferedReader localBufferedReader = null;
        try {
            fr = new FileReader(str1);
            localBufferedReader = new BufferedReader(fr, 8192);
            if ((str2 = localBufferedReader.readLine()) != null) {

                arrayOfString = str2.split("\\s+");
//                for(String num : arrayOfString){
//                	Log.i(str2, num+"\t");
//                }
                sTotalMemory = Integer.valueOf(arrayOfString[1]).intValue();
            }
        } catch (IOException e) {

        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                if (localBufferedReader != null) {
                    localBufferedReader.close();
                }
            } catch (IOException e) {
            }
        }
        return sTotalMemory;
    }

    public static int getFreeMemoryInKb() {
        String str1 = "/proc/meminfo";
        String str2 = "";
        String[] arrayOfString;
        FileReader fr = null;
        BufferedReader localBufferedReader = null;
        int freeMem = 0;
        try {
            fr = new FileReader(str1);
            localBufferedReader = new BufferedReader(fr, 8192);
            int line = 0;
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (++line == 2) {
                    arrayOfString = str2.split("\\s+");
                    freeMem = Integer.valueOf(arrayOfString[1]).intValue();
                    break;
                }
            }
        } catch (IOException e) {

        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                if (localBufferedReader != null) {
                    localBufferedReader.close();
                }
            } catch (IOException e) {
            }
        }
        return freeMem;
    }

    /**
     * 获取当前手机系统的最大的可用堆栈大小
     *
     * @return
     */
    public static long getMaxHeapSizeInBytes(Context context) {
        long max = Runtime.getRuntime().maxMemory();
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            long memoryClass = am.getMemoryClass() << 20;
            if (max > memoryClass) {
                max = memoryClass;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return max;
    }

    /**
     * 获取当前已经分配的堆栈的比例
     *
     * @return
     */
    public static float getHeapAllocatePercent() {
        long heapAllocated = Runtime.getRuntime().totalMemory();
        long heapMax = Runtime.getRuntime().maxMemory();
        return Math.round(heapAllocated * 10000.0f / heapMax) / 100.0f;
    }

    /**
     * 获取当前已经使用的堆栈占总堆栈的比例
     *
     * @return
     */
    public static float getHeapUsedPercent(Context context) {
        long heapUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long heapMax = getMaxHeapSizeInBytes(context);
        return Math.round(heapUsed * 10000.0f / heapMax) / 100.0f;
    }

    /**
     * 获取整个max heap里面除去已经使用的heap还可以再分配的heap大小
     *
     * @return
     */
    public static long getHeapRemainInBytes(Context context) {
        return getMaxHeapSizeInBytes(context) - (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }

    public static String getCpuHardware(){
        String r = null;
        ProcessBuilder pb = new ProcessBuilder("/system/bin/cat","/proc/cpuinfo");
        try {
            Process process = pb.start();
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String result;
            while ((result = bufferedReader.readLine()) != null){
                if(result.contains("Hardware")){
                    String[] a = result.split(":",2);
                    r = a[1];
                }
            }
            bufferedReader.close();
            return r;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }


    public static String getCpuName(){
        String result;
        result = getArmCPUName();
        if (result == null){
            result = getX86CPUName();
        }
        if(result == null){
            result = getMIPSCPUName();
        }
        return result;
    }

    public static String getX86CPUName() {
        String aLine = "Intel";
        if (new File("/proc/cpuinfo").exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
                String strArray[] = new String[2];
                while ((aLine = br.readLine()) != null) {
                    if(aLine.contains("model name")){
                        br.close();
                        strArray = aLine.split(":", 2);
                        aLine = strArray[1];
                    }
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return aLine;
    }

    public static String getMIPSCPUName() {
        String aLine = "MIPS";
        if (new File("/proc/cpuinfo").exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
                String strArray[] = new String[2];
                while ((aLine = br.readLine()) != null) {
                    if(aLine.contains("cpu model")){
                        br.close();
                        strArray = aLine.split(":", 2);
                        aLine = strArray[1];
                    }
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return aLine;
    }

    public static String getArmCPUName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            br.close();
            String[] array = text.split(":\\s+", 2);
            if (array.length >= 2) {
                return array[1];
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = getBlockSize(stat);
        long availableBlocks = getAvailableBlockCount(stat);
        return CommonUtils.formatSize(availableBlocks * blockSize);
    }

    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = getBlockSize(stat);
        long totalBlocks = getBlockCount(stat);
        return CommonUtils.formatSize(totalBlocks * blockSize);
    }

    public static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = getBlockSize(stat);
            long availableBlocks = getAvailableBlockCount(stat);
            return CommonUtils.formatSize(availableBlocks * blockSize);
        } else {
            return null;
        }
    }

    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = getBlockSize(stat);
            long totalBlocks = getBlockCount(stat);
            return CommonUtils.formatSize(totalBlocks * blockSize);
        } else {
            return null;
        }
    }



    private static long getBlockSize(StatFs statFs){
        if(Build.VERSION.SDK_INT >= 18){
            return  statFs.getBlockSizeLong();
        }else {
            return statFs.getBlockSize();
        }
    }

    private static long getBlockCount(StatFs statFs){
        if(Build.VERSION.SDK_INT >= 18){
            return statFs.getBlockCountLong();
        }else {
            return statFs.getBlockCount();
        }
    }

    private static long getAvailableBlockCount(StatFs statFs){
        if(Build.VERSION.SDK_INT >= 18){
            return statFs.getAvailableBlocksLong();
        }else {
            return statFs.getAvailableBlocks();
        }
    }


    public  static boolean isRooted() {
        return findBinary("su");
    }



    private static boolean findBinary(String binaryName) {
        boolean found = false;
        if (!found) {
            String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
                    "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                if ( new File( where + binaryName ).exists() ) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    // permission android.permission.internet
    public static String getWifiMacAddress() {
        try {
            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                    continue;
                }

                byte[] mac = intf.getHardwareAddress();
                if (mac == null) {
                    return "";
                }

                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02X:", aMac));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString();
            }
        } catch (Exception exp) {

            exp.printStackTrace();
        }
        return "";
    }


    public static String getLocalIpAddress() {
        try {
            if (NetworkInterface.getNetworkInterfaces() != null) {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    if (intf != null) {
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress()
                                    && inetAddress instanceof Inet4Address) {
                                if (!inetAddress.getHostAddress().equals("null")
                                        && inetAddress.getHostAddress() != null) {
                                    return inetAddress.getHostAddress().trim();
                                }
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * Returns MAC address of the given interface name.
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return  mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx=0; idx<mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    public static boolean isMockLocationEnabled(@NonNull Context context)
    {
        boolean isMockLocation = false;
        try
        {
            //if marshmallow
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                AppOpsManager opsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                isMockLocation = (opsManager.checkOp(AppOpsManager.OPSTR_MOCK_LOCATION, android.os.Process.myUid(),context.getPackageName())== AppOpsManager.MODE_ALLOWED);
            }
            else
            {
                // in marshmallow this will always return true
                isMockLocation = !android.provider.Settings.Secure.getString(context.getContentResolver(), "mock_location").equals("0");
            }
        }
        catch (Exception e)
        {
            return isMockLocation;
        }

        return isMockLocation;
    }


    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
}
