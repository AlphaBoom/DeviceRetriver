package com.anarchy.deviceretriever.data.source.retriever;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.anarchy.deviceretriever.data.Info;
import com.anarchy.deviceretriever.data.source.retriever.utils.CommonUtils;
import com.anarchy.deviceretriever.data.source.retriever.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/21 15:10
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public class NetworkRetriever extends BasePermissionRetriever {
    private final static String TAG = NetworkRetriever.class.getSimpleName();

    public NetworkRetriever(Context context) {
        super(context);
    }


    @Override
    public String[] checkUnGrantedPermission() {
        String[] unGrantedPermission = new String[0];
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            unGrantedPermission = addPermission(unGrantedPermission,Manifest.permission.INTERNET);
        }
        if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED){
            unGrantedPermission = addPermission(unGrantedPermission,Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED){
            unGrantedPermission = addPermission(unGrantedPermission,Manifest.permission.ACCESS_WIFI_STATE);
        }
        return unGrantedPermission;
    }



    @Override
    public List<Info> retrieve(boolean ignorePermission) {
        //proxy
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mResult.add(new Info("proxy",Proxy.getHost(mContext) + ":" + Proxy.getPort(mContext)));
        } else {
            mResult.add(new Info("proxy",System.getProperty("http.proxyHost") + ":" + System.getProperty("http.proxyPort")));
        }
        if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){
            mResult.add(new Info("wifi mac", DeviceUtils.getWifiMacAddress()));
            mResult.add(new Info("cell ip",DeviceUtils.getLocalIpAddress()));
        }else {
            Log.w(TAG,"android.permission.INTERNET  denied");
            if(!ignorePermission) return null;
        }
        if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED){
            WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            if(dhcpInfo != null){
                mResult.add(new Info("gateway", CommonUtils.formatIpInt(dhcpInfo.gateway),"网关"));
                mResult.add(new Info("wifi netmask",CommonUtils.formatIpInt(dhcpInfo.netmask),"子网掩码"));
                mResult.add(new Info("dhcp server",CommonUtils.formatIpInt(dhcpInfo.serverAddress),"dhcp 服务器"));
                mResult.add(new Info("dns address",CommonUtils.formatIpInt(dhcpInfo.dns1),"dns 地址"));
            }else {
                Log.w(TAG,"getDhcpInfo return null");
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if(wifiInfo != null){
                mResult.add(new Info("ssid",wifiInfo.getSSID()));
                mResult.add(new Info("wifiIp",CommonUtils.formatIpInt(wifiInfo.getIpAddress())));
                mResult.add(new Info("bssid",wifiInfo.getBSSID()));
                mResult.add(new Info("rssi",String.valueOf(wifiInfo.getRssi())));
            }else {
                Log.w(TAG,"no wifi connect");
            }
            List<ScanResult> scanResults = wifiManager.getScanResults();
            if(scanResults != null) {
                List<String> scanSSIDs = new ArrayList<>();
                for (ScanResult scanResult : scanResults) {
                    scanSSIDs.add(scanResult.SSID);
                }
                mResult.add(new Info("scan ssid",Arrays.toString(scanSSIDs.toArray())));
            }
            List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();
            if(wifiConfigurations != null){
                List<String> configurationSSIDs = new ArrayList<>();
                for(WifiConfiguration wifiConfiguration :wifiConfigurations){
                    configurationSSIDs.add(wifiConfiguration.SSID);
                }
                mResult.add(new Info("configuration ssid",Arrays.toString(configurationSSIDs.toArray())));
            }

        }else{
            Log.w(TAG,"android.permission.ACCESS_WIFI_STATE denied");
            if(!ignorePermission) return null;
        }
        if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED){
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null) {
                mResult.add(new Info("network type", String.valueOf(networkInfo.getType())));
            }
        }else {
            Log.w(TAG,"android.permission.ACCESS_NETWORK_STATE denied");
            if(!ignorePermission) return null;
        }
        return null;
    }
}
