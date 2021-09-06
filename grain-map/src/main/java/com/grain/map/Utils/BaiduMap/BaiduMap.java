package com.grain.map.Utils.BaiduMap;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Polyline;
import com.grain.map.Entity.LatLng;
import com.grain.map.R;
import com.grain.map.Utils.DrawNumberBitmapUtils;

import java.util.List;

/**
 * @anthor GrainRain
 * @funcation 百度地图方法
 * @date 2020/5/12
 */
public class BaiduMap {

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
        BaiduMapFragment.moveCamera(myLatLng, zoom);
    }

    /**
     * 放大地图
     */
    public static void enlargeMapZoom() {
        BaiduMapFragment.enlargeMapZoom();
    }

    /**
     * 缩小地图
     */
    public static void narrowMapZoom() {
        BaiduMapFragment.narrowMapZoom();
    }

    /**
     * 获取地图当前缩放级别
     * @return
     */
    public static float getMapZoom() {
        return BaiduMapFragment.getMapZoom();
    }

    /**
     * 添加Marker
     * @param myLatLng
     * @return
     */
    public static Marker addMarker(LatLng myLatLng) {
        return addMarker(myLatLng, 0, false);
    }

    /**
     * 添加Marker
     * @param myLatLng
     * @param rotateAngle
     * @return
     */
    public static Marker addMarker(LatLng myLatLng, float rotateAngle) {
        return addMarker(myLatLng, rotateAngle, false);
    }

    /**
     * 添加Marker
     * @param myLatLng
     * @param draggable 设置可拖动
     * @return
     */
    public static Marker addMarker(LatLng myLatLng, float rotateAngle, boolean draggable) {

        //构建Marker图标
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.leading_mark);
        return BaiduMapFragment.addMarker(myLatLng, descriptor, rotateAngle, draggable);
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
        //构建Marker图标
        Bitmap bitmap = DrawNumberBitmapUtils.getNumberBitmap(50, num, isSelect);
        return BaiduMapFragment.addMarker(myLatLng, bitmap, 0, false);
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
     * @param setDottedLine
     * @return
     */
    public static Polyline addPolyline(List<LatLng> latLngList, int color, boolean setDottedLine) {
        return addPolyline(latLngList, color, 5, setDottedLine);
    }

    /**
     * 添加Polyline
     * @param latLngList
     * @param color
     * @param width
     * @param setDottedLine
     * @return
     */
    public static Polyline addPolyline(List<LatLng> latLngList, int color, int width, boolean setDottedLine) {
        return BaiduMapFragment.addPolyline(latLngList, color, width, setDottedLine);
    }
}
