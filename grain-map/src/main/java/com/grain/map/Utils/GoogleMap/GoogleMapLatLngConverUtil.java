package com.grain.map.Utils.GoogleMap;

import com.grain.map.Entity.LatLng;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @anthor GrainRain
 * @funcation 谷歌地图坐标转换工具
 * @date 2020/8/18
 */
public class GoogleMapLatLngConverUtil {

    /**
     * LatLng转GeoPoint
     * @param myLatLng
     * @return
     */
    public static GeoPoint latLngToGeoPoint(LatLng myLatLng) {
        if(myLatLng == null) return null;
        return new GeoPoint(myLatLng.getLatitude(), myLatLng.getLongitude());
    }

    /**
     * LatLng转GeoPoint
     * @param myLatLngs
     * @return
     */
    public static List<GeoPoint> latLngToGeoPoint(List<LatLng> myLatLngs) {
        if(myLatLngs == null) return null;
        List<GeoPoint> pointList = new ArrayList<>();
        for (LatLng latLng : myLatLngs) {
            pointList.add(GoogleMapLatLngConverUtil.latLngToGeoPoint(latLng));
        }
        return pointList;
    }
}
