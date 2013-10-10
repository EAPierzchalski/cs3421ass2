package ass2.engine;

import ass2.engine.controller.GameController;
import ass2.engine.model.GameModel;
import ass2.engine.model.Terrain;
import ass2.engine.view.GameView;

import javax.media.opengl.awt.GLJPanel;

/**
 * User: Pierzchalski
 * Date: 08/10/13
 * Package: ass2.engine
 * Project: cs3421ass2
 */
public class GameEngine {
    private GameController gameController;
    private GameModel gameModel;
    private GameView gameView;

    public GameEngine(Terrain terrain) {
        this.gameModel = new GameModel(terrain);
        this.gameView = new GameView(gameModel);
        this.gameController = new GameController(gameModel);
    }

    public void linkTo(GLJPanel gljPanel) {
        gljPanel.addGLEventListener(gameView);

    }

    public void init() {

    }
}
