package me.apex.hades.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MathUtils {

    public static long gcd(long a, long b) {
        return b <= 0x4000 ? a : gcd(b, a % b);
    }

    public static long getGcd(long current, long previous) {
        return (double) previous <= 16384.0D ? current : getGcd(previous, (long) Math.abs(current - previous));
    }

    public static long lcd(long a, long b) {
        return a * (b / absGCD(a, b));
    }

    public static boolean isBetween(double a, double b, double c) {
        return a >= b && a <= c;
    }

    public static boolean isRoughlyEqual(double d1, double d2, double seperator) {
        return Math.abs(d1 - d2) < seperator; //0.001, 0.125, 0.35
    }

    public static long elapsed(long num){ return System.currentTimeMillis() - num; }

    private static long absGCD(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    public static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(0, RoundingMode.UP);
        return bd.doubleValue();
    }

    public static double normalize(double val, double min, double max) {
        if (max < min) return 0;
        return (val - min) / (max - min);
    }

    public static float sqrt_float(float value) {
        return (float) Math.sqrt((double) value);
    }

    public static float sqrt_double(double value) {
        return (float) Math.sqrt(value);
    }

    public static double getDistance3D(Location a, Location b) {
        double xSqr = (b.getX() - a.getX()) * (b.getX() - a.getX());
        double ySqr = (b.getY() - a.getY()) * (b.getY() - a.getY());
        double zSqr = (b.getZ() - a.getZ()) * (b.getZ() - a.getZ());
        double sqrt = Math.sqrt(xSqr + ySqr + zSqr);
        return Math.abs(sqrt);
    }

    //Client Mathematics
    private static final float[] SIN_TABLE = new float[65536];

    /**
     * sin looked up in a table
     */
    public static float sin(float p_76126_0_) {
        return SIN_TABLE[(int) (p_76126_0_ * 10430.378F) & 65535];
    }

    /**
     * cos looked up in the sin table with the appropriate offset
     */
    public static float cos(float value) {
        return SIN_TABLE[(int) (value * 10430.378F + 16384.0F) & 65535];
    }

    public static float getDistanceXZToEntity(Entity entity1, Entity entityIn) {
        org.bukkit.Location p = entity1.getLocation();
        org.bukkit.Location e = entityIn.getLocation();
        float f = (float) (p.getX() - e.getX());
        float f1 = (float) (p.getZ() - e.getZ());
        return Math.abs(MathUtils.sqrt_float(f * f + f1 * f1));
    }

    public static double pingFormula(long ping) {
        return Math.ceil((ping + 5) / 50.0D);
    }

    public static double getDirection(Location from, Location to) {
        if (from == null || to == null) {
            return 0.0D;
        }
        double difX = to.getX() - from.getX();
        double difZ = to.getZ() - from.getZ();

        return (float) ((Math.atan2(difZ, difX) * 180.0D / Math.PI) - 90.0F);
    }

    public static float wrapAngleTo180_float(float value) {
        value %= 360.0F;

        if (value >= 180.0F) {
            value -= 360.0F;
        }

        if (value < -180.0F) {
            value += 360.0F;
        }

        return value;
    }

    public static float fixRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
        float var4 = wrapAngleTo180_float(p_70663_2_ - p_70663_1_);

        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }

        if (var4 < -p_70663_3_) {
            var4 = -p_70663_3_;
        }

        return p_70663_1_ + var4;
    }

    public static double getDistanceBetweenAngles360(double angle1, double angle2) {
        double distance = Math.abs(angle1 % 360.0 - angle2 % 360.0);
        distance = Math.min(360.0 - distance, distance);
        return Math.abs(distance);
    }

    public static double average(List<Double> values) {
        double avg = 0.0D;
        for (Double value : values)
            avg += value;
        return avg / values.size();
    }

    public static double addAll(List<Double> values) {
        double total = 0.0D;
        for (Double value : values)
            total += value;
        return total;
    }

    public static double getStandardDeviation(long numberArray[]) {
        double sum = 0.0, deviation = 0.0;
        int length = numberArray.length;
        for (double num : numberArray)
            sum += num;
        double mean = sum / length;
        for (double num : numberArray)
            deviation += Math.pow(num - mean, 2);

        return Math.sqrt(deviation / length);
    }

    public static double getStandardDeviation(double numberArray[]) {
        double sum = 0.0, deviation = 0.0;
        int length = numberArray.length;
        for (double num : numberArray)
            sum += num;
        double mean = sum / length;
        for (double num : numberArray)
            deviation += Math.pow(num - mean, 2);

        return Math.sqrt(deviation / length);
    }

    public static float[] getRotationFromPosition(Player player, double x, double z, double y) {
        double xDiff = x - player.getLocation().getX();
        double zDiff = z - player.getLocation().getZ();
        double yDiff = y - player.getLocation().getY() - 1.2;

        double dist = sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
        return new float[]{yaw, pitch > 90 ? 90 : pitch < -90 ? -90 : pitch};
    }

    /*public static double getDistToHitbox(Entity entity1, Entity entity2, BoundingBox targetBox) {
        double offset = Math.abs((targetBox.maxX - targetBox.minX) * (targetBox.maxZ - targetBox.minZ));
        double dist = Math.abs((entity1.getLocation().clone().toVector().setY(0.0D).distance(entity2.getLocation().clone().toVector().setY(0.0D))) - (offset % 3.0) - 1);
        return dist;
    }

    public static double getVerticalDistToHitbox(Entity entity1, Entity entity2, BoundingBox targetBox) {
        double offset = Math.abs(targetBox.maxY - targetBox.minY) / 2D;
        double dist = Math.abs((entity1.getLocation().getY() - entity2.getLocation().getY()) - (offset % 3.0) - 1);
        return dist;
    }*/

}
