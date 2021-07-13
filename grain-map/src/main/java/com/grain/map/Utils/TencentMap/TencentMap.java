package com.grain.map.Utils.TencentMap;

import android.graphics.Color;

import com.grain.map.Entity.LatLng;
import com.grain.map.R;
import com.grain.map.Utils.DrawNumberBitmapUtils;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;

import java.util.List;

/**
 * @anthor GrainRain
 * @funcation 腾讯地图方法
 * @date 2020/5/12
 */
public class TencentMap {

    /**
     * 移动地图中心点
     * @param myLatLng
     */
    public static void moveCamera(LatLng myLatLng) {
        TencentMap.moveCamera(myLatLng, 16);
    }

    /**
     * 移动地图中心点
     * @param myLatLng
     * @param zoom
     */
    public static void moveCamera(LatLng myLatLng, int zoom) {
        TencentMapFragment.moveCamera(myLatLng, zoom);
    }

    /**
     * 放大地图
     */
    public static void enlargeMapZoom() {
        TencentMapFragment.enlargeMapZoom();
    }

    /**
     * 缩小地图
     */
    public static void narrowMapZoom() {
        TencentMapFragment.narrowMapZoom();
    }

    /**
     * 获取地图当前缩放级别
     * @return
     */
    public static float getMapZoom() {
        return TencentMapFragment.getMapZoom();
    }

    /**
     * 添加Marker
     * @param myLatLng
     * @return
     */
    public static Marker addMarker(LatLng myLatLng) {
        return TencentMap.addMarker(myLatLng, 0);
    }

    /**
     * 添加Marker
     * @param myLatLng
     * @param rotateAngle
     * @return
     */
    public static Marker addMarker(LatLng myLatLng, float rotateAngle) {
        return TencentMap.addMarker(myLatLng, rotateAngle, false);
    }

    /**
     * 添加Marker
     * @param myLatLng
     * @param rotateAngle
     * @param draggable
     * @return
     */
    public static Marker addMarker(LatLng myLatLng, float rotateAngle, boolean draggable) {
        BitmapDescriptor descriptor = TencentMapBitmapUtils.drawableToBitmapDescriptor(R.drawable.leading_mark);
        return addMarker(myLatLng, descriptor, rotateAngle, draggable);
    }

    /**
     * 添加序号Marker
     * @param myLatLng
     * @param num
     * @return
     */
    public static Marker addNumberMarker(LatLng myLatLng, int num) {
        return addNumberMarker(myLatLng, num, false);
    }

    /**
     * 添加序号Marker
     * @param myLatLng
     * @param num
     * @param isSelect
     * @return
     */
    public static Marker addNumberMarker(LatLng myLatLng, int num, boolean isSelect) {
        BitmapDescriptor descriptor = TencentMapBitmapUtils.bitmapToBitmapDescriptor(DrawNumberBitmapUtils.getNumberBitmap(50, num, isSelect));
        return addMarker(myLatLng, descriptor, 0, false);
    }

    /**
     * 添加Marker
     * @param myLatLng
     * @param bitmap
     * @param rotateAngle
     * @param draggable
     * @return
     */
    public static Marker addMarker(LatLng myLatLng, BitmapDescriptor bitmap, float rotateAngle, boolean draggable) {
        return TencentMapFragment.addMarker(myLatLng, bitmap, rotateAngle, draggable);
    }

    /**
     * 添加Polyline
     * @param latLngList
     * @return
     */
    public static Polyline addPolyline(List<LatLng> latLngList) {
        return addPolyline(latLngList, Color.RED);
    }

    /**
     * 添加Polyline
     * @param latLngList
     * @param color
     * @return
     */
    public static Polyline addPolyline(List<LatLng> latLngList, int color) {
        return addPolyline(latLngList, color, 5, false);
    }

    /**
     * 添加Polyline
     * @param latLngList
     * @param color
     * @return
     */
    public static Polyline addPolyline(List<LatLng> latLngList, int color, boolean setDottedLine) {
        return addPolyline(latLngList, color, 5, setDottedLine);
    }

    /**
     * 添加Polyline
     * @param latLngList
     * @param width
     * @param color
     * @return
     */
    public static Polyline addPolyline(List<LatLng> latLngList, int color, int width, boolean setDottedLine) {
        return TencentMapFragment.addPolyline(latLngList, color, width, setDottedLine);
    }
}
