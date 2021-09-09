package com.grain.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.grain.map.Common.CoordinateSystemType;
import com.grain.map.Common.MapParameter;
import com.grain.map.Entity.LatLng;
import com.grain.map.Entity.Marker;
import com.grain.map.Entity.Polyline;
import com.grain.map.Interfaces.SwitchMapSourceListener;
import com.grain.map.Listener.MapLoadingFinishedListener;
import com.grain.map.Utils.AMAp.AMapFragment;
import com.grain.map.Utils.BaiduMap.BaiduMapFragment;
import com.grain.map.Utils.DrawableUtils;
import com.grain.map.Utils.GoogleMap.GoogleMapFragment;
import com.grain.map.Utils.LatLngConvertUtil;
import com.grain.map.Utils.TencentMap.TencentMapFragment;
import com.grain.map.Interfaces.LocationListener;
import com.grain.map.Utils.AMAp.AMap;
import com.grain.map.Utils.BaiduMap.BaiduMap;
import com.grain.map.Utils.GoogleMap.GoogleMap;
import com.grain.map.Utils.TencentMap.TencentMap;
import com.grain.utils.hint.L;
import com.grain.utils.utils.SharedPreferenceUtils;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.grain.map.InitMapModule.getActivity;

/**
 * @anthor GrainRain
 * @funcation 地图
 * @date 2020/5/11
 */
public class MapView extends RelativeLayout {

    //地图类型
    public static final int MAP_TYPE_SATELLITE = 1;     //卫星图
    public static final int MAP_TYPE_NORMAL = 2;        //电子地图

    //地图源
    public static final int MAP_SOURCE_TENCENT = 1;
    public static final int MAP_SOURCE_BAIDU = 2;
    public static final int MAP_SOURCE_AMAP = 3;
    public static final int MAP_SOURCE_GOOGLE = 4;

    private View view;
    private static OnMapClickListener onMapClickListener;
    private static LocationListener locationListener;

    private static MapLoadingFinishedListener listener;
    private static MapView mapView;


    //当前地图源
    private static int currentMapSource = MAP_SOURCE_AMAP;
    //当前地图类型
    private static int currentMapType = MAP_TYPE_SATELLITE;

    //初始化中心点
    private static LatLng initCamearLatLng = new LatLng(39.917190478161814, 116.39725613251412);
    //初始化地图级别
    private static int initZoom = 18;

    private static SwitchMapSourceListener switchMapSourceListener;

