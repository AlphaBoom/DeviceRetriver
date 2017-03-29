package com.anarchy.deviceretriever.data.source.retriever;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.anarchy.deviceretriever.data.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/20 10:37
 * <p/>
 */
public class TelephoneRetriever extends BasePermissionRetriever {

    public TelephoneRetriever(@NonNull Context context){
        super(context);
    }

    @Override
    public String[] checkUnGrantedPermission() {
        if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            return new String[]{Manifest.permission.READ_PHONE_STATE};
        }
        return new String[0];
    }


    @Override
    List<Info> doRetrieve(boolean ignorePermission) {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        //phone type
        int phoneType = tm.getPhoneType();
        String phoneTypeName = "phone type";
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_GSM:
                mResult.add(new Info(phoneTypeName,"GSM"));
                break;
            case TelephonyManager.PHONE_TYPE_CDMA:
                mResult.add(new Info(phoneTypeName,"CDMA"));
                break;
            case TelephonyManager.PHONE_TYPE_SIP:
                mResult.add(new Info(phoneTypeName,"SIP"));
                break;
            default:
                mResult.add(new Info(phoneTypeName,"NONE"));
        }
        //radio type
        int radioType = tm.getNetworkType();
        String radioTypeName = "radio type";
        String radioTypeDesc = "移动网络制式";
        switch (radioType){
            case TelephonyManager.NETWORK_TYPE_GPRS:
                mResult.add(new Info(radioTypeName,"GPRS",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                mResult.add(new Info(radioTypeName,"EDGE",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                mResult.add(new Info(radioTypeName,"UMTS",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                mResult.add(new Info(radioTypeName,"CDMA",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                mResult.add(new Info(radioTypeName,"EVDO_0",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                mResult.add(new Info(radioTypeName,"EVDO_A",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                mResult.add(new Info(radioTypeName,"1xRTT",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                mResult.add(new Info(radioTypeName,"HSDPA",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                mResult.add(new Info(radioTypeName,"HSUPA",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                mResult.add(new Info(radioTypeName,"HSPA",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                mResult.add(new Info(radioTypeName,"IDEN",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                mResult.add(new Info(radioTypeName,"EVDO_B",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                mResult.add(new Info(radioTypeName,"LTE",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                mResult.add(new Info(radioTypeName,"EHRPD",radioTypeDesc));
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                mResult.add(new Info(radioTypeName,"HSPAP",radioTypeDesc));
                break;
            case 16:
                mResult.add(new Info(radioTypeName,"GSM",radioTypeDesc));
                break;
            default:
                mResult.add(new Info(radioTypeName,"NONE",radioTypeDesc));
        }
        //mcc and mnc
        String networkOperator = tm.getNetworkOperator();
        if(!TextUtils.isEmpty(networkOperator)) {
            mResult.add(new Info("mcc",networkOperator.substring(0,3)));
            mResult.add(new Info("mnc",networkOperator.substring(3)));
        }
        //sim country iso
        mResult.add(new Info("sim country iso",tm.getSimCountryIso(),"国家代码"));
        //network country iso
        mResult.add(new Info("network country iso",tm.getNetworkCountryIso(),"移动网络国家代码"));
        //carrier
        mResult.add(new Info("network operator name",tm.getNetworkOperatorName(),"移动网络提供商"));
        //require sim state ready
        if(tm.getSimState() == TelephonyManager.SIM_STATE_READY){
            //sim operator
            mResult.add(new Info("sim operator",tm.getSimOperator(),"SIM卡运营商"));
            //sim operator name
            mResult.add(new Info("sim operator name",tm.getSimOperatorName(),"SIM卡运营商名称"));
        }
        if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            //phone number
            mResult.add(new Info("phone number",tm.getLine1Number(),"电话号码 一般无法获取到有效信息"));
            //imei
            mResult.add(new Info("imei",tm.getDeviceId()));
            //imsi
            mResult.add(new Info("imsi",tm.getSubscriberId()));
            //sim serial number
            mResult.add(new Info("sim serial",tm.getSimSerialNumber(),"SIM卡序列号"));
            //voice mail number
            mResult.add(new Info("voice mail",tm.getVoiceMailNumber(),"语音信箱号码"));
        }else if(!ignorePermission){
            // if not ignore drop all info
            return null;
        }
        return mResult;
    }

}
