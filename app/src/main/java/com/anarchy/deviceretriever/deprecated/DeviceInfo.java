package com.anarchy.deviceretriever.deprecated;


import java.util.List;

/**
 * Version 2.1.1
 * <p>
 * Date: 16/10/14 15:00
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */
public class DeviceInfo {
    private static final String ANDROID = "Android";
    public String os = ANDROID;
    public String releaseVersion;//Android 系统版本
    public int releaseVersionInt;//Android 系统版本
    public String model;//设备型号
    public String product;
    public String brand;
    public String serialNo;
    public String display;
    public String host;
    public String deviceName;
    public String hardware;
    public GeoLp geoLp;
    public String tags;
    public String advertisingId;
    public String imsi;
    public String phoneNumber;
    public String imei;
    public String voiceMail;
    public String simSerial;
    public String simCountryIso;
    public String networkCountryIso;
    public String carrier;
    public String simCarrier;
    public String mnc;
    public String mcc;
    public String simOperator;
    public String phoneType;
    public String radioType;
    public String cellLocation;
    public List<CellInfo> allCellInfo;
    public String deviceSVN;
    public String wifiIp;
    public String dhcpServer;
    public String wifiMac;
    public String ssid;
    public String bssid;
    public String gateway;
    public String wifiNetmask;
    public String proxyInfo;
    public String dnsAddress;
    public String vpnIp;
    public String vpnNetmask;
    public String cellIp;
    public String networkType;
    public String currentTime;
    public String upTime;
    public String bootTime;
    public String activeTime;
    public String root;
    public String packageName;
    public String apkVersion;
    public String uid;
    public String signMD5;
    public String apkMD5;
    public String timeZone;
    public String language;
    public String brightness;
    public String batteryStatus;
    public String batteryLevel;
    public String batteryTemp;
    public String screenRes;
    public String fontHash;
    public String blueMac;
    public String androidId;
    public String cpuFrequency;
    public String cpuHardware;
    public String cpuType;
    public String totalMemory;
    public String availableMemory;
    public String totalInternalStorage;//内部存储总大小
    public String availableInternalStorage;//内部存储可用大小
    public String totalExternalStorage;//外部存储总大小
    public String availableExternalStorage;//外部存储可用大小
    public String basebandVersion;
    public String kernelVersion;
    public String gpsLocation;
    public String allowMockLocation;
    public String maxHeapSize;//最大堆栈大小
    public String heapAllocatePercent;//堆栈已分配比例
    public String heapUsedPercent;//已经使用的堆栈占比
    public boolean isEmulator;
    public long initTime;//采集数据消耗时间


    public static class GeoLp {
        public String provinceId;
        public String countryId;
        public String desc;
        public String lip;
        public String cityId;
        public String isp;
        public String extra1;
        public String extra2;
        public String type;
        public String areaId;
        public String ip;
        public String city;
        public String country;
        public String area;
        public String ispId;
        public String address;
        public String county;
        public String province;
        public String longitude;
        public String latitude;
        public String countyId;

        @Override
        public String toString() {
            return "GeoLp{" +
                    "provinceId='" + provinceId + '\'' +
                    ", countryId='" + countryId + '\'' +
                    ", desc='" + desc + '\'' +
                    ", lip='" + lip + '\'' +
                    ", cityId='" + cityId + '\'' +
                    ", isp='" + isp + '\'' +
                    ", extra1='" + extra1 + '\'' +
                    ", extra2='" + extra2 + '\'' +
                    ", type='" + type + '\'' +
                    ", areaId='" + areaId + '\'' +
                    ", ip='" + ip + '\'' +
                    ", city='" + city + '\'' +
                    ", country='" + country + '\'' +
                    ", area='" + area + '\'' +
                    ", ispId='" + ispId + '\'' +
                    ", address='" + address + '\'' +
                    ", county='" + county + '\'' +
                    ", province='" + province + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", countyId='" + countyId + '\'' +
                    '}';
        }
    }

    public static class CellInfo{
        public int rssi;
        public int cid;
        public int lac;
        public int psc;