    public MapView(Context context) {
        super(context);
        initView(context);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.map_view_layout, this);
        switchMapSource(currentMapSource, null);
    }

    public static MapView getInstance(Context context) {
        return new MapView(context);
    }

    public static void setMapParameter(Context context, MapParameter mapParameter, MapLoadingFinishedListener mListener) {
        mapView = new MapView(context).setMapParameter(mapParameter);
        listener = mListener;
    }

    private MapView setMapParameter(final MapParameter mapParameter) {
        if (mapParameter != null) {

            if (mapParameter.camearLatLng != null) {
                if (mapParameter.camearLatLng.type == CoordinateSystemType.WGS84) {
                    initCamearLatLng = LatLngConvertUtil.WGS84ToGCJ02(mapParameter.camearLatLng);
                } else if (mapParameter.camearLatLng.type == CoordinateSystemType.GCJ02) {
                    initCamearLatLng = mapParameter.camearLatLng;
                }
            }

            initZoom = mapParameter.zoom;

            switchMapSource(mapParameter.mapSource, new SwitchMapSourceListener() {
                @Override
                public void onSuccess() {
                    switchMapType(mapParameter.mapType);
                    moveCamera(initCamearLatLng, initZoom);
                    listener.onSuccess();
                }
            });
        }

        return this;
    }

    /**
     * 定位信息回调
     *
     * @param mLocationListener
     * @return
     */
    public void setLocationListener(LocationListener mLocationListener) {
        locationListener = mLocationListener;
    }

    /**
     * 切换地图源
     * MAP_SOURCE_TENCENT = 1;
     * MAP_SOURCE_BAIDU = 2;
     * MAP_SOURCE_AMAP = 3;
     *
     * @param mapSource
     */
    public void switchMapSource(int mapSource, SwitchMapSourceListener listener) {
        switchMapSourceListener = listener;
        SharedPreferenceUtils.get("lastOpenMapType", mapSource);

        switch (mapSource) {
            case MAP_SOURCE_AMAP:
                replaceFragment(new AMapFragment());
                currentMapSource = MAP_SOURCE_AMAP;
                break;
            case MAP_SOURCE_TENCENT:
                replaceFragment(new TencentMapFragment());
                currentMapSource = MAP_SOURCE_TENCENT;
                break;
            case MAP_SOURCE_BAIDU:
                replaceFragment(new BaiduMapFragment());
                currentMapSource = MAP_SOURCE_BAIDU;
                break;
            case MAP_SOURCE_GOOGLE:
                replaceFragment(new GoogleMapFragment());
                currentMapSource = MAP_SOURCE_GOOGLE;
                break;
        }
    }

    /**
     * 切换地图类型
     * MAP_TYPE_SATELLITE = 1
     * MAP_TYPE_NORMAL = 2
     *
     * @param mapType
     */
    public void switchMapType(int mapType) {
        switch (currentMapSource) {
            case MAP_SOURCE_AMAP:
                AMapFragment.setMapType(mapType);
                break;
            case MAP_SOURCE_TENCENT:
                TencentMapFragment.setMapType(mapType);
                break;
            case MAP_SOURCE_BAIDU:
                BaiduMapFragment.setMapType(mapType);
                break;
            case MAP_SOURCE_GOOGLE:
                GoogleMapFragment.setMapType(mapType);
                break;
        }
    }

    /**
     * 将地图中心点移动到指定坐标
     *
     * @param latitude  纬度
     * @param longitude 经度
     */
    public void moveCamera(double latitude, double longitude) {
        moveCamera(latitude, longitude, getMapZoom());
    }

    /**
     * 将地图中心点移动到指定坐标
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @param zoom      地图级别
     */
    public void moveCamera(double latitude, double longitude, int zoom) {
        moveCamera(new LatLng(latitude, longitude), zoom);
    }

    /**
     * 将地图中心点移动到指定坐标
     *
     * @param latLng 坐标
     */
    public void moveCamera(LatLng latLng) {
        moveCamera(latLng, getMapZoom());
    }

    /**
     * 将地图中心点移动到指定坐标
     *
     * @param latLng 坐标
     * @param zoom   地图级别
     */
    public void moveCamera(LatLng latLng, int zoom) {
        //地图显示坐标系为火星坐标系，如传入的坐标类型为WGS84，则进行转换
        LatLng newLatLng = latLng;
        if (latLng.getType() != null && latLng.getType() == CoordinateSystemType.WGS84) {
            newLatLng = LatLngConvertUtil.WGS84ToGCJ02(latLng.latitude, latLng.longitude);
        }

        switch (currentMapSource) {
            case MAP_SOURCE_AMAP:
                AMap.moveCamera(newLatLng, zoom);
                break;
            case MAP_SOURCE_TENCENT:
                TencentMap.moveCamera(newLatLng, zoom);
                break;
            case MAP_SOURCE_BAIDU:
                BaiduMap.moveCamera(newLatLng, zoom);
                break;
            case MAP_SOURCE_GOOGLE:
                GoogleMap.moveCamera(newLatLng, zoom);
                break;
        }
    }

    /**
     * 放大地图
     */
    public void enlargeMapZoom() {
        switch (currentMapSource) {
            case MAP_SOURCE_AMAP:
                AMap.enlargeMapZoom();
                break;
            case MAP_SOURCE_TENCENT:
                TencentMap.enlargeMapZoom();
                break;
            case MAP_SOURCE_BAIDU:
                BaiduMap.enlargeMapZoom();
                break;
            case MAP_SOURCE_GOOGLE:
                GoogleMap.enlargeMapZoom();
                break;
        }
    }

    /**
     * 缩小地图
     */
    public void narrowMapZoom() {
        switch (currentMapSource) {
            case MAP_SOURCE_AMAP:
                AMap.narrowMapZoom();
                break;
            case MAP_SOURCE_TENCENT:
                TencentMap.narrowMapZoom();
                break;
            case MAP_SOURCE_BAIDU:
                BaiduMap.narrowMapZoom();
                break;
            case MAP_SOURCE_GOOGLE:
                GoogleMap.narrowMapZoom();
                break;
        }
    }

    /**
     * 获取地图当前缩放级别
     *
     * @return
     */
    public static int getMapZoom() {
        switch (currentMapSource) {
            case MAP_SOURCE_AMAP:
                return (int) AMap.getMapZoom();
            case MAP_SOURCE_TENCENT:
                return (int) TencentMap.getMapZoom();
            case MAP_SOURCE_BAIDU:
                return (int) BaiduMap.getMapZoom();
            case MAP_SOURCE_GOOGLE:
                return (int) GoogleMap.getMapZoom();
        }
        return 0;
    }

    public Marker addMarker(LatLng latLng) {
        return this.addMarker(latLng, 0);
    }

    public Marker addMarker(LatLng latLng, float rotateAngle) {
        return addMarker(latLng, R.drawable.leading_mark, rotateAngle);
    }

    public Marker addMarker(LatLng latLng, int res, float rotateAngle) {
        return addMarker(latLng, DrawableUtils.drawableToBitmap(res), rotateAngle);
    }

    /**
     * 添加Marker
     * @param latLng 坐标
     * @param bitmap 显示图标
     * @param rotateAngle 旋转角度
     * @return 自定义Marker
     */
    public Marker addMarker(LatLng latLng, Bitmap bitmap, float rotateAngle) {
        //地图显示坐标系为火星坐标系，如传入的坐标类型为WGS84，则进行转换
        LatLng newLatLng = latLng;
        if (latLng.getType() != null && latLng.getType() == CoordinateSystemType.WGS84) {
            newLatLng = LatLngConvertUtil.WGS84ToGCJ02(latLng.latitude, latLng.longitude);
        }

        switch (currentMapSource) {
            case MAP_SOURCE_AMAP:
                return new Marker(AMap.addMarker(newLatLng, bitmap, rotateAngle));
            case MAP_SOURCE_TENCENT:
                return new Marker(TencentMap.addMarker(newLatLng, bitmap, rotateAngle));
            case MAP_SOURCE_BAIDU:
                return new Marker(BaiduMap.addMarker(newLatLng, bitmap, rotateAngle));
            case MAP_SOURCE_GOOGLE:
                return new Marker(GoogleMap.addMarker(newLatLng, bitmap, rotateAngle));
        }
        return null;
    }

    /**
     * 添加序号Marker
     *
     * @param latLng 坐标
     * @param num 序号
     * @return
     */
    public Marker addNumberMarker(LatLng latLng, int num) {
        return addNumberMarker(latLng, num, false);
    }

    /**
     * 添加序号Marker
     *
     * @param latLng 坐标
     * @param num 序号
     * @param isSelect 是否选中状态
     * @return
     */
    public Marker addNumberMarker(LatLng latLng, int num, boolean isSelect) {
        switch (currentMapSource) {
            case MAP_SOURCE_AMAP:
                return new Marker(AMap.addNumberMarker(latLng, num, isSelect));
            case MAP_SOURCE_TENCENT:
                return new Marker(TencentMap.addNumberMarker(latLng, num, isSelect));
            case MAP_SOURCE_BAIDU:
                return new Marker(BaiduMap.addNumberMarker(latLng, num, isSelect));
            case MAP_SOURCE_GOOGLE:
                return new Marker(GoogleMap.addNumberMarker(latLng, num, isSelect));
        }
        return null;
    }

    /**
     * 添加Polyline
     *
     * @param latLngs 参数变长
     * @return
     */
    public Polyline addPolyline(LatLng... latLngs) {
        return addPolyline(Color.RED, false, latLngs);
    }

    /**
     * 添加Polyline
     *
     * @param color   颜色
     * @param latLngs 参数变长
     * @return
     */
    public Polyline addPolyline(int color, boolean setDottedLine, LatLng... latLngs) {
        List<LatLng> latLngList = new ArrayList<>();
        if (latLngs != null) {
            latLngList.addAll(Arrays.asList(latLngs));
        }
        return addPolyline(latLngList, color, setDottedLine);
    }

    /**
     * 添加Polyline
     *
     * @param latLngList 坐标列表
     * @return
     */
    public Polyline addPolyline(List<LatLng> latLngList) {
        return addPolyline(latLngList, ContextCompat.getColor(getContext(), R.color.colorGray));
    }

    /**
     * 添加Polyline
     *
     * @param latLngList 坐标列表
     * @param color      颜色
     * @return
     */
    public Polyline addPolyline(List<LatLng> latLngList, int color) {
        return addPolyline(latLngList, color, 5, false);
    }

    /**
     * 添加Polyline
     *
     * @param latLngList 坐标列表
     * @param color      颜色
     * @return
     */
    public Polyline addPolyline(List<LatLng> latLngList, int color, boolean setDottedLine) {
        return addPolyline(latLngList, color, 5, setDottedLine);
    }

    /**
     * 添加Polyline
     *
     * @param latLngList 坐标列表
     * @return
     */
    public Polyline addPolyline(List<LatLng> latLngList, int color, int width, boolean setDottedLine) {

        if (latLngList == null) return null;

        //地图显示坐标系为火星坐标系，如传入的坐标类型为WGS84，则进行转换
        List<LatLng> newLatLngList = new ArrayList<>();
        for (int i = 0; i < latLngList.size(); i++) {
            LatLng latLng = latLngList.get(i);
            if (latLng.getType() != null && latLng.getType() == CoordinateSystemType.WGS84) {
                newLatLngList.add(LatLngConvertUtil.WGS84ToGCJ02(latLng));
            } else {
                newLatLngList.add(latLng);
            }
        }

        switch (currentMapSource) {
            case MAP_SOURCE_AMAP:
                return new Polyline(AMap.addPolyline(newLatLngList, color, width, setDottedLine));
            case MAP_SOURCE_TENCENT:
                return new Polyline(TencentMap.addPolyline(newLatLngList, color, width, setDottedLine));
            case MAP_SOURCE_BAIDU:
                return new Polyline(BaiduMap.addPolyline(newLatLngList, color, width, setDottedLine));
            case MAP_SOURCE_GOOGLE:
                return new Polyline(GoogleMap.addPolyline(newLatLngList, color, width, setDottedLine));
        }
        return null;
    }

    //切换Fragment
    private static void replaceFragment(Fragment fragment) {
        if (InitMapModule.getActivity() != null) {
            FragmentManager fragmentManager = InitMapModule.getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.map_view_framelayout, fragment);
            transaction.commit();
        } else {
            L.v("MapModule: replace fragment error, getActivity() is null");
        }
    }

    public static OnMapClickListener getOnMapClickListener() {
        return onMapClickListener;
    }

    public void setOnMapClickListener(OnMapClickListener l) {
        onMapClickListener = l;
    }

    //地图点击接口回调
    public interface OnMapClickListener {
        void onClick(LatLng latLng);
    }

    /**
     * 获取当前地图源
     * MAP_SOURCE_TENCENT = 1;
     * MAP_SOURCE_BAIDU = 2;
     * MAP_SOURCE_AMAP = 3;
     * MAP_SOURCE_GOOGLE = 4
     *
     * @return
     */
    public static int getCurrentMapSource() {
        return currentMapSource;
    }

    public static void setCurrentMapSource(int currentMapSource) {
        MapView.currentMapSource = currentMapSource;
    }

    public static int getCurrentMapType() {
        return currentMapType;
    }

    public static void setCurrentMapType(int currentMapType) {
        MapView.currentMapType = currentMapType;
    }

    public static LatLng getInitCamearLatLng() {
        return initCamearLatLng;
    }

    public static int getInitZoom() {
        return initZoom;
    }

    public static LocationListener getLocationListener() {
        return locationListener;
    }

    public static SwitchMapSourceListener getSwitchMapSourceListener() {
        return switchMapSourceListener;
    }
}
