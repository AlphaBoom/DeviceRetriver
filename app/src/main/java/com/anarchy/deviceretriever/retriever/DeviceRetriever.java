package com.anarchy.deviceretriever.retriever;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.VpnService;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.anarchy.deviceretriever.R;
import com.anarchy.deviceretriever.retriever.utils.CommonUtils;
import com.anarchy.deviceretriever.retriever.utils.DeviceUtils;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/14 15:21
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */
public class DeviceRetriever {
    private static final String TAG = DeviceRetriever.class.getSimpleName();
    /**
     * 不分析数据
     */
    public static final int LEVEL_NONE = 0;
    /**
     * 解析不需要权限的数据，需要较少的时间获取
     */
    public static final int LEVEL_LOW = 1;
    /**
     * 解析不需要权限的数据，可能需要较多的时间获取
     */
    public static final int LEVEL_WEAK = 2;
    /**
     * 获取常用权限的数据
     */
    public static final int LEVEL_MEDIUM = 3;
    /**
     * 获取敏感权限的数据，可能需要用户授权
     */
    public static final int LEVEL_HIGH = 4;
    /**
     * 尽可能获取所有权限
     */
    public static final int LEVEL_MAX = 5;

    @IntDef({LEVEL_NONE, LEVEL_LOW, LEVEL_WEAK, LEVEL_MEDIUM, LEVEL_HIGH, LEVEL_MAX})
    @Retention(RetentionPolicy.SOURCE)
    @interface Level {

    }

    private final Context mContext;
    private DeviceInfo mDeviceInfo;

    public static DeviceRetriever getInstance(Context context) {
        return new DeviceRetriever(context);
    }

    private DeviceRetriever(@NonNull Context context) {
        mContext = context;
    }

    /**
     * 分析级别
     *
     * @param level
     * @return
     */
    public DeviceInfo analyze(@Level int level) {
        if (mDeviceInfo == null) mDeviceInfo = new DeviceInfo();
        if (level <= LEVEL_NONE) return mDeviceInfo;
        switch (level) {
            case LEVEL_LOW:
                analyzeLow(mDeviceInfo);
                break;
            case LEVEL_WEAK:
                analyzeWeak(mDeviceInfo);
                break;
            case LEVEL_MEDIUM:
                analyzeMedium(mDeviceInfo);
                break;
            case LEVEL_HIGH:
                analyzeHigh(mDeviceInfo);
                break;
            case LEVEL_MAX:
                analyzeMax(mDeviceInfo);
                break;
        }
        level--;
        return analyze(level);
    }


