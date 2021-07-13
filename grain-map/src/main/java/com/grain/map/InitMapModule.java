package com.grain.map;

import android.content.Context;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.grain.map.Utils.LocationUtil;
import com.grain.utils.InitUtilsModule;

import androidx.appcompat.app.AppCompatActivity;


/**
 * @anthor GrainRain
 * @funcation 初始化moudle
 * @date 2020/5/11
 */
public class InitMapModule {

    private static AppCompatActivity activity;
    private static Context context;

    public static void init(AppCompatActivity mActivity) {
        activity = mActivity;
        context = activity.getApplicationContext();
        InitUtilsModule.init(activity);
//        new LocationUtil();

        //初始化百度地图
        SDKInitializer.initialize(context);

        //百度地图坐标设置为 GCJ02
        SDKInitializer.setCoordType(CoordType.GCJ02);
    }

    public static AppCompatActivity getActivity() {
        return activity;
    }

    public static Context getContext() {
        return context;
    }

}
