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

    public static String doubleArray2String(double[] da) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ ");
        for (double d : da) {
            stringBuilder.append(d);
            stringBuilder.append(" ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
