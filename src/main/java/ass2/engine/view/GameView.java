package ass2.engine.view;

import ass2.engine.model.GameModel;
import ass2.engine.model.shaderoptions.ShaderChoice;
import ass2.engine.view.render.DrawUtil;
import ass2.engine.view.render.modeldrawer.ModelDrawer;
import ass2.engine.view.shaders.Program;
import ass2.util.Util;

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
    private ModelDrawer modelDrawer;

    private static final float[] AMBIENT_COLOR = new float[]{0.1f, 0.1f, 0.1f, 1};
    private static final float[] FLASHLIGHT_COLOR_ON = Util.scale(40, AMBIENT_COLOR);
    private static final float[] FLASHLIGHT_COLOR_OFF = new float[]{0, 0, 0, 1};

    private static final long START_TIME_MILLIS = System.currentTimeMillis();
    private int glslTimeLoc;

    public GameView(GameModel gameModel) {
        this.gameModel = gameModel;
        this.camera = new Camera(gameModel.getPlayer3DPosition(), gameModel.getPlayerLookDirection());
        this.modelDrawer = new ModelDrawer(gameModel.getTerrain());
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glEnable(GL.GL_DEPTH_TEST);

        gl.glEnable(GL2.GL_LIGHTING);

        gl.glEnable(GL2.GL_LIGHT0);

        gl.glEnable(GL2.GL_LIGHT1);
        gl.glLightf (GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, 30);
        gl.glLightf (GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, 5);
        gl.glLightf (GL2.GL_LIGHT1, GL2.GL_CONSTANT_ATTENUATION, 1);
        gl.glLightf (GL2.GL_LIGHT1, GL2.GL_LINEAR_ATTENUATION, 1);
        gl.glLightf (GL2.GL_LIGHT1, GL2.GL_QUADRATIC_ATTENUATION, 1);

        gl.glEnable(GL2.GL_NORMALIZE);

        gl.glShadeModel(GL2.GL_SMOOTH);

        gl.glEnable(GL2.GL_TEXTURE_2D);

        gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);

        ShaderChoice.init(gl);
        glslTimeLoc = gl.glGetUniformLocation(ShaderChoice.NPR.getProgram().getMyID(), "time");
        modelDrawer.init(gl);
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
    }

    private void draw(GL2 gl) {
        gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
        gl.glPushMatrix(); {
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, getSunlightPosition(), 0);
            setupSunlight(gl);
            setupFlashlight(gl);

            DrawUtil.drawAxes(gl);
            drawSunlightVector(gl);

            ShaderChoice shaderChoice = gameModel.getShaderChoice();
            if (shaderChoice == ShaderChoice.NONE) {
                gl.glUseProgram(0);
            } else {
                Program program = shaderChoice.getProgram();
                gl.glUseProgram(program.getMyID());
                if (shaderChoice == ShaderChoice.PHONG) {
                    gl.glUniform1i(gl.glGetUniformLocation(program.getMyID(), "texture_sampler"), 0);
                    gl.glUniform1i(gl.glGetUniformLocation(program.getMyID(), "normal_sampler"), 1);
                }
            }
            modelDrawer.draw(gl);
        } gl.glPopMatrix();
        gl.glFlush();
    }

    private float[] getSunlightPosition() {
        float[] sunlight = gameModel.getSunlightDirection();
        for (int i = 0; i < sunlight.length; i++) {
            sunlight[i] *= -1;
        }
        return sunlight;
    }

    private static final float[] YELLOW = new float[]{1, 1, 0, 1};
    private void drawSunlightVector(GL2 gl) {
        DrawUtil.drawLine(gl, new float[]{0, 0, 0}, getSunlightPosition(), YELLOW);
    }

    private void setupFlashlight(GL2 gl) {
        if (gameModel.isUsingFlashlight()) {
            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, FLASHLIGHT_COLOR_ON, 0);
            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, getPlayer3DPosition4f(), 0);
            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, getPlayerLookDirection3f(), 0);
        } else {
            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, FLASHLIGHT_COLOR_OFF, 0);
        }
    }

    private void setupSunlight(GL2 gl) {
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, gameModel.getSunlightColor(), 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, AMBIENT_COLOR, 0);
    }

    private float[] getPlayer3DPosition4f() {
        double[] playerPositiond = gameModel.getPlayer3DPosition();
        float[] playerPositionf = new float[playerPositiond.length + 1];
        for (int i = 0; i < playerPositiond.length; i++) {
            playerPositionf[i] = (float) playerPositiond[i];
        }
        playerPositionf[playerPositionf.length - 1] = 1;
        return playerPositionf;
    }

    private float[] getPlayerLookDirection3f() {
        double[] playerLookDirectiond = gameModel.getPlayerLookDirection();
        float[] playerPositionf = new float[playerLookDirectiond.length];
        for (int i = 0; i < playerLookDirectiond.length; i++) {
            playerPositionf[i] = (float) playerLookDirectiond[i];
        }
        return playerPositionf;
    }
}
