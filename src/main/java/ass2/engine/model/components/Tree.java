package ass2.engine.model.components;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree {

    private double[] myPos;

    private double myAngle;
    
    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
        myAngle = 2 * Math.PI * Math.random();
    }
    
    public double[] getPosition() {
        return myPos;
    }

    public double getMyAngle() {
        return myAngle;
    }
}
