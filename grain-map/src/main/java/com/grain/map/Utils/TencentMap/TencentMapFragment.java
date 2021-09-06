package com.grain.map.Utils.TencentMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grain.map.Common.CoordinateSystemType;
import com.grain.map.Entity.LatLng;
import com.grain.map.InitMapModule;
import com.grain.map.R;
import com.grain.map.Base.BaseFragment;
import com.grain.utils.Interfaces.DirectionSensorListener;
import com.grain.utils.hint.L;
import com.grain.utils.sensor.DirectionSensor;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.grain.map.MapView.MAP_TYPE_NORMAL;
import static com.grain.map.MapView.MAP_TYPE_SATELLITE;
import static com.grain.map.MapView.setCurrentMapType;

/**
 * @anthor GrainRain
 * @funcation 显示腾讯地图的Fragment
 * @date 2020/5/11
 */
public class TencentMapFragment extends BaseFragment {

    private View view;

    //腾讯地图
    private static MapView tencentMapView;
    private static TencentMap tencentMap;

    //设置类
    private UiSettings tencentMapUiSettings;

    //定位
    private TencentLocationManager locationManager;
    private TencentLocationRequest request;

    private static com.grain.map.Entity.LatLng oldLatLng;
    private static float oldDirection;
    private static float oldRadius;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tencent_map_layout, container, false);
        initView();

        if(com.grain.map.MapView.getSwitchMapSourceListener() != null && com.grain.map.MapView.getCurrentMapSource() == com.grain.map.MapView.MAP_SOURCE_TENCENT) {
            com.grain.map.MapView.getSwitchMapSourceListener().onSuccess();
        }

        return view;
    }

    private void initView() {

        tencentMapView = (MapView) view.findViewById(R.id.tencent_mapview);
        tencentMap = tencentMapView.getMap();

        //初始化地图
        tencentMapUiSettings = tencentMap.getUiSettings();
        tencentMapUiSettings.setRotateGesturesEnabled(false);
        tencentMapUiSettings.setZoomControlsEnabled(false);

        //设置地图类型
        setMapType(com.grain.map.MapView.getCurrentMapType());

        //设置中心点
        moveCamera(com.grain.map.MapView.getInitCamearLatLng(), com.grain.map.MapView.getInitZoom());

        //初始化定位设置
        locationManager = TencentLocationManager.getInstance(InitMapModule.getActivity());
        request = TencentLocationRequest.create();      //定位请求
        request.setInterval(1000);
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_NAME);
        request.setAllowGPS(true);
        request. setAllowDirection(false);    //是否需要获取传感器方向
        locationManager.requestLocationUpdates(request, locationListener);

        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener() {
            @Override
            public void onMapClick(com.tencent.tencentmap.mapsdk.maps.model.LatLng latLng) {
                if (com.grain.map.MapView.getOnMapClickListener() != null) {
                    com.grain.map.MapView.getOnMapClickListener().onClick(new com.grain.map.Entity.LatLng(latLng.latitude, latLng.longitude));
                }
            }
        });

        //方向传感器回调
        new DirectionSensor(new DirectionSensorListener() {
            @Override
            public void onSensorChanged(float x, float y, float z) {
                if(oldLatLng != null && Math.abs(oldDirection - x) > 1) {
                    com.grain.map.MapView.getLocationListener().onReceiveLocation(oldLatLng, Math.abs(x - 360), oldRadius);
                    oldDirection = x;
                }
            }
        });
    }

    private TencentLocationListener locationListener = new TencentLocationListener() {

        @Override
        public void onLocationChanged(TencentLocation location, int i, String s) {

            if(com.grain.map.MapView.getLocationListener() != null) {
                com.grain.map.Entity.LatLng latLng = new com.grain.map.Entity.LatLng(location.getLatitude(), location.getLongitude(), CoordinateSystemType.GCJ02);

                if(latLng.latitude > 10 && latLng.latitude != 0 && latLng.longitude > 10 && latLng.longitude != 0) {
                    if(oldLatLng != null) {
                        if(oldLatLng.latitude != latLng.latitude || oldLatLng.longitude != latLng.longitude) {

                            com.grain.map.MapView.getLocationListener().onReceiveLocation(latLng, oldDirection, location.getAccuracy());
                            oldLatLng = latLng;
                            oldRadius = location.getAccuracy();
                        }
                    } else {
                        oldLatLng = latLng;
                        oldRadius = location.getAccuracy();
                    }
                }
            }
        }

        @Override
        public void onStatusUpdate(String s, int i, String s1) {

        }
    };

    /**
     *  设置地图类型
     *  MAP_TYPE_SATELLITE 卫星图
     *  MAP_TYPE_NORMAL 电子地图
     * @param mapType
     */
    public static void setMapType(int mapType) {
        if(mapType == MAP_TYPE_SATELLITE) {
            tencentMap.setMapStyle(TencentMap.MAP_TYPE_SATELLITE);
            setCurrentMapType(MAP_TYPE_SATELLITE);
        } else if(mapType == MAP_TYPE_NORMAL) {
            tencentMap.setMapStyle(TencentMap.MAP_TYPE_NORMAL);
            setCurrentMapType(MAP_TYPE_NORMAL);
        }
    }

    /**
     * 移动地图中心点
     * @param myLatLng
     * @param zoom
     */
    public static void moveCamera(LatLng myLatLng, int zoom) {
        CameraUpdate cameraSigma = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new com.tencent.tencentmap.mapsdk.maps.model.LatLng(myLatLng.getLatitude(), myLatLng.getLongitude()), zoom, 0, 0));
        if(tencentMap != null) {
            tencentMap.moveCamera(cameraSigma);
        }
    }

    /**
     * 放大地图
     */
    public static void enlargeMapZoom() {
        tencentMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    /**
     * 缩小地图
     */
    public static void narrowMapZoom() {
        tencentMap.animateCamera(CameraUpdateFactory.zoomOut());
    }

    /**
     * 获取地图当前缩放级别
     * @return
     */
    public static float getMapZoom() {
        return tencentMap.getCameraPosition().zoom;
    }

    /**
     * 添加Marker
     * @param myLatLng
     * @param bitmap
     * @param draggable 设置可拖动
     * @return
     */
    public static Marker addMarker(LatLng myLatLng, BitmapDescriptor bitmap, float rotateAngle, boolean draggable) {

        com.tencent.tencentmap.mapsdk.maps.model.LatLng latLng = new com.tencent.tencentmap.mapsdk.maps.model.LatLng(myLatLng.getLatitude(), myLatLng.getLongitude());
        Marker marker = null;

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new com.tencent.tencentmap.mapsdk.maps.model.LatLng(latLng.latitude, latLng.longitude))
                .anchor(0.5f, 0.5f)
                .icon(bitmap)
                .draggable(draggable);

        if(markerOptions != null && tencentMap != null) {
            marker = tencentMap.addMarker(markerOptions);
            marker.setRotation(rotateAngle);
        }

        return marker;
    }

    /**
     * 添加Polyline
     * @param myLatLngList
     * @return
     */
    public static Polyline addPolyline(List<LatLng> myLatLngList, int color, int width, boolean setDottedLine) {
        if(tencentMap == null) return null;

        Polyline polyline = tencentMap.addPolyline(new PolylineOptions()
                .addAll(TencentMapLatLngConverUtil.tencentMapLatLngToLatLng(myLatLngList))
                .color(color)
                .width(width));

        //设置虚线
        if(setDottedLine) {
            List<Integer> pattern = new ArrayList<>();
            pattern.add(20);
            pattern.add(10);
            polyline.pattern(pattern);
        }

        return polyline;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        tencentMapView.onStart();
//    }

    @Override
    public void onResume() {
        super.onResume();
        tencentMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        tencentMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        tencentMapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tencentMapView.onDestroy();
        locationManager.removeUpdates(locationListener);
    }

}
