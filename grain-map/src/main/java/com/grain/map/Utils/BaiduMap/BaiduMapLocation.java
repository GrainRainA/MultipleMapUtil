package com.grain.map.Utils.BaiduMap;

import android.app.Service;
import android.os.Vibrator;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.grain.map.Common.CoordinateSystemType;
import com.grain.map.Entity.LatLng;
import com.grain.map.MapView;
import com.grain.utils.Interfaces.DirectionSensorListener;
import com.grain.utils.hint.L;
import com.grain.utils.sensor.DirectionSensor;

import static com.grain.map.InitMapModule.getActivity;

/**
 * @anthor GrainRain
 * @funcation 百度地图定位功能
 * @date 2021/1/25
 */
public class BaiduMapLocation {

    private BaiduMapLocationService locationService;
    private Vibrator mVibrator;

    private static LatLng oldLatLng;
    private static float oldDirection;
    private static float oldRadius;

    public BaiduMapLocation() {
        //全局只初始化一个locationService
        locationService = new BaiduMapLocationService(getActivity());
        locationService.registerListener(listener);
        mVibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);

        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK

        //方向传感器回调
        new DirectionSensor(new DirectionSensorListener() {
            @Override
            public void onSensorChanged(float x, float y, float z) {
                if(oldLatLng != null && Math.abs(oldDirection - x) > 1) {
                    MapView.getLocationListener().onReceiveLocation(oldLatLng, Math.abs(x - 360), oldRadius);
                    oldDirection = x;
                }
            }
        });
    }

    private BDAbstractLocationListener listener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (null != location && location.getLocType() != BDLocation.TypeServerError) {

                StringBuffer sb = new StringBuffer(256);

                sb.append(location.getLatitude());
                sb.append(location.getLongitude());
                sb.append(location.getRadius());// 半径

                if(MapView.getLocationListener() != null) {

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude(), CoordinateSystemType.GCJ02);

                    if(latLng.latitude > 10 && latLng.latitude != 0 && latLng.longitude > 10 && latLng.longitude != 0) {
                        if(oldLatLng != null) {
                            if(oldLatLng.latitude != latLng.latitude || oldLatLng.longitude != latLng.longitude) {

                                MapView.getLocationListener().onReceiveLocation(latLng, oldDirection, location.getRadius());
                                oldLatLng = latLng;
                                oldRadius = location.getRadius();
                            }
                        } else {
                            oldLatLng = latLng;
                            oldRadius = location.getRadius();
                        }
                    }
                }

//                sb.append(location.getProvince()); // 获取省份
//                sb.append(location.getCountry());   // 国家名称
//                sb.append(location.getCity()); // 城市
//                sb.append(location.getDistrict());// 区
//                sb.append(location.getTown());// 获取镇信息
//                sb.append(location.getStreet());// 街道
//                sb.append(location.getAddrStr());// 地址信息
//                sb.append(location.getStreetNumber());// 获取街道号码
//                sb.append(location.getUserIndoorState());   //用户室内外判断结果
//                sb.append(location.getDirection());     // 方向
//
//                sb.append(locationService.getSDKVersion()); // 获取SDK版本
//                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                    sb.append("\nspeed : ");
//                    sb.append(location.getSpeed());// 速度 单位：km/h
//                    sb.append("\nsatellite : ");
//                    sb.append(location.getSatelliteNumber());// 卫星数目
//                    sb.append("\nheight : ");
//                    sb.append(location.getAltitude());// 海拔高度 单位：米
//                    sb.append("\ngps status : ");
//                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
//                    sb.append("\ndescribe : ");
//                    sb.append("gps定位成功");
//                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                    // 运营商信息
//                    if (location.hasAltitude()) {// *****如果有海拔高度*****
//                        sb.append("\nheight : ");
//                        sb.append(location.getAltitude());// 单位：米
//                    }
//                    sb.append("\noperationers : ");// 运营商信息
//                    sb.append(location.getOperators());
//                    sb.append("\ndescribe : ");
//                    sb.append("网络定位成功");
//                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                    sb.append("\ndescribe : ");
//                    sb.append("离线定位成功，离线定位结果也是有效的");
//                } else if (location.getLocType() == BDLocation.TypeServerError) {
//                    sb.append("\ndescribe : ");
//                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                    sb.append("\ndescribe : ");
//                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
//                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                    sb.append("\ndescribe : ");
//                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            super.onConnectHotSpotMessage(s, i);
        }

        /**
         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
         * @param locType 当前定位类型
         * @param diagnosticType 诊断类型（1~9）
         * @param diagnosticMessage 具体的诊断信息释义
         */
        @Override
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
            super.onLocDiagnosticMessage(locType, diagnosticType, diagnosticMessage);
            int tag = 2;
            StringBuffer sb = new StringBuffer(256);
            sb.append("诊断结果: ");
            if (locType == BDLocation.TypeNetWorkLocation) {
                if (diagnosticType == 1) {
                    sb.append("网络定位成功，没有开启GPS，建议打开GPS会更好");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 2) {
                    sb.append("网络定位成功，没有开启Wi-Fi，建议打开Wi-Fi会更好");
                    sb.append("\n" + diagnosticMessage);
                }
            } else if (locType == BDLocation.TypeOffLineLocationFail) {
                if (diagnosticType == 3) {
                    sb.append("定位失败，请您检查您的网络状态");
                    sb.append("\n" + diagnosticMessage);
                }
            } else if (locType == BDLocation.TypeCriteriaException) {
                if (diagnosticType == 4) {
                    sb.append("定位失败，无法获取任何有效定位依据");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 5) {
                    sb.append("定位失败，无法获取有效定位依据，请检查运营商网络或者Wi-Fi网络是否正常开启，尝试重新请求定位");
                    sb.append(diagnosticMessage);
                } else if (diagnosticType == 6) {
                    sb.append("定位失败，无法获取有效定位依据，请尝试插入一张sim卡或打开Wi-Fi重试");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 7) {
                    sb.append("定位失败，飞行模式下无法获取有效定位依据，请关闭飞行模式重试");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 9) {
                    sb.append("定位失败，无法获取任何有效定位依据");
                    sb.append("\n" + diagnosticMessage);
                }
            } else if (locType == BDLocation.TypeServerError) {
                if (diagnosticType == 8) {
                    sb.append("定位失败，请确认您定位的开关打开状态，是否赋予APP定位权限");
                    sb.append("\n" + diagnosticMessage);
                }
            }
            L.v(sb.toString());
        }
    };
}
