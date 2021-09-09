package com.grain.maputil;

import android.graphics.Color;

import com.grain.map.Common.CoordinateSystemType;
import com.grain.map.Common.MapParameter;
import com.grain.map.Entity.LatLng;
import com.grain.map.InitMapModule;
import com.grain.map.Interfaces.LocationListener;
import com.grain.map.Listener.MapLoadingFinishedListener;
import com.grain.map.MapView;
import com.grain.map.Utils.LatLngCalculationDistance;
import com.grain.map.Utils.LatLngConvertUtil;
import com.grain.map.View.ShowLocationUtil;
import com.grain.utils.hint.L;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @anthor GrainRain
 * @funcation 地图显示工具
 * @date 2021/6/30
 */
public class MapShowUtil {

    public MapShowUtil(AppCompatActivity activity, final MapView mapView) {

        InitMapModule.init(activity);

        MapParameter mapParameter = new MapParameter.Builder()
                .mapSource(MapView.MAP_SOURCE_BAIDU)
                .mapType(MapView.MAP_TYPE_SATELLITE)
                .zoom(16)
//                .camearLatLng(new LatLng(30.8602733, 118.796859, CoordinateSystemType.WGS84))
                .build();

        MapView.setMapParameter(activity, mapParameter, new MapLoadingFinishedListener() {
            @Override
            public void onSuccess() {
                initMap(mapView);


                List<LatLng> latLngs = new ArrayList<>();
                latLngs.add((new LatLng(39.922245635222545, 116.39215630489747, CoordinateSystemType.WGS84)));
                latLngs.add((new LatLng(39.92300562476863, 116.40187705461851)));
                latLngs.add((new LatLng(39.91354956808267, 116.40227580411027)));
                latLngs.add((new LatLng(39.913452737257145, 116.3931597786972, CoordinateSystemType.WGS84)));


                mapView.moveCamera(new LatLng(39.92300562476863, 116.40187705461851), 10);
                mapView.enlargeMapZoom();
                mapView.narrowMapZoom();
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

            }
        });

        //设备定位
        mapView.setLocationListener(new LocationListener() {
            @Override
            public void onReceiveLocation(LatLng latLng, float direction, float radius) {

//                ShowLocationUtil.remoteControl(mapView, latLng, true);
//                ShowLocationUtil.device(map, deviceLatLng, rotateAngle, true, 1);
            }
        });
    }
}
