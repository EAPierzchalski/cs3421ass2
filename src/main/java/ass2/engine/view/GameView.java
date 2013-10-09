package ass2.engine.view;

import ass2.engine.model.GameModel;

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
    private Camera camera = new Camera();

    public GameView(GameModel gameModel) {
        this.gameModel = gameModel;
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
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
