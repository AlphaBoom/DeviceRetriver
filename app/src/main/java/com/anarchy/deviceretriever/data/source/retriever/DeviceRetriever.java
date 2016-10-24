package com.anarchy.deviceretriever.data.source.retriever;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;

import com.anarchy.deviceretriever.data.Info;
import com.anarchy.deviceretriever.data.source.retriever.utils.DeviceUtils;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/20 10:37
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public class DeviceRetriever extends BasePermissionRetriever {


    public DeviceRetriever(Context context) {
        super(context);
    }

    @Override
    public List<Info> retrieve() {
        return retrieve(true);
    }

    @Override
    List<Info> doRetrieve(boolean ignorePermission) {
        //Android 系统版本
        mResult.add(new Info("release version", Build.VERSION.RELEASE,"Android 系统版本"));
        mResult.add(new Info("release version int",String.valueOf(Build.VERSION.SDK_INT),"Android 系统版本 数字"));
        //设备型号
        mResult.add(new Info("model",Build.MODEL,"设备型号"));
        //产品代号
        mResult.add(new Info("brand",Build.BRAND,"手机品牌"));
        //设备序列号
        mResult.add(new Info("serial number",Build.SERIAL,"设备序列号"));
        //ROM编号
        mResult.add(new Info("display",Build.DISPLAY,"ROM编号"));
        //HOST
        mResult.add(new Info("host",Build.HOST,"HOST"));
        //设备名称
        mResult.add(new Info("device name",Build.DEVICE,"设备名称"));
        //硬件
        mResult.add(new Info("hardware",Build.HARDWARE,"硬件明后才能"));
        //ROM 标签
        mResult.add(new Info("tags",Build.TAGS,"ROM 标签"));
        //当前时间
        mResult.add(new Info("current time",String.valueOf(System.currentTimeMillis()),"当前时间"));
        //开机时间
        mResult.add(new Info("boot time",String.valueOf(System.currentTimeMillis() - SystemClock.elapsedRealtime()),"开机时间"));
        //待机时间
        mResult.add(new Info("up time",String.valueOf(SystemClock.elapsedRealtime() - SystemClock.uptimeMillis()),"待机时间"));
        //运行时间
        mResult.add(new Info("active time",String.valueOf(SystemClock.uptimeMillis()),"运行时间"));
        //时区
        mResult.add(new Info("time zone", TimeZone.getDefault().getDisplayName(),"时区"));
        //语言
        mResult.add(new Info("language", Locale.getDefault().getLanguage(),"语言"));
        //基带版本
        mResult.add(new Info("base band version",Build.getRadioVersion(),"基带版本"));
        //屏幕分辨率
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        mResult.add(new Info("screen resolution",displayMetrics.widthPixels + " x " + displayMetrics.heightPixels,"屏幕分辨率"));
        //Android ID
        mResult.add(new Info("android id", Settings.System.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID),"Android ID"));
        //屏幕 亮度
        mResult.add(new Info("brightness", Settings.System.getString(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS),"屏幕亮度"));
        //bluetooth mac
        mResult.add(new Info("bluetooth mac", Settings.Secure.getString(mContext.getContentResolver(),"bluetooth_address"),"蓝牙 MAC"));
        //cpu 主频
        mResult.add(new Info("cpu frequency",String.valueOf(DeviceUtils.getMaxCpuFreq()),"cpu 主频"));
        //cpu type
        mResult.add(new Info("cpu type",DeviceUtils.getCpuName()));
        //cpu 硬件信息
        mResult.add(new Info("cpu hardware",DeviceUtils.getCpuHardware()));
        //内存大小
        mResult.add(new Info("total memory",DeviceUtils.getTotalMemoryInMb() + "Mb"));
        //可用内存大小
        mResult.add(new Info("available memory", (DeviceUtils.getFreeMemoryInKb() >> 10) + "Mb"));
        //最大堆栈大小
        mResult.add(new Info("max heap size",(DeviceUtils.getMaxHeapSizeInBytes(mContext) >> 20) + "Mb"));
        //堆栈已分配占比
        mResult.add(new Info("heap allocation percent",DeviceUtils.getHeapAllocatePercent() + "%"));
        //已经使用的堆栈占比
        mResult.add(new Info("heap used percent",DeviceUtils.getHeapUsedPercent(mContext) + "%"));
        //内部存储总大小
        mResult.add(new Info("total internal storage",DeviceUtils.getTotalInternalMemorySize()));
        //内部存储可用大小
        mResult.add(new Info("available internal storage",DeviceUtils.getAvailableInternalMemorySize()));
        //外部存储总大小
        mResult.add(new Info("total external storage",DeviceUtils.getTotalExternalMemorySize()));
        //外部存储可用大小
        mResult.add(new Info("available external storage",DeviceUtils.getAvailableExternalMemorySize()));
        //设备是否root
        mResult.add(new Info("rooted",String.valueOf(DeviceUtils.isRooted()),"设备是否已root"));
        //是否为模拟器
        mResult.add(new Info("is emulator",String.valueOf(DeviceUtils.isEmulator()),"是否为模拟器"));
        //kernel version
        mResult.add(new Info("kernel version",System.getProperty("os.version")));
        //font hash
        mResult.add(new Info("font hash",String.valueOf(Typeface.DEFAULT.hashCode())));
        //allow mock location
        mResult.add(new Info("allow mock location",String.valueOf(DeviceUtils.isMockLocationEnabled(mContext)),"是否允许模拟位置信息"));
        //电磁信息  sticky broadcast
        IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryIntent = mContext.registerReceiver(null, batteryFilter);
        if (batteryIntent != null) {
            int batterStatus = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
            String batteryStatusName = "battery status";
            switch (batterStatus) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    mResult.add(new Info(batteryStatusName,"CHARGING"));
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    mResult.add(new Info(batteryStatusName,"DISCHARGING"));
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    mResult.add(new Info(batteryStatusName,"FULL"));
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    mResult.add(new Info(batteryStatusName,"NOT_CHARGING"));
                    break;
                default:
                    mResult.add(new Info(batteryStatusName,"UNKNOWN"));
            }
            String batteryTemp = Integer.toString(batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1));
            String batteryLevel = Integer.toString(batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1));
            mResult.add(new Info("battery level",batteryLevel,"当前电量"));
            mResult.add(new Info("battery temperature",batteryTemp,"电池温度"));
        }
        return mResult;
    }

    @Override
    public String[] checkUnGrantedPermission() {
        return new String[0];
    }

}
