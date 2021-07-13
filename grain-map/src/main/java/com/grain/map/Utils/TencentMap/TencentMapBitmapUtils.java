package com.grain.map.Utils.TencentMap;

import android.graphics.Bitmap;

import com.grain.map.Utils.DrawableUtils;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;

/**
 * @anthor GrainRain
 * @funcation 腾讯地图bitmap工具类
 * @date 2020/5/12
 */
public class TencentMapBitmapUtils {

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
