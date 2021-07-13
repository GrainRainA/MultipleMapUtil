package com.grain.map.Utils.AMAp;

import android.graphics.Color;

import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.Polyline;
import com.grain.map.Entity.LatLng;
import com.grain.map.R;
import com.grain.map.Utils.DrawNumberBitmapUtils;

import java.util.List;

/**
 * @anthor GrainRain
 * @funcation 高德地图方法
 * @date 2020/5/12
 */
public class AMap {

    /**
     * 移动地图中心点
     * @param myLatLng
     */
    public static void moveCamera(LatLng myLatLng) {
        moveCamera(myLatLng, 16);
    }

    /**
     * 移动地图中心点
     * @param myLatLng
     * @param zoom
     */
    public static void moveCamera(LatLng myLatLng, int zoom) {
        AMapFragment.moveCamera(myLatLng, zoom);
    }

    /**
     * 放大地图
     */
    public static void enlargeMapZoom() {
        AMapFragment.enlargeMapZoom();
    }

    /**
     * 缩小地图
     */
    public static void narrowMapZoom() {
        AMapFragment.narrowMapZoom();
    }

    /**
     * 获取地图当前缩放级别
     * @return
     */
    public static float getMapZoom() {
        return AMapFragment.getMapZoom();
    }

    /**
     * 添加Marker
     * @param myLatLng
     * @return
     */
    public static Marker addMarker(LatLng myLatLng) {
        return addMarker(myLatLng, 0);
    }

    /**
     * 添加Marker
     * @param myLatLng
     * @param rotateAngle
     * @return
     */
    public static Marker addMarker(LatLng myLatLng, float rotateAngle) {
        BitmapDescriptor descriptor = AMapBitmapUtils.drawableToBitmapDescriptor(R.drawable.leading_mark);
        return AMapFragment.addMarker(myLatLng, descriptor, false, rotateAngle);
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
     * @param isSelect 是否选中
     * @return
     */
    public static Marker addNumberMarker(LatLng myLatLng, int num, boolean isSelect) {
        BitmapDescriptor descriptor = AMapBitmapUtils.bitmapToBitmapDescriptor(DrawNumberBitmapUtils.getNumberBitmap(50, num, isSelect));
        return AMapFragment.addMarker(myLatLng, descriptor, false, 0);
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
        return AMapFragment.addPolyline(latLngList, color, width, setDottedLine);
    }
}
