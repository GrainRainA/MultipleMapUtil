package com.grain.map.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.grain.map.InitMapModule.getContext;

/**
 * @anthor GrainRain
 * @funcation 图片资源工具类
 * @date 2020/5/12
 */
public class DrawableUtils {

    /**
     * 图片资源转bitmap
     * @param res 图片资源ID
     * @return
     */
    public static Bitmap drawableToBitmap(int res) {

        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeResource(getContext().getResources(), res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
