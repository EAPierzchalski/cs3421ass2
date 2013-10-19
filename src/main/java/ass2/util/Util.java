package ass2.util;

/**
 * Created with IntelliJ IDEA.
 * User: Edward
 * Date: 9/10/13
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class Util {
    public static double[] copyArray(double[] src) {
        double[] dest = new double[src.length];
        System.arraycopy(src, 0, dest, 0, src.length);
        return dest;
    }

    public static float[] copyArray(float[] src) {
        float[] dest = new float[src.length];
        System.arraycopy(src, 0, dest, 0, src.length);
        return dest;
    }

    public static double[] add(double[] v1, double[] v2) {
        double[] sum = new double[v1.length];
        for (int i = 0; i < v1.length; i++) {
            sum[i] = v1[i] + v2[i];
        }
        return sum;
    }

    public static double[] normalize(double[] v) {
        double norm = 0;
        for (double x : v) {
            norm += Math.pow(x, 2);
        }
        norm = Math.pow(norm, 0.5);
        double[] normalizedV = new double[v.length];
        for (int i = 0; i < normalizedV.length; i++) {
            normalizedV[i] = v[i]/norm;
        }
        return normalizedV;
    }

    public static double[] cross(double[] v1, double[] v2) {
        double[] crossV = new double[v1.length];
        crossV[0] = v1[1] * v2[2] - v1[2] * v2[1];
        crossV[1] = v1[2] * v2[0] - v1[0] * v2[2];
        crossV[2] = v1[0] * v2[1] - v1[1] * v2[0];
        return crossV;
    }

    /**
     *
     * @param v1
     * @param v2
     * @return v1 - v2
     */
    public static double[] sub(double[] v1, double[] v2) {
        double[] v1subV2 = new double[v1.length];
        for (int i = 0; i < v1subV2.length; i++) {
            v1subV2[i] = v1[i] - v2[i];
        }
        return v1subV2;
    }

    public static double[] sum(double[] v1, double[] v2) {
        double[] v1addV2 = new double[v1.length];
        for (int i = 0; i < v1addV2.length; i++) {
            v1addV2[i] = v1[i] + v2[i];
        }
        return v1addV2;
    }

    public static double[] scale(double s, double[] v) {
        double[] sv = new double[v.length];
        for (int i = 0; i < v.length; i++) {
            sv[i] = s * v[i];
        }
        return sv;
    }

    public static double[][][] flatten(double[][][][] allDoubles) {
        int flattenedLength = 0;
        for (double[][][] firstLayer : allDoubles) {
            flattenedLength += firstLayer.length;
        }
        double[][][] flattenedDoubles = new double[flattenedLength][][];
        int flattenedIndex = 0;
        for (double[][][] firstLayer : allDoubles) {
            for (double[][] secondLayer : firstLayer) {
                flattenedDoubles[flattenedIndex] = secondLayer;
                flattenedIndex++;
            }
        }
        return flattenedDoubles;
    }

    public static double[][][] join(double[][][] doubles1, double[][][] doubles2) {
        double[][][] joinedDoubles = new double[doubles1.length + doubles2.length][][];
        System.arraycopy(doubles1, 0, joinedDoubles, 0, doubles1.length);
        System.arraycopy(doubles2, 0, joinedDoubles, doubles1.length, doubles2.length);
        return joinedDoubles;
    }
}
