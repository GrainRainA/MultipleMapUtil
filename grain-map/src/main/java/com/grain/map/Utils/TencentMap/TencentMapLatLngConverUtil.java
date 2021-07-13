package com.grain.map.Utils.TencentMap;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * @anthor GrainRain
 * @funcation 腾讯地图坐标转换工具
 * @date 2020/5/13
 */
public class TencentMapLatLngConverUtil {

    /**
     * LatLng转TencentMapLatLng
     * @param myLatLng
     * @return
     */
    public static LatLng tencentMapLatLngToLatLng(com.grain.map.Entity.LatLng myLatLng) {
        LatLng latLng = null;

        if(myLatLng != null) {
            latLng = new LatLng(myLatLng.getLatitude(), myLatLng.getLongitude());
        }
        return latLng;
    }

    /**
     * LatLng转TencentMapLatLng
     * @param myLatLngList
     * @return
     */
    public static List<LatLng> tencentMapLatLngToLatLng(List<com.grain.map.Entity.LatLng> myLatLngList) {
        List<LatLng> latLngList = new ArrayList<>();

        if(myLatLngList != null) {
            for (com.grain.map.Entity.LatLng latLng : myLatLngList) {
                latLngList.add(tencentMapLatLngToLatLng(latLng));
            }
        }
        return latLngList;
    }
}
