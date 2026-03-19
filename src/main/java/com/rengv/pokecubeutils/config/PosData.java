package com.rengv.pokecubeutils.config;

public class PosData {
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;

    private PosData() {}

    public PosData(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
