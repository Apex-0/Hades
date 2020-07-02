package me.apex.hades.util;

import me.apex.hades.check.impl.combat.ml.model.LabeledData;
import net.minecraft.util.org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MathUtil {

    //Client Mathematics
    private static final float[] SIN_TABLE = new float[65536];
    private static final int[] multiplyDeBruijnBitPosition;
    private static final double field_181163_d;
    private static final double[] field_181164_e;
    private static final double[] field_181165_f;
    public static final double GCD_OFFSET;

    static {
        for (int i = 0; i < 65536; ++i) {
            SIN_TABLE[i] = (float) Math.sin((double) i * Math.PI * 2.0D / 65536.0D);
        }

        multiplyDeBruijnBitPosition = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
        field_181163_d = Double.longBitsToDouble(4805340802404319232L);
        field_181164_e = new double[257];
        field_181165_f = new double[257];
        GCD_OFFSET = Math.pow(2, 24);

        for (int j = 0; j < 257; ++j) {
            double d0 = (double) j / 256.0D;
            double d1 = Math.asin(d0);
            field_181165_f[j] = Math.cos(d1);
            field_181164_e[j] = d1;
        }
    }

    public static long gcd(long a, long b) {
        return b <= 0x4000 ? a : gcd(b, a % b);
    }

    public static long elapsed(long num) {
        return System.currentTimeMillis() - num;
    }

    public static long getGcd(long current, long previous) {
        return (double) previous <= 16384.0D ? current : getGcd(previous, Math.abs(current - previous));
    }

    public static long lcd(long a, long b) {
        long value = 0;
        try {
            value = a * (b / absGCD(a, b));
        } catch (Exception e) {

        }
        return value;
    }

    public static boolean isBetween(double a, double b, double c) {
        return a >= b && a <= c;
    }

    public static boolean isRoughlyEqual(double d1, double d2, double seperator) {
        return Math.abs(d1 - d2) < seperator; //0.001, 0.125, 0.35
    }

    public static float getAngleDiff(float a, float b) {
        float diff = Math.abs(a - b);
        float altDiff = b + 360 - a;
        float altAltDiff = a + 360 - b;
        if(altDiff < diff) diff = altDiff;
        if(altAltDiff < diff) diff = altAltDiff;
        return diff;
    }

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

    public static float sqrt_float(float value) {
        return (float) Math.sqrt(value);
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
        return Math.abs(MathUtil.sqrt_float(f * f + f1 * f1));
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

    public static double wrapAngleTo180_double(double value) {
        value = value % 360.0D;

        if (value >= 180.0D) {
            value -= 360.0D;
        }

        if (value < -180.0D) {
            value += 360.0D;
        }

        return value;
    }

    public static double func_181159_b(double p_181159_0_, double p_181159_2_) {
        double d0 = p_181159_2_ * p_181159_2_ + p_181159_0_ * p_181159_0_;

        if (Double.isNaN(d0)) {
            return Double.NaN;
        } else {
            boolean flag = p_181159_0_ < 0.0D;

            if (flag) {
                p_181159_0_ = -p_181159_0_;
            }

            boolean flag1 = p_181159_2_ < 0.0D;

            if (flag1) {
                p_181159_2_ = -p_181159_2_;
            }

            boolean flag2 = p_181159_0_ > p_181159_2_;

            if (flag2) {
                double d1 = p_181159_2_;
                p_181159_2_ = p_181159_0_;
                p_181159_0_ = d1;
            }

            double d9 = func_181161_i(d0);
            p_181159_2_ = p_181159_2_ * d9;
            p_181159_0_ = p_181159_0_ * d9;
            double d2 = field_181163_d + p_181159_0_;
            int i = (int) Double.doubleToRawLongBits(d2);
            double d3 = field_181164_e[i];
            double d4 = field_181165_f[i];
            double d5 = d2 - field_181163_d;
            double d6 = p_181159_0_ * d4 - p_181159_2_ * d5;
            double d7 = (6.0D + d6 * d6) * d6 * 0.16666666666666666D;
            double d8 = d3 + d7;

            if (flag2) {
                d8 = (Math.PI / 2D) - d8;
            }

            if (flag1) {
                d8 = Math.PI - d8;
            }

            if (flag) {
                d8 = -d8;
            }

            return d8;
        }
    }

    public static double func_181161_i(double p_181161_0_) {
        double d0 = 0.5D * p_181161_0_;
        long i = Double.doubleToRawLongBits(p_181161_0_);
        i = 6910469410427058090L - (i >> 1);
        p_181161_0_ = Double.longBitsToDouble(i);
        p_181161_0_ = p_181161_0_ * (1.5D - d0 * p_181161_0_ * p_181161_0_);
        return p_181161_0_;
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

    public static double getStandardDeviation(long[] numberArray) {
        double sum = 0.0, deviation = 0.0;
        int length = numberArray.length;
        for (double num : numberArray)
            sum += num;
        double mean = sum / length;
        for (double num : numberArray)
            deviation += Math.pow(num - mean, 2);

        return Math.sqrt(deviation / length);
    }

    public static double getStandardDeviation(double[] numberArray) {
        double sum = 0.0, deviation = 0.0;
        int length = numberArray.length;
        for (double num : numberArray)
            sum += num;
        double mean = sum / length;
        for (double num : numberArray)
            deviation += Math.pow(num - mean, 2);

        return Math.sqrt(deviation / length);
    }

    public static double getStandardDeviation(List<Double> numberArray) {
        double sum = 0.0, deviation = 0.0;
        int length = numberArray.size();
        for (double num : numberArray)
            sum += num;
        double mean = sum / length;
        for (double num : numberArray)
            deviation += Math.pow(num - mean, 2);

        return Math.sqrt(deviation / length);
    }

    private float getMouseDelta(float rotation) {
        return ((float) Math.cbrt((rotation / 0.01875F)) - 0.2F) / 0.6F;
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

    public static int floor(double var0) {
        int var2 = (int) var0;
        return var0 < var2 ? var2 - 1 : var2;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd2 = new BigDecimal(value);
        bd2 = bd2.setScale(places, RoundingMode.HALF_UP);
        return bd2.doubleValue();
    }

    public static double hypot(double... values) {
        return Math.sqrt(MathUtil.hypotSquared(values));
    }

    public static double hypotSquared(double... values) {
        double total = 0.0;
        double[] var1 = values;
        int var2 = values.length;
        for (int var3 = 0; var3 < var2; ++var3) {
            double value = var1[var3];
            total += Math.pow(value, 2.0);
        }
        return total;
    }

    public static double trim(int degree, double d) {
        String format = "#.#";
        for (int i = 1; i < degree; ++i) {
            format = format + "#";
        }
        DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.parseDouble(twoDForm.format(d).replaceAll(",", "."));
    }

    public static float getBaseSpeed(Player player) {
        return 0.34f + (PlayerUtil.getPotionEffectLevel(player, PotionEffectType.SPEED) * 0.062f) + ((player.getWalkSpeed() - 0.2f) * 1.6f);
    }

    public static double clamp180(double theta) {
        if ((theta %= 360.0) >= 180.0) {
            theta -= 360.0;
        }
        if (theta < -180.0) {
            theta += 360.0;
        }
        return theta;
    }

    @SuppressWarnings("unused")
    public static double[] extractFeatures(List<Float> angleSequence) {
        List<Double> anglesDouble = toDoubleList(angleSequence);
        List<Double> anglesDoubleDelta = calculateDelta(anglesDouble);

        double featureA = stddev(anglesDouble);
        double featureB = mean(anglesDouble);
        double featureC = stddev(anglesDoubleDelta);
        double featureD = mean(anglesDoubleDelta);

        return new double[]{featureA, featureB, featureC, featureD};
    }

    // Get delta of a double list
    public static List<Double> calculateDelta(List<Double> doubleList) {
        if (doubleList.size() <= 1) {
            throw new IllegalArgumentException(
                    "The list must contain 2 or more elements in order to calculate delta"
            );
        }

        List<Double> out = new ArrayList<>();
        for (int i = 1; i <= doubleList.size() - 1; i++)
            out.add(doubleList.get(i) - doubleList.get(i - 1));
        return out;
    }

    // Convert a float list to a double list
    public static List<Double> toDoubleList(List<Float> floatList) {
        return floatList.stream().map(e -> (double) e).collect(Collectors.toList());
    }

    // Get mean average of a double sequence
    public static double mean(List<Double> angles) {
        return angles.stream().mapToDouble(e -> e).sum() / angles.size();
    }

    // Get standard deviation of a double sequence
    public static double stddev(List<Double> angles) {
        double mean = mean(angles);
        double output = 0;
        for (double angle : angles)
            output += Math.pow(angle - mean, 2);
        return output / angles.size();
    }

    // Get euclidean distance of two vector
    public static double euclideanDistance(double[] vectorA, double[] vectorB) {
        validateDimension("Two vectors need to have exact the same dimension", vectorA, vectorB);

        double dist = 0;
        for (int i = 0; i <= vectorA.length - 1; i++)
            dist += Math.pow(vectorA[i] - vectorB[i], 2);
        return Math.sqrt(dist);
    }

    // Convert a double array to a double list
    public static List<Double> toList(double[] doubleArray) {
        return Arrays.asList(ArrayUtils.toObject(doubleArray));
    }

    // Convert a double list to a double array
    public static double[] toArray(List<Double> doubleList) {
        return doubleList.stream().mapToDouble(e -> e).toArray();
    }

    // generate a double array filled with random values from 0 to 1
    public static double[] randomArray(int length) {
        double[] randomArray = new double[length];
        applyFunc(randomArray, e -> e = ThreadLocalRandom.current().nextDouble());
        return randomArray;
    }

    // apply function on a array
    public static void applyFunc(double[] doubleArray, Function<Double, Double> func) {
        for (int i = 0; i <= doubleArray.length - 1; i++)
            doubleArray[i] = func.apply(doubleArray[i]);
    }

    // add two vector together
    public static double[] add(double[] vectorA, double[] vectorB) {
        validateDimension("Two vectors need to have exact the same dimension", vectorA, vectorB);

        double[] output = new double[vectorA.length];
        for (int i = 0; i <= vectorA.length - 1; i++)
            output[i] = vectorA[i] + vectorB[i];
        return output;
    }

    // Get diff of two different vectors (subtract)
    public static double[] subtract(double[] vectorA, double[] vectorB) {
        validateDimension("Two vectors need to have exact the same dimension", vectorA, vectorB);
        return add(vectorA, opposite(vectorB));
    }

    // get opposite numbers of elements in the vector
    public static double[] opposite(double[] vector) {
        return multiply(vector, -1);
    }

    // multiply all elements in the vector with a value
    public static double[] multiply(double[] vector, double factor) {
        double[] output = vector.clone();
        applyFunc(output, e -> e * factor);
        return output;
    }

    // normalize dataset with feature scaling
    // return: double[row number][0 = min value in this row, 1 = max value in this row]
    public static double[][] normalize(List<LabeledData> dataset) {
        validateDimension("Data in dataset have inconsistent features",
                dataset.stream().map(LabeledData::getData).toArray(double[][]::new));

        int dimension = dataset.get(0).getData().length;
        double[][] minMax = new double[dimension][2];

        for (int row = 0; row <= dimension - 1; row++) {
            int rowCurrent = row;
            double min = Collections.min(dataset.stream()
                    .map(data -> data.getData()[rowCurrent]).collect(Collectors.toList()));
            double max = Collections.max(dataset.stream()
                    .map(data -> data.getData()[rowCurrent]).collect(Collectors.toList()));
            minMax[row] = new double[]{min, max};
            for (int i = 0; i <= dataset.size() - 1; i++) {
                double originalValue = dataset.get(i).getData()[row];
                dataset.get(i).setData(row, (originalValue - min) / (max - min));
            }
        }
        return minMax;
    }

    // normalize a value with feature scaling according to the given min and max
    public static double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    // round a value with given arguments
    public static double round(double value, int precision, RoundingMode mode) {
        return BigDecimal.valueOf(value).round(new MathContext(precision, mode)).doubleValue();
    }

    @SuppressWarnings("SameParameterValue")
    private static void validateDimension(String message, double[]... vectors) {
        for (int i = 0; i <= vectors.length - 1; i++) {
            if (vectors[0].length != vectors[i].length)
                throw new IllegalArgumentException(message);
        }
    }

}
