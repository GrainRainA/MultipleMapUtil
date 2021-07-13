package com.grain.map.Entity;

import com.grain.map.Common.CoordinateSystemType;

/**
 * @anthor GrainRain
 * @funcation 自定义坐标类 WGS84
 * @date 2020/5/11
 */
public class LatLng {

    public double latitude;
    public double longitude;
    public CoordinateSystemType type;

    private LatLng(Builder builder) {
        setLatitude(builder.latitude);
        setLongitude(builder.longitude);
        setType(builder.type);
    }

    @Override
    public String toString() {
        return "LatLng{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", type=" + type +
                '}';
    }

    public LatLng() {
    }

    /**
     * 坐标类
     * @param latitude 纬度
     * @param longitude 经度
     */
    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @param latitude 纬度
     * @param longitude 经度
     * @param type 坐标系统类型
     */
    public LatLng(double latitude, double longitude, CoordinateSystemType type) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public CoordinateSystemType getType() {
        return type;
    }

    public void setType(CoordinateSystemType type) {
        this.type = type;
    }

    public static final class Builder {
        private double latitude;
        private double longitude;
        private CoordinateSystemType type;

        public Builder() {
        }

        public Builder latitude(double val) {
            latitude = val;
            return this;
        }

        public Builder longitude(double val) {
            longitude = val;
            return this;
        }

        public Builder type(CoordinateSystemType val) {
            type = val;
            return this;
        }

        public LatLng build() {
            return new LatLng(this);
        }
    }
}