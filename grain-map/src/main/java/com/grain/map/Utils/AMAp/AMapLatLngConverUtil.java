package com.grain.map.Utils.AMAp;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * @anthor GrainRain
 * @funcation 高德地图坐标转换工具
 * @date 2020/5/13
 */
public class AMapLatLngConverUtil {

    /**
     * LatLng转AMapLatLng
     * @param myLatLngs
     * @return
     */
    public static LatLng latLngToAMapLatLng(com.grain.map.Entity.LatLng myLatLngs) {
        LatLng latLng = null;
        if(myLatLngs != null) {
            latLng = new LatLng(myLatLngs.getLatitude(), myLatLngs.getLongitude());
        }
        return latLng;
    }

    /**
     * LatLng转AMapLatLng
     * @param myLatLngs
     * @return
     */
    public static List<LatLng> latLngToAMapLatLng(List<com.grain.map.Entity.LatLng> myLatLngs) {
        List<LatLng> latLngs = new ArrayList<>();
        for(com.grain.map.Entity.LatLng myLatLng : myLatLngs) {
            latLngs.add(latLngToAMapLatLng(myLatLng));
        }
        return latLngs;
    }

    /**
     * AMapLatLng转LatLng
     * @param latLng
     * @return
     */
    public static com.grain.map.Entity.LatLng aMapLatLngToLatLng(LatLng latLng) {

        com.grain.map.Entity.LatLng myLatLng = null;

        if(latLng != null) {
            myLatLng = new com.grain.map.Entity.LatLng(latLng.latitude, latLng.longitude);
        }
        return myLatLng;
    }

    /**
     * AMapLatLng转LatLng
     * @param latLngs
     * @return
     */
    public static List<com.grain.map.Entity.LatLng> aMapLatLngToLatLng(List<LatLng> latLngs) {

        List<com.grain.map.Entity.LatLng> myLatLngList = new ArrayList<>();

        for(LatLng latLng : latLngs) {
            myLatLngList.add(aMapLatLngToLatLng(latLng));
        }
        return myLatLngList;
    }
}
