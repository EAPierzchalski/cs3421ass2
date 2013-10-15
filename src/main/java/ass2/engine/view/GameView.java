package ass2.engine.view;

import ass2.engine.controller.Mouse;
import ass2.engine.model.GameModel;
import ass2.engine.view.render.DrawUtil;
import ass2.engine.view.render.TerrainDrawer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * User: Pierzchalski
 * Date: 09/10/13
 * Package: ass2.engine
 * Project: cs3421ass2
 */
public class GameView implements GLEventListener {

    private GameModel gameModel;
    private Camera camera;
    private TerrainDrawer terrainDrawer;

    private static final double[] BACKGROUND_COLOR = new double[]{0, 0, 0, 1};
    private static final float[] SUNLIGHT_DIFFUSE_COLOR = new float[]{1f, 1f, 1f, 1};
    private static final float[] SUNLIGHT_AMBIENT_COLOR = new float[]{0.1f, 0.1f, 0.1f, 1};

    public GameView(GameModel gameModel) {
        this.gameModel = gameModel;
        this.camera = new Camera(gameModel.getPlayer3DPosition(), gameModel.getPlayerLookDirection());
        this.terrainDrawer = new TerrainDrawer(gameModel.getTerrain());
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glEnable(GL.GL_DEPTH_TEST);

        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, SUNLIGHT_DIFFUSE_COLOR, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, SUNLIGHT_AMBIENT_COLOR, 0);

        gl.glEnable(GL2.GL_NORMALIZE);

        gl.glShadeModel(GL2.GL_SMOOTH);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        //does nothing yet
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        updateCamera();
        camera.setView(gl);
        draw(gl);
    }

    private void updateCamera() {
        camera.setPosition(gameModel.getPlayer3DPosition());
        camera.setLookDirection(gameModel.getPlayerLookDirection());
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        camera.reshape(gl, x, y, width, height);
        Mouse.theMouse.reshape(gl);
    }

    private void draw(GL2 gl) {
        gl.glColor4dv(BACKGROUND_COLOR, 0);
        gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
        gl.glPushMatrix(); {
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, getSunlightPositionFloat(), 0);
            //DrawUtil.drawAxes(gl);
            //drawSunlight(gl);
            terrainDrawer.drawTerrain(gl);
        } gl.glPopMatrix();
        gl.glFlush();
    }

    private float[] getSunlightPositionFloat() {
        float[] sunlight = gameModel.getTerrain().getSunlightDirection();
        for (int i = 0; i < sunlight.length; i++) {
            sunlight[i] *= -1;
        }
        return sunlight;
    }

    private double[] getSunlightPositionDouble() {
        float[] sunlight = gameModel.getTerrain().getSunlightDirection();
        double[] sunlightd = new double[sunlight.length];
        for (int i = 0; i < sunlightd.length; i++) {
            sunlightd[i] = sunlight[i] * -1;
        }
        return sunlightd;
    }

    private static final float[] YELLOW = new float[]{1, 1, 0, 1};
    private void drawSunlight(GL2 gl) {
        DrawUtil.drawLine(gl, new double[]{0, 0, 0}, getSunlightPositionDouble(), YELLOW);
    }
}
