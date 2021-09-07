package com.grain.map.Entity;

import com.grain.map.MapView;
import com.grain.map.Utils.AMAp.AMapLatLngConverUtil;
import com.grain.map.Utils.GoogleMap.GoogleMap;

import java.util.List;

import static com.grain.map.MapView.*;

/**
 * @anthor GrainRain
 * @funcation 自定义Polyline类
 * @date 2020/5/12
 */
public class Polyline {

    //  Polyline类型
    private int type = 0;

    private com.amap.api.maps.model.Polyline aMapPolyline;
    private com.baidu.mapapi.map.Polyline baiduPolyline;
    private com.tencent.tencentmap.mapsdk.maps.model.Polyline tencentPolyline;
    private org.osmdroid.views.overlay.Polyline googlePolyline;

    public Polyline() {
    }

    /**
     * 创建AMap Polyline
     * @param aMapPolyline
     */
    public Polyline(com.amap.api.maps.model.Polyline aMapPolyline) {
        type = MAP_SOURCE_AMAP;
        this.aMapPolyline = aMapPolyline;
    }

    /**
     * 创建BaiduMap Polyline
     * @param baiduPolyline
     */
    public Polyline(com.baidu.mapapi.map.Polyline baiduPolyline) {
        type = MAP_SOURCE_BAIDU;
        this.baiduPolyline = baiduPolyline;
    }

    /**
     * 创建TencentMap Polyline
     * @param tencentPolyline
     */
    public Polyline(com.tencent.tencentmap.mapsdk.maps.model.Polyline tencentPolyline) {
        type = MAP_SOURCE_TENCENT;
        this.tencentPolyline = tencentPolyline;
    }

    public Polyline(org.osmdroid.views.overlay.Polyline googlePolyline) {
        type = MAP_SOURCE_GOOGLE;
        this.googlePolyline = googlePolyline;
    }

    /**
     * 移除Polyline
     */
    public void remove() {
        switch (MapView.getCurrentMapSource()) {
            case MAP_SOURCE_AMAP:
                if(aMapPolyline != null) aMapPolyline.remove();
                break;
            case MAP_SOURCE_TENCENT:
                if(tencentPolyline != null) tencentPolyline.remove();
                break;
            case MAP_SOURCE_BAIDU:
                if(baiduPolyline != null) baiduPolyline.remove();
                break;
            case MAP_SOURCE_GOOGLE:
                if(googlePolyline != null) {
                    GoogleMap.getMap().getOverlays().remove(googlePolyline);
                    GoogleMap.getMap().invalidate();
                }
                break;
        }
    }

    /**
     *
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     * 获取线段内的坐标
     * @return
     */
    public List<LatLng> getPoints() {

        switch (MapView.getCurrentMapSource()) {
            case MAP_SOURCE_AMAP:
                return AMapLatLngConverUtil.aMapLatLngToLatLng(aMapPolyline.getPoints());
            case MAP_SOURCE_TENCENT:
                break;
            case MAP_SOURCE_BAIDU:
                break;
        }
        return null;
    }

}
