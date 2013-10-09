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
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;

    public Terrain(
            Dimension mySize,
            double[][] myAltitude,
            List<Tuple2<Double, Double>> treePositions,
            List<Road> myRoads,
            float[] mySunlight) {
        this.mySize = mySize;
        this.myAltitude = myAltitude;
        this.myTrees = treesFrom2DPositions(treePositions);
        this.myRoads = myRoads;
        this.mySunlight = mySunlight;
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

    public float[] getSunlight() {
        return Util.copyArray(mySunlight);
    }

    /**
     * Get the altitude at a grid point
     *
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
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
        double altitude = 0;


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
