package com.grain.map.Utils.GoogleMap;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grain.map.Base.BaseFragment;
import com.grain.map.Common.CoordinateSystemType;
import com.grain.map.Entity.LatLng;
import com.grain.map.InitMapModule;
import com.grain.map.R;
import com.grain.map.Utils.LatLngConvertUtil;
import com.grain.utils.Interfaces.DirectionSensorListener;
import com.grain.utils.hint.L;
import com.grain.utils.sensor.DirectionSensor;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

import androidx.annotation.Nullable;

import static com.grain.map.MapView.MAP_TYPE_NORMAL;
import static com.grain.map.MapView.MAP_TYPE_SATELLITE;
import static com.grain.map.MapView.setCurrentMapType;

/**
 * @anthor GrainRain
 * @funcation 显示谷歌地图的Fragment
 * @date 2020/8/17
 */
public class GoogleMapFragment extends BaseFragment {

    private View view;
    private static MapView map;
    private static IMapController mapController;

    private MyLocationNewOverlay myLocationoverlay;

    private static com.grain.map.Entity.LatLng oldLatLng;
    private static float oldDirection;
    private static float oldRadius;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_google_map_layout, container, false);
        initView();

        if(com.grain.map.MapView.getSwitchMapSourceListener() != null && com.grain.map.MapView.getCurrentMapSource() == com.grain.map.MapView.MAP_SOURCE_GOOGLE) {
            com.grain.map.MapView.getSwitchMapSourceListener().onSuccess();
        }

        return view;
    }

    private void initView() {
        map = view.findViewById(R.id.google_mapview);

        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(false);
        map.setMaxZoomLevel(20.0);      //设置最大放大等级

        //地图控制器
        mapController = map.getController();

        //注册点击
        map.getOverlays().add(new org.osmdroid.views.overlay.MapEventsOverlay(receiver));

        //设置中心点
        moveCamera(com.grain.map.MapView.getInitCamearLatLng(), com.grain.map.MapView.getInitZoom());

        //设置地图类型
        setMapType(com.grain.map.MapView.getCurrentMapType());

        myLocationoverlay = new MyLocationNewOverlay(map);
        myLocationoverlay.enableMyLocation();
        myLocationoverlay.setDrawAccuracyEnabled(false);
//        map.getOverlays().add(myLocationoverlay);
        myLocationoverlay.runOnFirstFix(new Runnable() {
            public void run() {

                if(com.grain.map.MapView.getLocationListener() != null) {

                    LatLng latLng = LatLngConvertUtil.WGS84ToGCJ02(
                            new LatLng(myLocationoverlay.getMyLocation().getLatitude(), myLocationoverlay.getMyLocation().getLongitude(), CoordinateSystemType.GCJ02));

                    if(latLng.latitude > 10 && latLng.latitude != 0 && latLng.longitude > 10 && latLng.longitude != 0) {
                        if(oldLatLng != null) {
                            if(oldLatLng.latitude != latLng.latitude || oldLatLng.longitude != latLng.longitude) {

                                com.grain.map.MapView.getLocationListener().onReceiveLocation(latLng, oldDirection, 0);
                                oldLatLng = latLng;
                                oldRadius = 0;
                            }
                        } else {
                            oldLatLng = latLng;
                            oldRadius = 0;
                        }
                    }
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

    /**
     * 地图点击回调
     */
    private MapEventsReceiver receiver = new MapEventsReceiver() {
        @Override
        public boolean singleTapConfirmedHelper(GeoPoint p) {

            if (com.grain.map.MapView.getOnMapClickListener() != null) {
                com.grain.map.MapView.getOnMapClickListener().onClick(new LatLng(p.getLatitude(), p.getLongitude()));
            }
            L.e(p.getLatitude() + " " + p.getLongitude());
            return false;
        }

        @Override
        public boolean longPressHelper(GeoPoint p) {
            return false;
        }
    };

    /**
     * 移动地图中心点
     *
     * @param myLatLng
     * @param zoom
     */
    public static void moveCamera(final LatLng myLatLng, final int zoom) {
        if(mapController == null || InitMapModule.getActivity() == null) return;

        try {
            InitMapModule.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GeoPoint point = new GeoPoint(myLatLng.getLatitude(), myLatLng.getLongitude());
                    mapController.setCenter(point);
                    mapController.setZoom(zoom);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置地图类型
     * MAP_TYPE_SATELLITE 卫星图
     * MAP_TYPE_NORMAL 电子地图
     *
     * @param mapType
     */
    public static void setMapType(int mapType) {
        if (mapType == MAP_TYPE_SATELLITE) {
            map.setTileSource(GoogleTileSource.GoogleHybrid);
            setCurrentMapType(MAP_TYPE_SATELLITE);
        } else if (mapType == MAP_TYPE_NORMAL) {
            map.setTileSource(GoogleTileSource.GoogleRoads);
            setCurrentMapType(MAP_TYPE_NORMAL);
        }
    }

    /**
     * 添加Marker
     *
     * @param myLatLng
     * @param bitmap
     * @return
     */
    public static Marker addMarker(LatLng myLatLng, Bitmap bitmap, float rotateAngle) {
        if (map != null) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            GeoPoint point = new GeoPoint(myLatLng.getLatitude(), myLatLng.getLongitude());
            Marker marker = new Marker(map);

            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_BOTTOM);
            marker.setRotation(rotateAngle);
            marker.setIcon(bitmapDrawable);
            marker.setOnMarkerClickListener(null);

            map.getOverlays().add(marker);
            map.invalidate();
            return marker;
        }
        return null;
    }

    /**
     * 添加Polyline
     *
     * @param myLatLngList
     * @param color
     * @param width
     * @return
     */
    public static Polyline addPolyline(List<LatLng> myLatLngList, int color, int width, boolean setDottedLine) {
        if (map == null || myLatLngList == null) return null;

        Polyline polyline = new Polyline(getMap());
        polyline.setWidth(width);
        polyline.setColor(color);

        List<GeoPoint> pointList = GoogleMapLatLngConverUtil.latLngToGeoPoint(myLatLngList);
        for (GeoPoint point : pointList) {
            polyline.addPoint(point);
        }
        map.getOverlays().add(polyline);
        map.invalidate();
        return polyline;
    }

    public static MapView getMap() {
        return map;
    }

    public static IMapController getMapController() {
        return mapController;
    }
}