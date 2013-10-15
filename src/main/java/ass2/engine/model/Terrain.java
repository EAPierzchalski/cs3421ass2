package ass2.engine.model;

import ass2.util.Tuple2;
import ass2.util.Util;

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
        double maxQuadX = mySize.width - 1;
        double maxQuadZ = mySize.height - 1;
        int gridX = (int) Math.floor(x);
        int gridZ = (int) Math.floor(z);
        if (0 <= gridX && gridX <= maxQuadX && 0 <= gridZ && gridZ <= maxQuadZ) {
            double intraQuadX = x - Math.floor(x);
            double intraQuadZ = z - Math.floor(z);
            Direction direction;
            if (intraQuadZ <= intraQuadX) {
                if (intraQuadZ <= 0.5 - intraQuadX) {
                    direction = Direction.SOUTH;
                } else {
                    direction = Direction.EAST;
                }
            } else {
                if (intraQuadZ <= 0.5 - intraQuadX) {
                    direction = Direction.WEST;
                } else {
                    direction = Direction.NORTH;
                }
            }
            altitude = quadQuarterAltitude(gridX, gridZ, intraQuadX, intraQuadZ, direction);
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
