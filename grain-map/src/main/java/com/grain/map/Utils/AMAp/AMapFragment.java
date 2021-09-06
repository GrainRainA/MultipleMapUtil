package com.grain.map.Utils.AMAp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.grain.map.Common.CoordinateSystemType;
import com.grain.map.InitMapModule;
import com.grain.map.MapView;
import com.grain.map.R;
import com.grain.map.Base.BaseFragment;
import com.grain.utils.Interfaces.DirectionSensorListener;
import com.grain.utils.hint.L;
import com.grain.utils.sensor.DirectionSensor;

import java.util.List;

import androidx.annotation.Nullable;

import static com.grain.map.MapView.MAP_TYPE_NORMAL;
import static com.grain.map.MapView.MAP_TYPE_SATELLITE;
import static com.grain.map.MapView.setCurrentMapType;


/**
 * @anthor GrainRain
 * @funcation 显示高德地图的Fragment
 * @date 2020/5/11
 */
public class AMapFragment extends BaseFragment {

    private View view;

    //高德地图
    private static com.amap.api.maps2d.MapView aMapView;
    private static AMap aMap;

    //定位
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private static com.grain.map.Entity.LatLng oldLatLng;
    private static float oldDirection;
    private static float oldRadius;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_amap_layout, container, false);
        initView(savedInstanceState);
        return view;
    }

    private void initView(Bundle savedInstanceState) {
        aMapView = view.findViewById(R.id.amap_view);
        aMapView.onCreate(savedInstanceState);
        aMap = aMapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(false);

        //设置地图类型
        setMapType(com.grain.map.MapView.getCurrentMapType());

        //初始化定位
        locationClient = new AMapLocationClient(InitMapModule.getActivity());
        locationClient.setLocationListener(locationListener);
        locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setInterval(1000);
        locationOption.setNeedAddress(false);   //设置是否返回地址信息（默认返回地址信息）
        locationClient.setLocationOption(locationOption);
        //启动定位
        locationClient.startLocation();

        //设置中心点
        moveCamera(MapView.getInitCamearLatLng(), MapView.getInitZoom());

        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (com.grain.map.MapView.getOnMapClickListener() != null) {
                    com.grain.map.MapView.getOnMapClickListener().onClick(new com.grain.map.Entity.LatLng(latLng.latitude, latLng.longitude));
                }
            }
        });

        //方向传感器回调
        new DirectionSensor(new DirectionSensorListener() {
            @Override
            public void onSensorChanged(float x, float y, float z) {
                if (oldLatLng != null && Math.abs(oldDirection - x) > 1) {
                    MapView.getLocationListener().onReceiveLocation(oldLatLng, Math.abs(x - 360), oldRadius);
                    oldDirection = x;
                }
            }
        });

        if (MapView.getSwitchMapSourceListener() != null && com.grain.map.MapView.getCurrentMapSource() == com.grain.map.MapView.MAP_SOURCE_AMAP) {
            MapView.getSwitchMapSourceListener().onSuccess();
        }
    }

    private AMapLocationListener locationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation location) {

            if (location != null) {
                if (location.getErrorCode() == 0) {

                    if (MapView.getLocationListener() != null) {
                        com.grain.map.Entity.LatLng latLng = new com.grain.map.Entity.LatLng(location.getLatitude(), location.getLongitude(), CoordinateSystemType.GCJ02);
                        if (latLng.latitude > 10 && latLng.latitude != 0 && latLng.longitude > 10 && latLng.longitude != 0) {
                            if (oldLatLng != null) {
                                if (oldLatLng.latitude != latLng.latitude || oldLatLng.longitude != latLng.longitude) {

                                    MapView.getLocationListener().onReceiveLocation(latLng, oldDirection, location.getAccuracy());
                                    oldLatLng = latLng;
                                    oldRadius = location.getAccuracy();
                                }
                            } else {
                                oldLatLng = latLng;
                                oldRadius = location.getAccuracy();
                            }
                        }
                    }
                } else {
                    L.v("AmapError: location Error, ErrCode:"
                            + location.getErrorCode() + ", errInfo:"
                            + location.getErrorInfo());
                }
            }
        }
    };

    /**
     * 设置地图类型
     * MAP_TYPE_SATELLITE 卫星图
     * MAP_TYPE_NORMAL 电子地图
     *
     * @param mapType
     */
    public static void setMapType(int mapType) {
        if (mapType == MAP_TYPE_SATELLITE) {
            aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
            setCurrentMapType(MAP_TYPE_SATELLITE);
        } else if (mapType == MAP_TYPE_NORMAL) {
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
            setCurrentMapType(MAP_TYPE_NORMAL);
        }
    }

    /**
     * 移动地图中心点
     *
     * @param myLatLng
     * @param zoom
     */
    public static void moveCamera(com.grain.map.Entity.LatLng myLatLng, int zoom) {
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new com.amap.api.maps2d.model.LatLng(
                myLatLng.getLatitude(), myLatLng.getLongitude()), zoom, 0, 0)));
    }

    /**
     * 放大地图
     */
    public static void enlargeMapZoom() {
        aMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    /**
     * 缩小地图
     */
    public static void narrowMapZoom() {
        aMap.animateCamera(CameraUpdateFactory.zoomOut());
    }

    /**
     * 获取地图当前缩放级别
     *
     * @return
     */
    public static float getMapZoom() {
        return aMap.getCameraPosition().zoom;
    }

    /**
     * 添加Marker
     * @param myLatLng
     * @param bitmap
     * @param draggable
     * @param rotateAngle
     * @return
     */
    public static Marker addMarker(com.grain.map.Entity.LatLng myLatLng, Bitmap bitmap, boolean draggable, float rotateAngle) {
        return addMarker(myLatLng, BitmapDescriptorFactory.fromBitmap(bitmap), draggable, rotateAngle);
    }

    /**
     * 添加Marker
     *
     * @param myLatLng
     * @param bitmap
     * @param draggable 设置可拖动
     * @return
     */
    public static Marker addMarker(com.grain.map.Entity.LatLng myLatLng, BitmapDescriptor bitmap, boolean draggable, float rotateAngle) {

        if (aMap == null) return null;

        LatLng latLng = new LatLng(myLatLng.getLatitude(), myLatLng.getLongitude());
        MarkerOptions options = new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(latLng)
                .icon(bitmap)
                .draggable(draggable);

        Marker marker = aMap.addMarker(options);
        marker.setRotateAngle(rotateAngle);

        return marker;
    }

    /**
     * 添加Polyline
     *
     * @param myLatLngList
     * @return
     */
    public static Polyline addPolyline(List<com.grain.map.Entity.LatLng> myLatLngList, int color, int width, boolean setDottedLine) {
        Polyline polyline = aMap.addPolyline(new PolylineOptions()
                .addAll(AMapLatLngConverUtil.latLngToAMapLatLng(myLatLngList))
                .setDottedLine(setDottedLine)
                .width(width)
                .color(color));
        return polyline;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        aMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        aMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        aMapView.onDestroy();
    }

    public static AMap getaMap() {
        return aMap;
    }
}
