package com.grain.map.Utils.BaiduMap;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * @anthor GrainRain
 * @funcation 百度地图坐标转换工具
 * @date 2020/5/13
 */
public class BaiduMapLatLngConverUtil {

    /**
     * LatLng转BaiduMapLatLng
     * @param myLatLng
     * @return
     */
    public static LatLng baiduMapLatLngToLatLng(com.grain.map.Entity.LatLng myLatLng) {

        LatLng latLng = null;
        if (myLatLng != null) {
            latLng = new LatLng(myLatLng.getLatitude(), myLatLng.getLongitude());
        }
        return latLng;
    }

    /**
     * LatLng转BaiduMapLatLng
     * @param myLatLngList
     * @return
     */
    public static List<LatLng> baiduMapLatLngToLatLng(List<com.grain.map.Entity.LatLng> myLatLngList) {

        List<LatLng> latLngList = new ArrayList<>();
        if(myLatLngList != null) {
            for (com.grain.map.Entity.LatLng latLng : myLatLngList) {
                latLngList.add(baiduMapLatLngToLatLng(latLng));
            }
        }
        return latLngList;
    }
}