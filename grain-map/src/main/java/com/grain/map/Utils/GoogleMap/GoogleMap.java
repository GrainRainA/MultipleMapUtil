package com.grain.map.Utils.GoogleMap;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import com.grain.map.Entity.LatLng;
import com.grain.map.R;
import com.grain.map.Utils.DrawNumberBitmapUtils;
import com.grain.map.Utils.DrawableUtils;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.List;

/**
 * @anthor GrainRain
 * @funcation 谷歌地图方法
 * @date 2020/8/18
 */
public class GoogleMap {

    /**
     * 移动地图中心点
     * @param myLatLng
     */
    public static void moveCamera(LatLng myLatLng) {
        GoogleMap.moveCamera(myLatLng, 16);
    }

    /**
     * 移动地图中心点
     * @param myLatLng
     * @param zoom
     */
    public static void moveCamera(LatLng myLatLng, int zoom) {
        GoogleMapFragment.moveCamera(myLatLng, zoom);
    }

    /**
     * 放大地图
     */
    public static void enlargeMapZoom() {
        getMapController().zoomIn();
    }

    /**
     * 缩小地图
     */
    public static void narrowMapZoom() {
        getMapController().zoomOut();
    }

    /**
     * 获取地图当前缩放级别
     * @return
     */
    public static float getMapZoom() {
        return (float) getMap().getZoomLevelDouble();
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

    /**
     * 添加Marker
     * @param myLatLng 坐标
     * @param bitmap 图片bitmap
     * @param rotateAngle 角度
     * @return
     */
    public static Marker addMarker(LatLng myLatLng, Bitmap bitmap, float rotateAngle) {
        return GoogleMapFragment.addMarker(myLatLng, bitmap, rotateAngle);
    }

    /**
     * 添加序号Marker
     * @param myLatLng 坐标
     * @param num 序号
     * @return
     */
    public static Marker addNumberMarker(LatLng myLatLng, int num) {
        return addNumberMarker(myLatLng, num);
    }

    /**
     * 添加序号Marker
     * @param myLatLng 坐标
     * @param num 序号
     * @param isSelect 是否已选中状态显示
     * @return
     */
    public static Marker addNumberMarker(LatLng myLatLng, int num, boolean isSelect) {
        Bitmap bitmap = DrawNumberBitmapUtils.getNumberBitmap(50, num, isSelect);
        return addMarker(myLatLng, bitmap, 0);
    }

    /**
     * 添加Polyline
     * @param myLatLngList 坐标列表
     * @return
     */
    public static Polyline addPolyline(List<LatLng> myLatLngList) {
        return GoogleMap.addPolyline(myLatLngList, Color.RED, 5, false);
    }

    /**
     * 添加Polyline
     * @param myLatLngList 坐标列表
     * @return
     */
    public static Polyline addPolyline(List<LatLng> myLatLngList, boolean setDottedLine) {
        return GoogleMap.addPolyline(myLatLngList, Color.RED, 5, setDottedLine);
    }

    /**
     * 添加Polyline
     * @param myLatLngList 坐标列表
     * @param color 颜色
     * @param width 线段宽度
     * @return
     */
    public static Polyline addPolyline(List<LatLng> myLatLngList, int color, int width, boolean setDottedLine) {
        return GoogleMapFragment.addPolyline(myLatLngList, color, width, setDottedLine);
    }

    public static MapView getMap() {
        return GoogleMapFragment.getMap();
    }

    public static IMapController getMapController() {
        return GoogleMapFragment.getMapController();
    }
}
