package com.grain.map.Interfaces;

import com.grain.map.Entity.LatLng;

/**
 * @anthor GrainRain
 * @funcation 位置信息回调接口
 * @date 2021/1/25
 */
public interface LocationListener {

    void onReceiveLocation(LatLng latLng, float direction, float radius);
}
