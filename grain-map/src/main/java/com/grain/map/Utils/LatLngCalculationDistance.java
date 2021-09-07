package com.grain.map.Utils;

import com.grain.map.Entity.LatLng;
import com.grain.utils.hint.L;

import java.util.List;

/**
 * @anthor GrainRain
 * @funcation 计算坐标距离工具
 * @date 2019/11/27
 */
public class LatLngCalculationDistance {

    /**
     * 计算坐标间的距离
     * @param latLng1
     * @param latLng2
     * @return
     */
    public static float calculationCoordinateDistance(LatLng latLng1, LatLng latLng2) {
        return calculateLineDistance(latLng1, latLng2);
    }

    /**
     * 计算坐标间的距离
     * @param latLngs
     * @return
     */
    public static float calculationCoordinateDistance(List<LatLng> latLngs) {
        float CoordinateDistance = 0;
        try {
            if(latLngs != null) {
                for(int i = 0; i < latLngs.size()-1; i++) {
                    CoordinateDistance += calculationCoordinateDistance(latLngs.get(i), latLngs.get(i+1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CoordinateDistance;
    }

    // TODO: 2021/9/7 需要测试算法是否正常
    public static float calculateLineDistance(LatLng var0, LatLng var1) {
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
}
