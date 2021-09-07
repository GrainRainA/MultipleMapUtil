package com.grain.map.Utils.BaiduMap;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.grain.map.Entity.LatLng;
import com.grain.map.MapView;
import com.grain.map.R;
import com.grain.map.Base.BaseFragment;
import com.grain.map.Utils.DrawNumberBitmapUtils;
import com.grain.utils.hint.L;


import java.util.List;

import androidx.annotation.Nullable;

import static com.grain.map.MapView.MAP_TYPE_NORMAL;
import static com.grain.map.MapView.MAP_TYPE_SATELLITE;
import static com.grain.map.MapView.setCurrentMapType;


/**
 * @anthor GrainRain
 * @funcation 显示百度地图的Fragment
 * @date 2020/5/11
 */
public class BaiduMapFragment extends BaseFragment {

    private View view;

    //百度地图
    private static com.baidu.mapapi.map.MapView baiduMapView;
    private static BaiduMap baiduMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_baidu_map_layout, container, false);

        //百度
        baiduMapView = view.findViewById(R.id.baidu_mapview);
        baiduMap = baiduMapView.getMap();

        baiduMapView.showZoomControls(false);            //禁止显示缩放控件
        baiduMap.getUiSettings().setCompassEnabled(false);      //禁止显示指南针
        baiduMap.getUiSettings().setRotateGesturesEnabled(false);//禁止地图旋转手势.
        baiduMap.getUiSettings().setOverlookingGesturesEnabled(false);      //禁止俯视视图

        //定位工具
        new BaiduMapLocation();

        //设置地图类型
        setMapType(com.grain.map.MapView.getCurrentMapType());

        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(com.baidu.mapapi.model.LatLng latLng) {
                if (com.grain.map.MapView.getOnMapClickListener() != null) {
                    com.grain.map.MapView.getOnMapClickListener().onClick(new LatLng(latLng.latitude, latLng.longitude));
                }
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });

        //marker点击事件
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                L.e(marker.getPosition().latitude + " " + marker.getPosition().longitude);
                return false;
            }
        });

        //polyline点击事件
        baiduMap.setOnPolylineClickListener(new BaiduMap.OnPolylineClickListener() {
            @Override
            public boolean onPolylineClick(Polyline polyline) {

                return false;
            }
        });

        //设置中心点
        moveCamera(MapView.getInitCamearLatLng(), MapView.getInitZoom());

        if (MapView.getSwitchMapSourceListener() != null && MapView.getCurrentMapSource() == MapView.MAP_SOURCE_BAIDU) {
            MapView.getSwitchMapSourceListener().onSuccess();
        }

        return view;
    }

    /**
     * 设置地图类型
     * MAP_TYPE_SATELLITE 卫星图
     * MAP_TYPE_NORMAL 电子地图
     *
     * @param mapType
     */
    public static void setMapType(int mapType) {
        if (baiduMap != null) {
            if (mapType == MAP_TYPE_SATELLITE) {
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                setCurrentMapType(MAP_TYPE_SATELLITE);
            } else if (mapType == MAP_TYPE_NORMAL) {
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                setCurrentMapType(MAP_TYPE_NORMAL);
            }
        } else {
            L.e("setMapType onError: baiduMap == null");
        }
    }

    /**
     * 移动地图中心点
     *
     * @param myLatLng
     * @param zoom
     */
    public static void moveCamera(LatLng myLatLng, int zoom) {
        if (baiduMap == null) return;
        MapStatus mapStatus = new MapStatus.Builder()
                .target(new com.baidu.mapapi.model.LatLng(myLatLng.getLatitude(), myLatLng.getLongitude()))
                .zoom(zoom)
                .build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * 放大地图
     */
    public static void enlargeMapZoom() {
        float zoom = baiduMap.getMapStatus().zoom;
        if (zoom < baiduMap.getMaxZoomLevel()) {
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(zoom + 1));
        }
    }

    /**
     * 缩小地图
     */
    public static void narrowMapZoom() {
        float zoom = baiduMap.getMapStatus().zoom;
        if (zoom > baiduMap.getMinZoomLevel()) {
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(zoom - 1));
        }
    }

    /**
     * 获取地图当前缩放级别
     *
     * @return
     */
    public static float getMapZoom() {
        if (baiduMap != null) {
            return baiduMap.getMapStatus().zoom;
        }

        return 0;
    }

    /**
     * 添加Marker
     * @param myLatLng
     * @param bitmap
     * @param rotateAngle
     * @return
     */
    public static Marker addMarker(LatLng myLatLng, Bitmap bitmap, float rotateAngle) {

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

        if (baiduMap != null && myLatLng != null) {
            com.baidu.mapapi.model.LatLng latLng = new com.baidu.mapapi.model.LatLng(myLatLng.getLatitude(), myLatLng.getLongitude());

            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(latLng)
                    .icon(bitmapDescriptor)
                    .draggable(false)
                    .anchor(0.5f, 0.5f);

            Overlay overlay = baiduMap.addOverlay(option);
            Marker marker = (Marker) overlay;
            marker.setRotate(rotateAngle);

            return marker;
        }

        return null;
    }

    /**
     * 添加Polyline
     *
     * @param myLatLngList
     * @return
     */
    public static Polyline addPolyline(List<LatLng> myLatLngList, int color, int width, boolean setDottedLine) {

        if (baiduMap == null) return null;
        Overlay overlay = null;
        if (myLatLngList.size() >= 2) {
            //设置折线的属性
            OverlayOptions options = new PolylineOptions()
                    .width(width)
                    .color(color)
                    .dottedLine(setDottedLine)
//                    .clickable(true)
                    .points(BaiduMapLatLngConverUtil.baiduMapLatLngToLatLng(myLatLngList));

            overlay = baiduMap.addOverlay(options);
        }

        return (Polyline) overlay;
    }


    @Override
    public void onResume() {
        super.onResume();
        baiduMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        baiduMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baiduMapView.onDestroy();
    }
}
