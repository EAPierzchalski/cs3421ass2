package ass2.engine.model;

import ass2.util.Tuple2;
import ass2.util.Util;

/**
 * Created with IntelliJ IDEA.
 * User: Edward
 * Date: 9/10/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameModel {

    public static final double HEIGHT_ABOVE_TERRAIN = 1;

    private Terrain terrain;
    private double[] playerPosition = new double[]{0, 0};
    private double[] playerLookDirection = new double[]{1, 0, 0};

    public GameModel(Terrain terrain) {
        this.terrain = terrain;
    }

    public double[] getPlayerPosition() {
        return Util.copyArray(playerPosition);
    }

    public void setPlayerPosition(double[] playerPosition) {
        this.playerPosition = playerPosition;
    }

    public double[] getPlayerLookDirection() {
        return Util.copyArray(playerLookDirection);
    }

    public void setPlayerLookDirection(double[] playerLookDirection) {
        this.playerLookDirection = playerLookDirection;
    }
}
