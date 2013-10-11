package ass2.engine.model;

import ass2.util.Util;

/**
 * Created with IntelliJ IDEA.
 * User: Edward
 * Date: 9/10/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameModel {

    private static final double HEIGHT_ABOVE_TERRAIN = 1;
    private static final double ROTATION_SPEED = 90;
    private static final double MOVEMENT_SPEED = 2;

    private Terrain terrain;
    private double[] player2DPosition = new double[]{0, 0};
    private double[] playerLookDirection = new double[]{1, 0, 0};

    public GameModel(Terrain terrain) {
        this.terrain = terrain;
    }

    public double[] getPlayer2DPosition() {
        return Util.copyArray(player2DPosition);
    }

    public void setPlayer2DPosition(double[] player2DPosition) {
        this.player2DPosition = player2DPosition;
    }

    public void translatePlayer2DPosition(double x, double z) {
        this.player2DPosition[0] += x;
        this.player2DPosition[1] += z;
    }

    public void moveForward(double dt) {
        double dDistance = dt * MOVEMENT_SPEED;
        translatePlayer2DPosition(playerLookDirection[0] * dDistance, playerLookDirection[2] * dDistance);
    }

    public double[] getPlayer3DPosition() {
        return new double[] {
                player2DPosition[0],
                HEIGHT_ABOVE_TERRAIN + terrain.altitude(player2DPosition[0], player2DPosition[1]),
                player2DPosition[1]
        };
    }

    public double[] getPlayerLookDirection() {
        return Util.copyArray(playerLookDirection);
    }

    public void setPlayerLookDirection(double[] playerLookDirection) {
        this.playerLookDirection = Util.normalize(playerLookDirection);
    }

    public void rotatePlayerLookDirection(double dt) {
        double radians = Math.toRadians(dt * ROTATION_SPEED);
        double s = Math.sin(radians);
        double c = Math.cos(radians);
        playerLookDirection = new double[]{
                playerLookDirection[0] * c - playerLookDirection[2] * s,
                playerLookDirection[1],
                playerLookDirection[0] * s + playerLookDirection[2] * c
        };
    }

    public Terrain getTerrain() {
        return terrain;
    }
}
