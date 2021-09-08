package com.grain.map.Utils;

import android.util.Log;

import com.amap.api.maps.AMapUtils;
import com.baidu.mapapi.utils.DistanceUtil;
import com.grain.map.Entity.LatLng;
import com.grain.map.MapView;
import com.grain.utils.hint.L;
import com.tencent.map.geolocation.TencentLocationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @anthor GrainRain
 * @funcation 计算坐标距离工具
 *
 * @date 2019/11/27
 */
public class LatLngCalculationDistance {

    //计算坐标距离使用的工具源
    private static int CALCULATION_DISTANCE_TYPE = MapView.MAP_SOURCE_AMAP;

    /**
     * 计算坐标间的距离
     * @param latLngs
     * @return
     */
    public static float getDistance(LatLng... latLngs) {
        List<LatLng> latLngList = new ArrayList<>();

        if (latLngs != null) latLngList.addAll(Arrays.asList(latLngs));
        if (latLngList.size() < 2) return 0;

        return getDistance(latLngList);
    }

    /**
     * 计算坐标间的距离
     * @param latLngs
     * @return
     */
    public static float getDistance(List<LatLng> latLngs) {
        float coordinateDistance = 0;
        try {
            if(latLngs != null) {
                for(int i = 0; i < latLngs.size()-1; i++) {
                    coordinateDistance += getDistance(latLngs.get(i), latLngs.get(i+1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordinateDistance;
    }


    //计算坐标间的距离
    private static float getDistance(LatLng latLng1, LatLng latLng2) {

        switch (CALCULATION_DISTANCE_TYPE) {
            case MapView.MAP_SOURCE_BAIDU:
                return getBaiduMapDistance(latLng1, latLng2);
            case MapView.MAP_SOURCE_TENCENT:
                return getTencentMapDistance(latLng1, latLng2);
            default:
                return calculateLineDistance(latLng1, latLng2);
        }
    }

    /**
     * 使用高德地图工具计算坐标间的距离
     * @param latLng1
     * @param latLng2
     * @return
     */
    public static float getAMapDistance(LatLng latLng1, LatLng latLng2) {

        float amapDistance = AMapUtils.calculateLineDistance(
                new com.amap.api.maps.model.LatLng(latLng1.latitude, latLng1.longitude),
                new com.amap.api.maps.model.LatLng(latLng2.latitude, latLng2.longitude));
        return amapDistance;
    }

    /**
     * 使用百度地图工具计算坐标间的距离
     * @param latLng1
     * @param latLng2
     * @return
     */
    public static float getBaiduMapDistance(LatLng latLng1, LatLng latLng2) {

        double baiduDistance = DistanceUtil.getDistance(
                new com.baidu.mapapi.model.LatLng(latLng1.latitude, latLng1.longitude),
                new com.baidu.mapapi.model.LatLng(latLng2.latitude, latLng2.longitude));
        return (float) baiduDistance;
    }

    /**
     * 使用百度地图工具计算坐标间的距离
     * @param latLng1
     * @param latLng2
     * @return
     */
    public static float getTencentMapDistance(LatLng latLng1, LatLng latLng2) {

        double tencentDistance = TencentLocationUtils.distanceBetween(
                latLng1.latitude, latLng1.longitude,
                latLng2.latitude, latLng2.longitude);
        return (float) tencentDistance;
    }


    private static float calculateLineDistance(LatLng var0, LatLng var1) {
        if (var0.longitude == 0 || var0.latitude == 0 || var1.latitude == 0 || var1.longitude == 0) {
            return 0;
        }

        if (var0 != null && var1 != null) {
            double var2 = 0.01745329251994329D;
            double var4 = var0.getLongitude();
            double var6 = var0.getLatitude();
            double var8 = var1.getLongitude();
            double var10 = var1.getLatitude();
            var4 *= 0.01745329251994329D;
            var6 *= 0.01745329251994329D;
            var8 *= 0.01745329251994329D;
            var10 *= 0.01745329251994329D;
            double var12 = Math.sin(var4);
            double var14 = Math.sin(var6);
            double var16 = Math.cos(var4);
            double var18 = Math.cos(var6);
            double var20 = Math.sin(var8);
            double var22 = Math.sin(var10);
            double var24 = Math.cos(var8);
            double var26 = Math.cos(var10);
            double[] var28 = new double[3];
            double[] var29 = new double[3];
            var28[0] = var18 * var16;
            var28[1] = var18 * var12;
            var28[2] = var14;
            var29[0] = var26 * var24;
            var29[1] = var26 * var20;
            var29[2] = var22;
            double var30 = Math.sqrt((var28[0] - var29[0]) * (var28[0] - var29[0]) + (var28[1] - var29[1]) * (var28[1] - var29[1]) + (var28[2] - var29[2]) * (var28[2] - var29[2]));
            return (float)(Math.asin(var30 / 2.0D) * 1.27420015798544E7D);
        } else {
            L.e("坐标转换失败 非法坐标值");
            return 0.0F;
        }
    }

    public static void setCalculationDistanceType(int calculationDistanceType) {
        CALCULATION_DISTANCE_TYPE = calculationDistanceType;
    }
}
