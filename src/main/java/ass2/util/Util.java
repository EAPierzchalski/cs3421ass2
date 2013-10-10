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
}
