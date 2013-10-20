package ass2.engine.model;

import ass2.engine.model.components.Terrain;
import ass2.util.Util;

/**
 * Created with IntelliJ IDEA.
 * User: Edward
 * Date: 9/10/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameModel {

    private static final double DEFAULT_HEIGHT_ABOVE_TERRAIN = 1;
    private static final double ROTATION_SPEED = 90;
    private static final double MOVEMENT_SPEED = 2;

    private Terrain terrain;
    private double[] player2DPosition = new double[]{0, 0};
    private double[] playerLookDirection = new double[]{1, 0, 0};
    private double playerHeightAboveTerrain = DEFAULT_HEIGHT_ABOVE_TERRAIN;

    private static final float[] DAY_COLOR = new float[]{0.8f, 0.8f, 0.6f, 1};
    private static final float[] NIGHT_COLOR = new float[]{0.1f, 0.2f, 0.3f, 1};
    private static final double DAY_LENGTH_IN_SECONDS = 60;
    private double sunlightAngle = 0;

    private boolean usingShaders = true;
    private boolean usingFlashlight = true;
    private boolean usingDayNightCycle = true;

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

    public void jump(double dt) {
        playerHeightAboveTerrain += dt * MOVEMENT_SPEED;
    }

    public double[] getPlayer3DPosition() {
        return new double[] {
                player2DPosition[0],
                playerHeightAboveTerrain + terrain.altitude(player2DPosition[0], player2DPosition[1]),
                player2DPosition[1]
        };
    }

    public double[] getPlayerLookDirection() {
        return Util.copyArray(playerLookDirection);
    }

    public void setPlayerLookDirection(double[] playerLookDirection) {
        this.playerLookDirection = Util.normalize(playerLookDirection);
    }

    public void translatePlayerLookDirection(double[] translation) {
        this.playerLookDirection = Util.normalize(Util.sum(this.playerLookDirection, translation));
    }

    public void toggleUsingShaders() {
        usingShaders = !usingShaders;
    }

    public void toggleUsingFlashlight() {
        usingFlashlight = !usingFlashlight;
    }

    public void toggleUsingDayNightCycle(){
        usingDayNightCycle = !usingDayNightCycle;
    }

    public boolean isUsingShaders() {
        return usingShaders;
    }

    public boolean isUsingFlashlight() {
        return usingFlashlight;
    }

    public boolean isUsingDayNightCycle() {
        return usingDayNightCycle;
    }

    public double getSunlightAngle() {
        return sunlightAngle;
    }

    public void update(double dt) {
        sunlightAngle += 2 * Math.PI * dt / DAY_LENGTH_IN_SECONDS;
        sunlightAngle %= 2 * Math.PI;
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

    /***
     *
     * @return the direction of the sun depending on time of day. Rotated around z-axis.
     */
    public float[] getSunlightDirection() {
        if (isUsingDayNightCycle()) {
            float[] baseSunlightDirection = terrain.getSunlightDirection();
            float s = (float) Math.sin(sunlightAngle);
            float c = (float) Math.cos(sunlightAngle);
            return new float[] {
                    baseSunlightDirection[0] * c - baseSunlightDirection[1] * s,
                    baseSunlightDirection[0] * s + baseSunlightDirection[1] * c,
                    baseSunlightDirection[2]
            };
        } else {
            return terrain.getSunlightDirection();
        }
    }

    public float[] getSunlightColor() {
        if (isUsingDayNightCycle()) {
            float s2 = (float) Math.pow(Math.sin(sunlightAngle / 2), 2);
            float c2 = (float) Math.pow(Math.cos(sunlightAngle / 2), 2);
            float[] weightedDayColor = Util.scale(c2, DAY_COLOR);
            float[] weightedNightColor = Util.scale(s2, NIGHT_COLOR);
            return Util.sum(weightedDayColor, weightedNightColor);
        } else {
            return DAY_COLOR;
        }
    }
}
