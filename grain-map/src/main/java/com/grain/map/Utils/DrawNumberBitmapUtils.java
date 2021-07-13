package com.grain.map.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.grain.map.InitMapModule;
import com.grain.map.R;

import androidx.core.content.ContextCompat;

/**
 * @anthor GrainRain
 * @funcation 绘制序号图标
 * @date 2019/11/29
 */
public class DrawNumberBitmapUtils {

    public static Bitmap getNumberBitmap(int iconSize, int number) {
        return getNumberBitmap(iconSize, number, false);
    }

    public static Bitmap getNumberBitmap(int iconSize, int number, boolean isSelect) {
        return getNumberBitmap(iconSize, iconSize / 10, number, isSelect);
    }

    public static Bitmap getNumberBitmap(int iconSize, int padding, int number, boolean isSelect) {
        Bitmap bitmap = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        RectF rect = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        // 绘制白色背景
        paint.setColor(Color.WHITE);
        canvas.drawOval(rect, paint);
        // 绘制内圈背景
        paint.setColor(isSelect ? getColor(R.color.colorBlue) : getColor(R.color.colorWhite));
        canvas.drawOval(new RectF(padding, padding, rect.width() - padding, rect.height() - padding), paint);
        // 绘制文字
        paint.setColor(isSelect ? getColor(R.color.colorWhite) : getColor(R.color.colorBlue));
        paint.setStrokeWidth(3);
        paint.setTextSize(iconSize * 0.7f);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        float baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(String.valueOf(number), rect.centerX(), baseline, paint);
        return bitmap;
    }

    private static int getColor(int resId) {
        return ContextCompat.getColor(InitMapModule.getContext(), resId);
    }
}
