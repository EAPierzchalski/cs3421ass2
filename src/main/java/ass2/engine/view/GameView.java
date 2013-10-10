package ass2.engine.view;

import ass2.engine.model.GameModel;

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

    public GameView(GameModel gameModel) {
        this.gameModel = gameModel;
        this.camera = new Camera(gameModel.getPlayer2DPosition(), gameModel.getPlayerLookDirection());
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        //To change body of implemented methods use File | Settings | File Templates.
        GL2 gl = glAutoDrawable.getGL().getGL2();
        updateCamera();
        camera.setView(gl);
        draw(gl);
    }

    private void updateCamera() {
        double[] playerPosition = gameModel.getPlayer2DPosition();
        double[] newCameraPosition = new double[]{
                playerPosition[0],
                gameModel.getTerrain().altitude(playerPosition[0], playerPosition[1]),
                playerPosition[1]
        };
        camera.setPosition(newCameraPosition);
        camera.setLookDirection(gameModel.getPlayerLookDirection());
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        //To change body of implemented methods use File | Settings | File Templates.
        GL2 gl = glAutoDrawable.getGL().getGL2();
        camera.reshape(gl, x, y, width, height);
    }

    private static final double[][] TEST_POLYGON = new double[][]{

    }

    private void draw(GL2 gl) {
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL); {

        }
    }
}
