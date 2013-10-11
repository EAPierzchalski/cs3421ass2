package ass2.engine.controller.keyinput.bindings;

import ass2.engine.model.GameModel;
import ass2.util.MyKeyStroke;

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
    MOVE_FORWARD(MyKeyStroke.UP, MyKeyStroke.W) {
        @Override
        public void doActionOn(GameModel gameModel, double dt) {
            gameModel.moveForward(dt);
        }
    },

    TURN_LEFT(MyKeyStroke.A, MyKeyStroke.LEFT) {
        @Override
        public void doActionOn(GameModel gameModel, double dt) {
            gameModel.rotatePlayerLookDirection(-dt);
        }
    },

    TURN_RIGHT(MyKeyStroke.D, MyKeyStroke.RIGHT) {
        @Override
        public void doActionOn(GameModel gameModel, double dt) {
            gameModel.rotatePlayerLookDirection(dt);
        }
    },

    MOVE_BACK(MyKeyStroke.S, MyKeyStroke.DOWN) {
        @Override
        public void doActionOn(GameModel gameModel, double dt) {
            gameModel.moveForward(-dt);
        }
    };

    private Set<MyKeyStroke> triggeringKeyStrokes;

    private GameAction(MyKeyStroke... triggeringKeyStrokes) {
        this.triggeringKeyStrokes = new HashSet<MyKeyStroke>();
        Collections.addAll(this.triggeringKeyStrokes, triggeringKeyStrokes);
    }

    public Set<MyKeyStroke> getTriggeringKeyStrokes() {
        return triggeringKeyStrokes;
    }

    public abstract void doActionOn(GameModel gameModel, double dt);
}
