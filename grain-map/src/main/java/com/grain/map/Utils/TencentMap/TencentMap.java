package com.grain.map.Utils.TencentMap;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.grain.map.Entity.LatLng;
import com.grain.map.R;
import com.grain.map.Utils.DrawNumberBitmapUtils;
import com.grain.map.Utils.DrawableUtils;
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


    public static Marker addMarker(LatLng myLatLng) {
        return addMarker(myLatLng, 0);
    }

    public static Marker addMarker(LatLng myLatLng, float rotateAngle) {
        return addMarker(myLatLng, R.drawable.leading_mark, rotateAngle);
    }

    public static Marker addMarker(LatLng myLatLng, int res, float rotateAngle) {
        return addMarker(myLatLng, DrawableUtils.drawableToBitmap(res), rotateAngle);
    }

    public static Marker addMarker(LatLng myLatLng, Bitmap bitmap, float rotateAngle) {
        return TencentMapFragment.addMarker(myLatLng, bitmap, rotateAngle);
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
        //构建Marker图标
        Bitmap bitmap = DrawNumberBitmapUtils.getNumberBitmap(50, num, isSelect);
        return addMarker(myLatLng, bitmap, 0);
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