        @Override
        public String toString() {
            return "CellInfo{" +
                    "rssi=" + rssi +
                    ", cid=" + cid +
                    ", lac=" + lac +
                    ", psc=" + psc +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "os='" + os + '\'' +
                ", releaseVersion='" + releaseVersion + '\'' +
                ", releaseVersionInt=" + releaseVersionInt +
                ", model='" + model + '\'' +
                ", product='" + product + '\'' +
                ", brand='" + brand + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", display='" + display + '\'' +
                ", host='" + host + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", hardware='" + hardware + '\'' +
                ", geoLp=" + geoLp +
                ", tags='" + tags + '\'' +
                ", advertisingId='" + advertisingId + '\'' +
                ", imsi='" + imsi + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", imei='" + imei + '\'' +
                ", voiceMail='" + voiceMail + '\'' +
                ", simSerial='" + simSerial + '\'' +
                ", simCountryIso='" + simCountryIso + '\'' +
                ", networkCountryIso='" + networkCountryIso + '\'' +
                ", carrier='" + carrier + '\'' +
                ", simCarrier='" + simCarrier + '\'' +
                ", mnc='" + mnc + '\'' +
                ", mcc='" + mcc + '\'' +
                ", simOperator='" + simOperator + '\'' +
                ", phoneType='" + phoneType + '\'' +
                ", radioType='" + radioType + '\'' +
                ", cellLocation='" + cellLocation + '\'' +
                ", allCellInfo=" + allCellInfo +
                ", deviceSVN='" + deviceSVN + '\'' +
                ", wifiIp='" + wifiIp + '\'' +
                ", dhcpServer='" + dhcpServer + '\'' +
                ", wifiMac='" + wifiMac + '\'' +
                ", ssid='" + ssid + '\'' +
                ", bssid='" + bssid + '\'' +
                ", gateway='" + gateway + '\'' +
                ", wifiNetmask='" + wifiNetmask + '\'' +
                ", proxyInfo='" + proxyInfo + '\'' +
                ", dnsAddress='" + dnsAddress + '\'' +
                ", vpnIp='" + vpnIp + '\'' +
                ", vpnNetmask='" + vpnNetmask + '\'' +
                ", cellIp='" + cellIp + '\'' +
                ", networkType='" + networkType + '\'' +
                ", currentTime='" + currentTime + '\'' +
                ", upTime='" + upTime + '\'' +
                ", bootTime='" + bootTime + '\'' +
                ", activeTime='" + activeTime + '\'' +
                ", root='" + root + '\'' +
                ", packageName='" + packageName + '\'' +
                ", apkVersion='" + apkVersion + '\'' +
                ", uid='" + uid + '\'' +
                ", signMD5='" + signMD5 + '\'' +
                ", apkMD5='" + apkMD5 + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", language='" + language + '\'' +
                ", brightness='" + brightness + '\'' +
                ", batteryStatus='" + batteryStatus + '\'' +
                ", batteryLevel='" + batteryLevel + '\'' +
                ", batteryTemp='" + batteryTemp + '\'' +
                ", screenRes='" + screenRes + '\'' +
                ", fontHash='" + fontHash + '\'' +
                ", blueMac='" + blueMac + '\'' +
                ", androidId='" + androidId + '\'' +
                ", cpuFrequency='" + cpuFrequency + '\'' +
                ", cpuHardware='" + cpuHardware + '\'' +
                ", cpuType='" + cpuType + '\'' +
                ", totalMemory='" + totalMemory + '\'' +
                ", availableMemory='" + availableMemory + '\'' +
                ", totalInternalStorage='" + totalInternalStorage + '\'' +
                ", availableInternalStorage='" + availableInternalStorage + '\'' +
                ", totalExternalStorage='" + totalExternalStorage + '\'' +
                ", availableExternalStorage='" + availableExternalStorage + '\'' +
                ", basebandVersion='" + basebandVersion + '\'' +
                ", kernelVersion='" + kernelVersion + '\'' +
                ", gpsLocation='" + gpsLocation + '\'' +
                ", allowMockLocation='" + allowMockLocation + '\'' +
                ", maxHeapSize='" + maxHeapSize + '\'' +
                ", heapAllocatePercent='" + heapAllocatePercent + '\'' +
                ", heapUsedPercent='" + heapUsedPercent + '\'' +
                ", isEmulator=" + isEmulator +
                ", initTime=" + initTime +
                '}';
    }
}
