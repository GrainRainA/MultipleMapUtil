package com.grain.map.Common;

/**
 * @anthor GrainRain
 * @funcation 坐标系统类型
 * @date 2021/4/15
 */
public enum CoordinateSystemType {

    WGS84(0, "GPS坐标"),
    GCJ02(1, "火星坐标");

    private int value;
    private String description;

    CoordinateSystemType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
