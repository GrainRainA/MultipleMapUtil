package com.grain.maputil;

import com.grain.map.Common.CoordinateSystemType;
import com.grain.map.Common.MapParameter;
import com.grain.map.Entity.LatLng;
import com.grain.map.InitMapModule;
import com.grain.map.Interfaces.LocationListener;
import com.grain.map.Interfaces.SwitchMapSourceListener;
import com.grain.map.Listener.MapLoadingFinishedListener;
import com.grain.map.MapView;
import com.grain.map.Utils.LatLngCalculationDistance;
import com.grain.map.View.ShowLocationUtil;
import com.grain.utils.hint.L;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @anthor GrainRain
 * @funcation 地图显示工具
 * @date 2021/6/30
 */
public class MapShowUtil {

    private static LatLng deviceLatLng;

    public MapShowUtil(AppCompatActivity activity, final MapView mapView) {

        InitMapModule.init(activity);

        MapParameter mapParameter = new MapParameter.Builder()
                .mapSource(MapView.MAP_SOURCE_BAIDU)
                .mapType(MapView.MAP_TYPE_SATELLITE)
                .zoom(16)
                .camearLatLng(new LatLng(30.8602733, 118.796859, CoordinateSystemType.WGS84))
                .build();

        MapView.setMapParameter(activity, mapParameter, new MapLoadingFinishedListener() {
            @Override
            public void onSuccess() {
                initMap(mapView);
            }
        });
    }

    private void initMap(final MapView mapView) {

        if (mapView == null) return;

//        mapView.switchMapSource(MapView.MAP_SOURCE_AMAP, new SwitchMapSourceListener() {
//            @Override
//            public void onSuccess() {
//
//            }
//        });

        mapView.setOnMapClickListener(new MapView.OnMapClickListener() {
            @Override
            public void onClick(LatLng latLng) {
                mapView.addMarker(latLng, R.drawable.icon_home, 100);
            }
        });

        //设备定位
        mapView.setLocationListener(new LocationListener() {
            @Override
            public void onReceiveLocation(LatLng latLng, float direction, float radius) {

                ShowLocationUtil.remoteControl(mapView, latLng, true);
//                ShowLocationUtil.device(map, deviceLatLng, rotateAngle, true, 1);
            }
        });
    }
}
