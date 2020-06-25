package me.apex.hades.util;

import org.bukkit.util.Vector;

public class ReachUtil {
    private static final double HITBOX_SIZE = 0.3;

    private double x, y, z;

    private final double minX, maxX;
    private final double minZ, maxZ;

    private final long time = System.currentTimeMillis();

    public ReachUtil(double x, double y, double z) {
        this.x = x;
        minX = x - HITBOX_SIZE;
        maxX = x + HITBOX_SIZE;

        this.y = y;

        this.z = z;
        minZ = z - HITBOX_SIZE;
        maxZ = z + HITBOX_SIZE;

    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

    public double getX() {
        return this.x;
    }

    public double getZ() {
        return this.z;
    }


    public double getMinZ() {
        return this.minZ;
    }

    public double getMinX() {
        return this.minX;
    }

    public double getMaxZ() {
        return this.maxZ;
    }

    public double getMaxX() {
        return this.maxX;
    }
}
