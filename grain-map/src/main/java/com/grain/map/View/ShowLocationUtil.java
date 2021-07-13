package com.grain.map.View;

import android.graphics.Color;

import com.grain.map.Entity.LatLng;
import com.grain.map.Entity.Marker;
import com.grain.map.Entity.Polyline;
import com.grain.map.MapView;
import com.grain.map.Utils.LatLngCalculationDistance;

import java.util.ArrayList;
import java.util.List;

/**
 * @anthor GrainRain
 * @funcation 显示设备位置工具
 * @date 2021/6/30
 */
public class ShowLocationUtil {

    private static boolean initState = true;

    //设备
    private static LatLng lastDeviceLatLng;
    private static Marker deviceMarker;

    //设备
    private static LatLng lastRemoteControlLatLng;
    private static Marker remoteControlMarker;

    private static MapView mMap;
    private static Polyline deviceConnectPolyline;
    private static List<Polyline> devicePolylineList;

    /**
     * 显示设备图标
     * @param map
     * @param latLng
     * @param rotateAngle
     * @param isShowLocus 显示轨迹
     * @param spacing
     */
    public static void device(MapView map, LatLng latLng, int rotateAngle, boolean isShowLocus, float spacing) {

        if (map == null || latLng == null || latLng.latitude == 0 || latLng.longitude == 0) return;
        mMap = map;

        if (lastDeviceLatLng != null) {
            if (lastDeviceLatLng.latitude != latLng.latitude || lastDeviceLatLng.longitude != latLng.longitude) {
                if (deviceMarker != null) deviceMarker.remove();
                deviceMarker = map.addMarker(latLng, rotateAngle);

                showDeviceLocus(map, isShowLocus, spacing, latLng, lastDeviceLatLng);
                lastDeviceLatLng = latLng;
            }
        } else {
            deviceMarker = map.addMarker(latLng);
            lastDeviceLatLng = latLng;
        }
    }

    /**
     * 显示设备轨迹
     * @param map
     * @param isShowLocus
     * @param spacing 两坐标距离超过指定值后再绘制轨迹
     * @param latLng
     * @param lastLatLng
     */
    private static void showDeviceLocus(MapView map, boolean isShowLocus, float spacing, LatLng latLng, LatLng lastLatLng) {
        if (devicePolylineList == null) devicePolylineList = new ArrayList<>();
        if (isShowLocus) {
            if (LatLngCalculationDistance.calculateLineDistance(latLng, lastLatLng) > spacing) {
                devicePolylineList.add(map.addPolyline(Color.GREEN, false, latLng, lastLatLng));
            }
        }
    }

    /**
     * 清除设备轨迹
     */
    public static void removeDeviceLocus() {
        if(devicePolylineList != null) {
            for (Polyline polyline : devicePolylineList) {
                polyline.remove();
            }
            devicePolylineList.clear();
        }
    }

    /**
     * 地图移动到设备位置
     */
    public static void moveDeviceLocation() {
        if(mMap != null && lastDeviceLatLng != null &&
                lastDeviceLatLng.latitude != 0 && lastDeviceLatLng.longitude != 0) {
            mMap.moveCamera(lastDeviceLatLng);
        }
    }

    /**
     * 地图移动到遥控器位置
     */
    public static void moveRemoteControlLocation() {
        if(mMap != null && lastRemoteControlLatLng != null &&
                lastRemoteControlLatLng.latitude != 0 && lastRemoteControlLatLng.longitude != 0) {
            mMap.moveCamera(lastRemoteControlLatLng);
        }
    }

    /**
     * 显示遥控器图标
     *
     * @param map
     * @param latLng
     * @param isShowDeviceConnect
     */
    public static void remoteControl(MapView map, LatLng latLng, boolean isShowDeviceConnect) {
        if (map == null || latLng == null || latLng.latitude == 0 || latLng.longitude == 0) return;
        mMap = map;

        if (lastRemoteControlLatLng != null) {
            if (lastRemoteControlLatLng.latitude != latLng.latitude || lastRemoteControlLatLng.longitude != latLng.longitude) {
                if (remoteControlMarker != null) remoteControlMarker.remove();
                remoteControlMarker = map.addMarker(latLng);

                lastRemoteControlLatLng = latLng;
                showDeviceConnect(map, isShowDeviceConnect);

                if (initState) {
                    map.moveCamera(latLng);
                    initState = false;
                }
            }
        } else {
            remoteControlMarker = map.addMarker(latLng);
            lastRemoteControlLatLng = latLng;
        }
    }

    /**
     * 显示设备与遥控器的连线
     * @param map
     * @param isShow
     */
    public static void showDeviceConnect(MapView map, boolean isShow) {
        if (isShow) {
            if(deviceConnectPolyline != null) deviceConnectPolyline.remove();
            deviceConnectPolyline = map.addPolyline(Color.WHITE, true, lastDeviceLatLng, lastRemoteControlLatLng);
        }
    }
}
