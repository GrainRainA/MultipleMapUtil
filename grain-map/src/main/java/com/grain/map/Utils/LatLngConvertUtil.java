package com.grain.map.Utils;

import com.grain.map.Entity.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * @anthor GrainRain
 * @funcation 坐标转换工具类
 * @date 2019/11/27
 */
public class LatLngConvertUtil {

    private static double PI = Math.PI;
    private static double AXIS = 6378245.0;
    private static double OFFSET = 0.00669342162296594323; // (a^2 - b^2) / a^2

    /**
     * WGS84 转 GCJ02
     * @param latitude WGS84 纬度
     * @param longitude WGS84 经度
     * @return GCJ02 火星坐标
     */
    public static LatLng WGS84ToGCJ02(double latitude, double longitude) {
        return WGS84ToGCJ02(new LatLng(latitude, longitude));
    }

    /**
     * WGS84 转 GCJ02
     * @param wgsLatLng WGS84坐标
     * @return GCJ02 火星坐标
     */
    public static LatLng WGS84ToGCJ02(LatLng wgsLatLng) {

        double wgsLat = wgsLatLng.getLatitude();
        double wgsLon = wgsLatLng.getLongitude();
        LatLng gcjLatLng = new LatLng();

        if (outOfChina(wgsLat, wgsLon)) {
            gcjLatLng.setLatitude(wgsLat);
            gcjLatLng.setLongitude(wgsLon);
            return gcjLatLng;
        }

        double[] deltaD = delta(wgsLat, wgsLon);
        gcjLatLng.setLatitude(wgsLat+ deltaD[0]);
        gcjLatLng.setLongitude(wgsLon + deltaD[1]);

        return gcjLatLng;
    }

    /**
     * WGS84 转 GCJ02
     * @param wgsLatLngList WGS84 坐标列表
     * @return GCJ02 火星坐标列表
     */
    public static List<LatLng> WGS84ToGCJ02(List<LatLng> wgsLatLngList) {

        List<LatLng> gcjLatLngList = new ArrayList<>();
        for(int i = 0; i < wgsLatLngList.size(); i++) {
            gcjLatLngList.add(WGS84ToGCJ02(wgsLatLngList.get(i)));
        }
        return gcjLatLngList;
    }

    /**
     * GCJ02 转 WGS84
     * @param latitude GCJ02 火星坐标纬度
     * @param longitude GCJ02 火星坐标经度
     * @return WGS84坐标
     */
    public static LatLng GCJ02ToWGS84(double latitude, double longitude) {
        return GCJ02ToWGS84(new LatLng(latitude, longitude));
    }

    /**
     * GCJ02 转 WGS84
     * @param gcjLatLng GCJ02 火星坐标
     * @return WGS84 坐标
     */
    public static LatLng GCJ02ToWGS84(LatLng gcjLatLng) {
        double gcjLat = gcjLatLng.getLatitude();
        double gcjLon = gcjLatLng.getLongitude();
        double initDelta = 0.01;
        double threshold = 0.000000001;
        double dLat = initDelta, dLon = initDelta;
        double mLat = gcjLat - dLat, mLon = gcjLon - dLon;
        double pLat = gcjLat + dLat, pLon = gcjLon + dLon;
        double wgsLat, wgsLon, i = 0;
        while (true) {
            wgsLat = (mLat + pLat) / 2;
            wgsLon = (mLon + pLon) / 2;
            double[] tmp = wgs2GCJ(wgsLat, wgsLon);
            dLat = tmp[0] - gcjLat;
            dLon = tmp[1] - gcjLon;
            if ((Math.abs(dLat) < threshold) && (Math.abs(dLon) < threshold))
                break;
            if (dLat > 0)
                pLat = wgsLat;
            else
                mLat = wgsLat;
            if (dLon > 0)
                pLon = wgsLon;
            else
                mLon = wgsLon;
            if (++i > 10000)break;
        }
        double[] latlon = new double[2];
        latlon[0] = wgsLat;
        latlon[1] = wgsLon;
        return new LatLng(wgsLat, wgsLon);
    }

    /**
     * GCJ02 转 WGS84
     * @param gcjLatLngList GCJ02 火星坐标列表
     * @return WGS84 坐标
     */
    public static List<LatLng> GCJ02ToWGS84(List<LatLng> gcjLatLngList) {

        List<LatLng> wgsLatLngList = new ArrayList<>();
        try {
            for(int i = 0; i < gcjLatLngList.size(); i++) {
                wgsLatLngList.add(GCJ02ToWGS84(gcjLatLngList.get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wgsLatLngList;
    }










    // WGS84 转 GCJ02
    public static double[] wgs2GCJ(double wgLat, double wgLon) {
        double[] latlon = new double[2];
        if (outOfChina(wgLat, wgLon)) {
            latlon[0] = wgLat;
            latlon[1] = wgLon;
            return latlon;
        }
        double[] deltaD = delta(wgLat, wgLon);
        latlon[0] = wgLat + deltaD[0];
        latlon[1] = wgLon + deltaD[1];
        return latlon;
    }

    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    public static double[] delta(double wgLat, double wgLon) {
        double[] latlng = new double[2];
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - OFFSET * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0)/ ((AXIS * (1 - OFFSET)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (AXIS / sqrtMagic * Math.cos(radLat) * PI);
        latlng[0] = dLat;latlng[1] = dLon;
        return latlng;
    }

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y+ 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1* Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0* PI)) * 2.0 / 3.0;
        return ret;
    }
}
