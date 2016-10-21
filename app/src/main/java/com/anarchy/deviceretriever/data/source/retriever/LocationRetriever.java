package com.anarchy.deviceretriever.data.source.retriever;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.anarchy.deviceretriever.data.Info;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/21 16:13
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */

public class LocationRetriever extends BasePermissionRetriever {
    private static final String TAG = LocationRetriever.class.getSimpleName();

    public LocationRetriever(Context context) {
        super(context);
    }

    @Override
    public String[] checkUnGrantedPermission() {
        String[] unGrantedPermission = new String[0];
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            unGrantedPermission = addPermission(unGrantedPermission,Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            unGrantedPermission = addPermission(unGrantedPermission,Manifest.permission.ACCESS_FINE_LOCATION);
        }
        return unGrantedPermission;
    }


    @SuppressLint("DefaultLocale")
    @Override
    public List<Info> retrieve(boolean ignorePermission) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Location gpsLocation = null;
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(gpsLocation != null) {
                    mResult.add(new Info("gps location", String.format("[%.6f,%.6f,%f]",
                            gpsLocation.getLatitude(), gpsLocation.getLongitude(),gpsLocation.getAltitude())));
                    injectAddress(gpsLocation.getLatitude(),gpsLocation.getLongitude(),1);
                }
            }
            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                Location cellLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(cellLocation != null){
                    mResult.add(new Info("cell location",String.format("[%.6f,%.6f,%f]",
                            cellLocation.getLatitude(),cellLocation.getLongitude(),cellLocation.getAltitude())));
                    if(gpsLocation == null){
                        injectAddress(cellLocation.getLatitude(),cellLocation.getLongitude(),1);
                    }
                }
            }
        }else if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                Location cellLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(cellLocation != null){
                    mResult.add(new Info("cell location",String.format("[%.6f,%.6f,%f]",
                            cellLocation.getLatitude(),cellLocation.getLongitude(),cellLocation.getAltitude())));
                    injectAddress(cellLocation.getLatitude(),cellLocation.getLongitude(),1);
                }
            }
        }else{
            Log.w(TAG,"location permission denied");
            if(!ignorePermission) return null;
        }
        return mResult;
    }


    private void injectAddress(double latitude,double longitude,int maxResult){
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude,longitude,maxResult);
            if(addressList.size() > 0){
                Address address = addressList.get(0);
                //country code
                mResult.add(new Info("countryId",String.valueOf(address.getCountryCode())));
                mResult.add(new Info("country",address.getCountryName()));
                mResult.add(new Info("latitude",String.format("%.4f",address.getLatitude())));
                mResult.add(new Info("longitude",String.format("%.4f",address.getLongitude())));
                mResult.add(new Info("address",address.toString(),"地址信息"));
            }else {
                Log.w(TAG,"no parsed address by geocoder");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"parse location information io exception");
        }
    }

}
