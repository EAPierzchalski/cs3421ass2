package ass2.engine;

import ass2.engine.controller.GameController;
import ass2.engine.model.Terrain;
import ass2.engine.view.GameView;

/**
 * User: Pierzchalski
 * Date: 08/10/13
 * Package: ass2.engine
 * Project: cs3421ass2
 */
public class GameEngine {
    private GameController gameController;
    private Terrain terrain;
    private GameView gameView;

    public GameEngine(Terrain terrain) {
        this.terrain = terrain;
        this.gameView = new GameView(terrain);
        this.gameController = new GameController(terrain);
    }
}