    private void analyzeLow(@NonNull DeviceInfo deviceInfo) {
        //获取Android系统版本
        deviceInfo.releaseVersion = Build.VERSION.RELEASE;
        deviceInfo.releaseVersionInt = Build.VERSION.SDK_INT;
        //设备型号
        deviceInfo.model = Build.MODEL;
        //产品代号
        deviceInfo.product = Build.PRODUCT;
        //手机品牌
        deviceInfo.brand = Build.BRAND;
        //设备序列号
        deviceInfo.serialNo = Build.SERIAL;
        //ROM编号
        deviceInfo.display = Build.DISPLAY;
        //HOST
        deviceInfo.host = Build.HOST;
        //设备名称
        deviceInfo.deviceName = Build.DEVICE;
        //硬件
        deviceInfo.hardware = Build.HARDWARE;
        //ROM标签
        deviceInfo.tags = Build.TAGS;
        //当前时间
        deviceInfo.currentTime = String.valueOf(System.currentTimeMillis());
        //开机时间
        deviceInfo.bootTime = String.valueOf(System.currentTimeMillis() - SystemClock.elapsedRealtime());
        //待机时间
        deviceInfo.upTime = String.valueOf(SystemClock.elapsedRealtime() - SystemClock.uptimeMillis());
        //运行时间
        deviceInfo.activeTime = String.valueOf(SystemClock.uptimeMillis());
        //时区
        deviceInfo.timeZone = TimeZone.getDefault().getDisplayName();
        //语言
        deviceInfo.language = Locale.getDefault().getLanguage();
        //屏幕分辨率
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        deviceInfo.screenRes = displayMetrics.widthPixels + " x " + displayMetrics.heightPixels;
        //基带版本
        deviceInfo.basebandVersion = Build.getRadioVersion();
        //应用包名及应用版本号等
        deviceInfo.packageName = mContext.getPackageName();
        deviceInfo.uid = String.valueOf(mContext.getApplicationInfo().uid);
        //Android id
        deviceInfo.androidId = Settings.System.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        //brightness
        deviceInfo.brightness = Settings.System.getString(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        //bluetooth address
        deviceInfo.blueMac = Settings.System.getString(mContext.getContentResolver(),"bluetooth_address");
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),PackageManager.GET_SIGNATURES);
            deviceInfo.apkVersion = packageInfo.versionName;
            Signature signature = packageInfo.signatures[0];
//            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
//            X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(signature.toByteArray()));
            deviceInfo.signMD5 = CommonUtils.getMessageDigest(signature.toByteArray());
            deviceInfo.apkMD5 = CommonUtils.getMd5ByFile(new File(mContext.getApplicationInfo().sourceDir));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void analyzeWeak(@NonNull DeviceInfo deviceInfo) {
        //获取cpu主频
        deviceInfo.cpuFrequency = String.valueOf(DeviceUtils.getMaxCpuFreq());
        //cpu type
        deviceInfo.cpuType = DeviceUtils.getCpuName();
        //cpu 硬件信息
        deviceInfo.cpuHardware = DeviceUtils.getCpuHardware();
        //内存大小
        deviceInfo.totalMemory = DeviceUtils.getTotalMemoryInMb() + "Mb";
        //可用内存大小
        deviceInfo.availableMemory = (DeviceUtils.getFreeMemoryInKb() >> 10) + "Mb";
        //最大堆栈大小
        deviceInfo.maxHeapSize = (DeviceUtils.getMaxHeapSizeInBytes(mContext) >> 20) + "Mb";
        //堆栈已分配占比
        deviceInfo.heapAllocatePercent = DeviceUtils.getHeapAllocatePercent() + "%";
        //已经使用的堆栈占比
        deviceInfo.heapUsedPercent = DeviceUtils.getHeapUsedPercent(mContext) + "%";
        //内部存储总大小
        deviceInfo.totalInternalStorage = DeviceUtils.getTotalInternalMemorySize();
        //内部存储可用大小
        deviceInfo.availableInternalStorage = DeviceUtils.getAvailableInternalMemorySize();
        //外部存储总大小
        deviceInfo.totalExternalStorage = DeviceUtils.getTotalExternalMemorySize();
        //外部存储可用大小
        deviceInfo.availableInternalStorage = DeviceUtils.getAvailableExternalMemorySize();
        //是否root
        deviceInfo.root = String.valueOf(DeviceUtils.isRooted());
        //电池信息  sticky broadcast
        IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryIntent = mContext.registerReceiver(null, batteryFilter);
        if (batteryIntent != null) {
            int batterStatus = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
            switch (batterStatus) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    deviceInfo.batteryStatus = mContext.getString(R.string.BATTERY_STATUS_CHARGING);
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    deviceInfo.batteryStatus = mContext.getString(R.string.BATTERY_STATUS_DISCHARGING);
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    deviceInfo.batteryStatus = mContext.getString(R.string.BATTERY_STATUS_FULL);
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    deviceInfo.batteryStatus = mContext.getString(R.string.BATTERY_STATUS_NOT_CHARGING);
                    break;
                default:
                    deviceInfo.batteryStatus = mContext.getString(R.string.BATTERY_STATUS_UNKNOWN);
            }
            deviceInfo.batteryTemp = Integer.toString(batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1));
            deviceInfo.batteryLevel = Integer.toString(batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1));
        }

    }

    /**
     * 需要申请一些权限
     *
     * @param deviceInfo
     */
    @SuppressLint("DefaultLocale")
    private void analyzeMedium(@NonNull DeviceInfo deviceInfo) {
        //android.permission.READ_PHONE_STATE
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            deviceInfo.phoneNumber = tm.getLine1Number();
            int phoneType = tm.getPhoneType();
            switch (phoneType) {
                case TelephonyManager.PHONE_TYPE_GSM:
                    deviceInfo.phoneType = mContext.getString(R.string.PHONE_TYPE_GSM);
                    break;
                case TelephonyManager.PHONE_TYPE_CDMA:
                    deviceInfo.phoneType = mContext.getString(R.string.PHONE_TYPE_CDMA);
                    break;
                case TelephonyManager.PHONE_TYPE_SIP:
                    deviceInfo.phoneType = mContext.getString(R.string.PHONE_TYPE_SIP);
                    break;
                default:
                    deviceInfo.phoneType = mContext.getString(R.string.PHONE_TYPE_NONE);
            }
            int radioType = tm.getNetworkType();
            switch (radioType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_GPRS);
                    break;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_EDGE);
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_UMTS);
                    break;
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_CDMA);
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_EVDO_0);
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_EVDO_A);
                    break;
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_1xRTT);
                    break;
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_HSDPA);
                    break;
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_HSUPA);
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_HSPA);
                    break;
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_IDEN);
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_EVDO_B);
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_LTE);
                    break;
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_EHRPD);
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_HSPAP);
                    break;
                case 16:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_GSM);
                    break;
                default:
                    deviceInfo.radioType = mContext.getString(R.string.NETWORK_TYPE_NONE);
            }

            deviceInfo.imei = tm.getDeviceId();
            deviceInfo.imsi = tm.getSubscriberId();
            deviceInfo.simSerial = tm.getSimSerialNumber();
            deviceInfo.voiceMail = tm.getVoiceMailNumber();
            deviceInfo.simCountryIso = tm.getSimCountryIso();
            deviceInfo.networkCountryIso = tm.getSimCountryIso();
            deviceInfo.carrier = tm.getNetworkOperatorName();
            deviceInfo.simCarrier = tm.getSimOperatorName();
            if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
                deviceInfo.simOperator = tm.getSimOperator();
                String networkOperator = tm.getNetworkOperator();
                if (!TextUtils.isEmpty(networkOperator)) {
                    deviceInfo.mcc = networkOperator.substring(0, 3);
                    deviceInfo.mnc = networkOperator.substring(3);
                }
            }
            try {
                AdvertisingIdClient.AdInfo adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
                deviceInfo.advertisingId = adInfo.getId();
            } catch (Exception e) {
                //nope
            }
        }
        //android.permission.ACCESS_COARSE_LOCATION or android.permission.ACCESS_FINE_LOCATION
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (lastKnownLocation != null) {
                    deviceInfo.cellLocation = String.format("[%.6f,%.6f]", lastKnownLocation.getLongitude(), lastKnownLocation.getLatitude());
                }
            }else {
                Log.e(TAG,"network location permission denied");
            }
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Location lastKnownGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastKnownGpsLocation != null) {
                        deviceInfo.gpsLocation = String.format("[%.6f,%.6f]", lastKnownGpsLocation.getLongitude(), lastKnownGpsLocation.getLatitude());
                    }
                }
            }else {
                Log.e(TAG,"gps location permission denied");
            }
            List<DeviceInfo.CellInfo> allCellInfo = new ArrayList<>();
            List<NeighboringCellInfo> cellInfos = tm.getNeighboringCellInfo();
            for (NeighboringCellInfo cellInfo : cellInfos) {
                DeviceInfo.CellInfo info = new DeviceInfo.CellInfo();
                info.rssi = cellInfo.getRssi();
                info.cid = cellInfo.getCid();
                info.psc = cellInfo.getPsc();
                info.lac = cellInfo.getLac();
                allCellInfo.add(info);
            }
        }

        if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){
            //wifi mac address
            deviceInfo.wifiMac = DeviceUtils.getWifiMacAddress();
            //cell ip
            deviceInfo.cellIp = DeviceUtils.getLocalIpAddress();
        }

        //android.permission.ACCESS_NETWORK_STATE
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            deviceInfo.networkType = networkInfo == null?"":Integer.toString(networkInfo.getType());
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                //android.permission.ACCESS_WIFI_STATE
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
                    DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
                    if (dhcpInfo != null) {
                        deviceInfo.gateway = CommonUtils.formatIpInt(dhcpInfo.gateway);
                        deviceInfo.wifiNetmask = CommonUtils.formatIpInt(dhcpInfo.netmask);
                        deviceInfo.dhcpServer = CommonUtils.formatIpInt(dhcpInfo.serverAddress);
                        deviceInfo.dnsAddress = CommonUtils.formatIpInt(dhcpInfo.dns1);
                    }
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        deviceInfo.ssid = wifiInfo.getSSID();
                        deviceInfo.wifiIp = CommonUtils.formatIpInt(wifiInfo.getIpAddress());
                        deviceInfo.bssid = wifiInfo.getBSSID();
                    }
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        deviceInfo.proxyInfo = Proxy.getHost(mContext) + ":" + Proxy.getPort(mContext);
                    } else {
                        deviceInfo.proxyInfo = System.getProperty("http.proxyHost") + ":" + System.getProperty("http.proxyPort");
                    }

                    List<ScanResult> scanResults = wifiManager.getScanResults();
                    for (ScanResult scanResult : scanResults){
                        Log.w(TAG,"wifi scan: "+scanResult.SSID);
                    }
                    List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();
                    for (WifiConfiguration configuration:wifiConfigurations){
                        Log.w(TAG,"wifi config: "+configuration.SSID);
                    }
                }
            }
            if(networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_VPN){
                Log.w(TAG,"vpn");
            }
        }else {
            Log.e(TAG,"internet permission denied");
        }


    }

    private void analyzeHigh(@NonNull DeviceInfo deviceInfo) {

    }

    private void analyzeMax(@NonNull DeviceInfo deviceInfo) {
        //nope
    }


    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


}
