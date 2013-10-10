package ass2.engine.controller.keyinput.bindings;

import ass2.engine.model.GameModel;
import ass2.util.KeyStrokes;

import javax.swing.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Edward
 * Date: 9/09/13
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */

public enum GameAction {
    MOVE_FORWARD(KeyStrokes.UP, KeyStrokes.W) {
        @Override
        public void doActionOn(GameModel gameModel, double dt) {
            gameModel.moveForward(dt);
        }
    },

    TURN_LEFT(KeyStrokes.A, KeyStrokes.LEFT) {
        @Override
        public void doActionOn(GameModel gameModel, double dt) {
            gameModel.rotatePlayerLookDirection(-dt);
        }
    },

    TURN_RIGHT(KeyStrokes.D, KeyStrokes.RIGHT) {
        @Override
        public void doActionOn(GameModel gameModel, double dt) {
            gameModel.rotatePlayerLookDirection(dt);
        }
    },

    MOVE_BACK(KeyStrokes.S, KeyStrokes.DOWN) {
        @Override
        public void doActionOn(GameModel gameModel, double dt) {
            gameModel.moveForward(-dt);
        }
    };

    private Set<KeyStroke> triggeringKeyStrokes;

    private GameAction(KeyStroke... triggeringKeyStrokes) {
        this.triggeringKeyStrokes = new HashSet<KeyStroke>();
        Collections.addAll(this.triggeringKeyStrokes, triggeringKeyStrokes);
    }

    public Set<KeyStroke> getTriggeringKeyStrokes() {
        return triggeringKeyStrokes;
    }

    public abstract void doActionOn(GameModel gameModel, double dt);
}
