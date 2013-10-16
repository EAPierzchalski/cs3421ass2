package ass2.engine.model;

import ass2.util.Tuple2;
import ass2.util.Util;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myGridAltitude;
    private double[][] myCentreAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlightDirection;

    public Terrain(
            Dimension mySize,
            double[][] myGridAltitude,
            List<Tuple2<Double, Double>> treePositions,
            List<Road> myRoads,
            float[] mySunlightDirection) {
        this.mySize = mySize;
        this.myGridAltitude = myGridAltitude;
        this.myCentreAltitude = centreAltitudes(mySize, myGridAltitude);
        this.myTrees = treesFrom2DPositions(treePositions);
        this.myRoads = myRoads;
        this.mySunlightDirection = mySunlightDirection;
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlightDirection() {
        return Util.copyArray(mySunlightDirection);
    }

    /**
     * Get the altitude at a grid point
     *
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myGridAltitude[x][z];
    }

    /**
     * Get the altitude at an arbitrary point.
     * Non-integer points should be interpolated from neighbouring grid points
     *
     * TO BE COMPLETED
     *
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {
        double altitude;
        int gridX = (int) Math.floor(x);
        int gridZ = (int) Math.floor(z);
        if (0 <= gridX && gridX < mySize.width - 1 && 0 <= gridZ && gridZ < mySize.height - 1) {

            Direction direction = quadQuarter(x, z);

            double[][] cornerAltitudes = new double[2][2];

            int quadCornerX = direction.quadCorners[0][0];
            int quadCornerZ = direction.quadCorners[0][1];
            int gridCornerX = gridX + quadCornerX;
            int gridCornerZ = gridZ + quadCornerZ;
            cornerAltitudes[quadCornerX][quadCornerZ] = myGridAltitude[gridCornerX][gridCornerZ];

            quadCornerX = direction.quadCorners[1][0];
            quadCornerZ = direction.quadCorners[1][1];
            gridCornerX = gridX + quadCornerX;
            gridCornerZ = gridZ + quadCornerZ;
            cornerAltitudes[quadCornerX][quadCornerZ] = myGridAltitude[gridCornerX][gridCornerZ];

            double altitudeAtCentre = myCentreAltitude[gridX][gridZ];

            /**
             * We use the change in altitude to determine the values at all four corners of the quad,
             * as though it were a rectangle with the same planar normal as that of the triangle.
             */
            switch (direction) {
                case NORTH:

            }

            double intraQuadX = x - Math.floor(x);
            double intraQuadZ = z - Math.floor(z);


        } else {
            altitude = 0;
        }
        return altitude;
    }

    private static double[][] centreAltitudes(Dimension dimensions, double[][] gridAltitudes) {
        int maxQuadX = dimensions.width - 1;
        int maxQuadZ = dimensions.height - 1;
        double[][] centreAltitudes = new double[maxQuadX][maxQuadZ];
        for (int x = 0; x < maxQuadX; x++) {
            for (int z = 0; z < maxQuadZ; z++) {
                double centreAltitude = 0;
                for (Direction direction : Direction.values()) {
                    int cornerX = x + direction.quadCorners[0][0];
                    int cornerZ = z + direction.quadCorners[0][1];
                    centreAltitude += gridAltitudes[cornerX][cornerZ];
                }
                centreAltitude /= Direction.values().length;
                centreAltitudes[x][z] = centreAltitude;
            }
        }
        return centreAltitudes;
    }

    /***
     * Returns the bilinear interpolation at x, z in a unit square with corner values given as arguments.
     * @param x0z0
     * @param x1z0
     * @param x0z1
     * @param x1z1
     * @param x
     * @param z
     * @return
     */
    private double bilinear(double x0z0, double x1z0, double x0z1, double x1z1, double x, double z) {
        double lerpX0 = (1 - x) * x0z0 + x * x1z0;
        double lerpX1 = (1 - x) * x0z1 + x * x1z1;
        return (1 - z) * lerpX0 + z * lerpX1;
    }

    private Direction quadQuarter(double x, double z) {
        double intraQuadX = x - Math.floor(x);
        double intraQuadZ = z - Math.floor(z);
        Direction direction;
        if (intraQuadZ >= intraQuadX) {
            if (intraQuadZ >= 1 - intraQuadX) {
                direction = Direction.NORTH;
            } else {
                direction = Direction.WEST;
            }
        } else {
            if (intraQuadZ >= 1 - intraQuadX) {
                direction = Direction.EAST;
            } else {
                direction = Direction.SOUTH;
            }
        }
        return direction;
    }

    private double quadQuarterAltitude(
            int gridX,
            int gridZ,
            double intraQuadX,
            double intraQuadZ,
            Direction direction) {
        double altitude = 0;

        //System.out.println(direction);

        return altitude;
    }

    private List<Tree> treesFrom2DPositions(List<Tuple2<Double, Double>> treePositions) {
        List<Tree> trees = new ArrayList<Tree>(treePositions.size());
        for (Tuple2<Double, Double> position : treePositions) {
            double x = position.get1();
            double z = position.get2();
            double y = altitude(x, z);
            trees.add(new Tree(x, y, z));
        }
        return trees;
    }

}
