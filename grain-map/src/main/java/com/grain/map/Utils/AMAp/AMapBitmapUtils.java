package com.grain.map.Utils.AMAp;


import android.graphics.Bitmap;

import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.grain.map.Utils.DrawableUtils;

/**
 * @anthor GrainRain
 * @funcation 高德地图bitmap工具类
 * @date 2020/5/12
 */
public class AMapBitmapUtils {

    /**
     * 图片资源转BitmapDescriptor
     * @param res
     * @return
     */
    public static BitmapDescriptor drawableToBitmapDescriptor(int res) {

        Bitmap bitmap = DrawableUtils.drawableToBitmap(res);
        BitmapDescriptor des = null;

        if(bitmap != null) {
            des = BitmapDescriptorFactory.fromBitmap(bitmap);
        }

        return des;
    }

    /**
     * bitmap转BitmapDescriptor
     * @param bitmap
     * @return
     */
    public static BitmapDescriptor bitmapToBitmapDescriptor(Bitmap bitmap) {

        BitmapDescriptor des = null;

        if(bitmap != null) {
            des = BitmapDescriptorFactory.fromBitmap(bitmap);
        }

        return des;
    }
}
