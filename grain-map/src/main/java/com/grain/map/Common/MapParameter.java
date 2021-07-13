package com.grain.map.Common;

import com.grain.map.Entity.LatLng;

/**
 * @anthor GrainRain
 * @funcation 初始化地图参数
 * @date 2021/4/15
 */
public class MapParameter {

    /**
     * 地图源
     */
    public int mapSource;

    /**
     * 地图类型
     */
    public int mapType;

    /**
     * 缩放级别
     */
    public int zoom;

    /**
     * 中心的坐标
     */
    public LatLng camearLatLng;

    private MapParameter(Builder builder) {
        mapSource = builder.mapSource;
        mapType = builder.mapType;
        zoom = builder.zoom;
        camearLatLng = builder.camearLatLng;
    }

    public static final class Builder {
        private int mapSource;
        private int mapType;
        private int zoom;
        private LatLng camearLatLng;

        public Builder() {
        }

        public Builder mapSource(int val) {
            mapSource = val;
            return this;
        }

        public Builder mapType(int val) {
            mapType = val;
            return this;
        }

        public Builder zoom(int val) {
            zoom = val;
            return this;
        }

        public Builder camearLatLng(LatLng val) {
            camearLatLng = val;
            return this;
        }

        public MapParameter build() {
            return new MapParameter(this);
        }
    }
}
