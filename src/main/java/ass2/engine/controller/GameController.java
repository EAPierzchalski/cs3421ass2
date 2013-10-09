package ass2.engine.controller;

import ass2.engine.model.GameModel;

import javax.swing.*;

/**
 * User: Pierzchalski
 * Date: 09/10/13
 * Package: ass2.engine
 * Project: cs3421ass2
 */
public class GameController {
    private GameModel gameModel;

    public GameController(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void bindToPanel(JComponent jComponent) {
        jComponent.addMouseListener(Mouse.theMouse);
        jComponent.addMouseMotionListener(Mouse.theMouse);
    }
}
