package com.grain.map.Entity;

import com.grain.map.Utils.GoogleMap.GoogleMap;
import com.grain.utils.hint.L;

import static com.grain.map.MapView.MAP_SOURCE_AMAP;
import static com.grain.map.MapView.MAP_SOURCE_BAIDU;
import static com.grain.map.MapView.MAP_SOURCE_GOOGLE;
import static com.grain.map.MapView.MAP_SOURCE_TENCENT;

/**
 * @anthor GrainRain
 * @funcation 自定义Marker
 * @date 2020/5/12
 */
public class Marker {

    //  Marker类型
    private static int type = 0;

    private com.amap.api.maps.model.Marker aMapMarker;
    private com.baidu.mapapi.map.Marker baiduMarker;
    private com.tencent.tencentmap.mapsdk.maps.model.Marker tencentMarker;
    private org.osmdroid.views.overlay.Marker googleMarker;

    private LatLng latLng;
    private boolean isFeeding;     //投料

    public Marker() {
    }

    /**
     * 创建AMAP Marker点
     * @param aMapMarker
     */
    public Marker(com.amap.api.maps.model.Marker aMapMarker) {
        if(aMapMarker == null) return;
        type = MAP_SOURCE_AMAP;
        latLng = new LatLng(aMapMarker.getPosition().latitude, aMapMarker.getPosition().longitude);
        this.aMapMarker = aMapMarker;
    }

    /**
     * 创建BaiduMap Marker点
     * @param baiduMarker
     */
    public Marker(com.baidu.mapapi.map.Marker baiduMarker) {
        if(baiduMarker == null) return;
        type = MAP_SOURCE_BAIDU;
        latLng = new LatLng(baiduMarker.getPosition().latitude, baiduMarker.getPosition().longitude);
        this.baiduMarker = baiduMarker;
    }

    /**
     * 创建TencentMap Marker点
     * @param tencentMarker
     */
    public Marker(com.tencent.tencentmap.mapsdk.maps.model.Marker tencentMarker) {
        if(tencentMarker == null) return;
        type = MAP_SOURCE_TENCENT;
        latLng = new LatLng(tencentMarker.getPosition().latitude, tencentMarker.getPosition().longitude);
        this.tencentMarker = tencentMarker;
    }

    public Marker(org.osmdroid.views.overlay.Marker googleMarker) {
        if(googleMarker == null) return;
        type = MAP_SOURCE_GOOGLE;
        latLng = new LatLng(googleMarker.getPosition().getLatitude(), googleMarker.getPosition().getLongitude());
        this.googleMarker = googleMarker;
    }

    /**
     * 移除Marker
     */
    public void remove() {
        switch (type) {
            case MAP_SOURCE_AMAP:
                if(this.aMapMarker != null) this.aMapMarker.remove();
                break;
            case MAP_SOURCE_BAIDU:
                if(this.baiduMarker != null) this.baiduMarker.remove();
                break;
            case MAP_SOURCE_TENCENT:
                if(this.tencentMarker != null) this.tencentMarker.remove();
                break;
            case MAP_SOURCE_GOOGLE:
                if(this.googleMarker != null) this.googleMarker.remove(GoogleMap.getMap());
                break;
        }
    }

    public LatLng getPosition() {
        return latLng;
    }

    public static int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Marker{" +
                "latLng=" + latLng +
                ", isFeeding=" + isFeeding +
                '}';
    }
}
