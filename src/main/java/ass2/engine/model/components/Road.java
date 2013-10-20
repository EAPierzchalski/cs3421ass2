package ass2.engine.model.components;

import java.util.ArrayList;
import java.util.List;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Double> myPoints;
    private double myWidth;

    private static final double LAST_POINT_TOLERANCE = 1e-6;
    
    /** 
     * Create a new road starting at the specified point
     */
    public Road(double width, double x0, double y0) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        myPoints.add(x0);
        myPoints.add(y0);
    }

    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(double width, double[] spine) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        for (int i = 0; i < spine.length; i++) {
            myPoints.add(spine[i]);
        }
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return myWidth;
    }

    /**
     * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
     * (x1, y1) and (x2, y2) are interpolated as bezier control points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
        myPoints.add(x1);
        myPoints.add(y1);
        myPoints.add(x2);
        myPoints.add(y2);
        myPoints.add(x3);
        myPoints.add(y3);        
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return myPoints.size() / 6;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public double[] controlPoint(int i) {
        double[] p = new double[2];
        p[0] = myPoints.get(i*2);
        p[1] = myPoints.get(i*2+1);
        return p;
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public double[] point(double t) {
        int i = (int)Math.floor(t);
        double bezierT = t - i;
        if (Math.abs(t - (double) size()) < LAST_POINT_TOLERANCE) {
            return new double[]{
                    myPoints.get(myPoints.size() - 2),
                    myPoints.get(myPoints.size() - 1)
            };
        }

        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i);
        
        double[] p = new double[2];

        p[0] = b(0, bezierT) * x0 + b(1, bezierT) * x1 + b(2, bezierT) * x2 + b(3, bezierT) * x3;
        p[1] = b(0, bezierT) * y0 + b(1, bezierT) * y1 + b(2, bezierT) * y2 + b(3, bezierT) * y3;
        
        return p;
    }

    /***
     * return the tangent vector to the spine at parameter t.
     * @param t
     * @return
     */
    public double[] tangent(double t) {
        int i = (int)Math.floor(t);
        double bezierT = t - i;
        if (Math.abs(t - (double) size()) < LAST_POINT_TOLERANCE) {
            double x2 = myPoints.get(myPoints.size() - 4);
            double y2 = myPoints.get(myPoints.size() - 3);
            double x3 = myPoints.get(myPoints.size() - 2);
            double y3 = myPoints.get(myPoints.size() - 1);
            double bx2 = x3 - x2;
            double by2 = y3 - y2;
            return new double[] {
                    3 * bx2,
                    3 * by2
            };
        }
        i *= 6;

        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i);

        double bx0 = x1 - x0;
        double by0 = y1 - y0;
        double bx1 = x2 - x1;
        double by1 = y2 - y1;
        double bx2 = x3 - x2;
        double by2 = y3 - y2;

        double[] tangent = new double[2];
        tangent[0] =
                bTangent(0, bezierT) * bx0 +
                bTangent(1, bezierT) * bx1 +
                bTangent(2, bezierT) * bx2;
        tangent[1] =
                bTangent(0, bezierT) * by0 +
                bTangent(1, bezierT) * by1 +
                bTangent(2, bezierT) * by2;
        return tangent;
    }
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private double b(int i, double t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }

    /***
     * Bezier coefficients for the tangents to the curve
     * @param p
     * @param t
     * @return
     */
    private double bTangent(int p, double t) {
        double bezierCoeff;
        switch (p) {
            case 0:
                bezierCoeff = 3 * (1 - t) * (1 - t);
                break;
            case 1:
                bezierCoeff = 6 * (1 - t) * t;
                break;
            case 2:
                bezierCoeff = 3 * t * t;
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(p));
        }
        return bezierCoeff;
    }
}
