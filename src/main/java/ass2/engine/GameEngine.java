package ass2.engine;

import ass2.engine.controller.GameController;
import ass2.engine.model.GameModel;
import ass2.engine.model.Terrain;
import ass2.engine.view.GameView;
import com.jogamp.opengl.util.FPSAnimator;

import javax.media.opengl.awt.GLJPanel;

/**
 * User: Pierzchalski
 * Date: 08/10/13
 * Package: ass2.engine
 * Project: cs3421ass2
 */
public class GameEngine {
    private FPSAnimator fpsAnimator;

    private static final int FRAMES_PER_SECOND = 60;

    public GameEngine(Terrain terrain, GLJPanel gljPanel) {
        GameModel gameModel = new GameModel(terrain);
        GameView gameView = new GameView(gameModel);
        GameController gameController = new GameController(gameModel);
        this.fpsAnimator = new FPSAnimator(gljPanel, FRAMES_PER_SECOND);
        gljPanel.addGLEventListener(gameView);
        gljPanel.addGLEventListener(gameController);
        gameController.bindToPanel(gljPanel);
    }

    public void start() {
        this.fpsAnimator.start();
    }
}
