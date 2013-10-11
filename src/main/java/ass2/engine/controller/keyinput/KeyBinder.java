package ass2.engine.controller.keyinput;

import ass2.engine.controller.keyinput.bindings.GameAction;
import ass2.engine.controller.keyinput.bindings.KeyPressedAction;
import ass2.engine.controller.keyinput.bindings.KeyReleasedAction;
import ass2.engine.model.GameModel;
import ass2.util.MyKeyStroke;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Edward
 * Date: 10/10/13
 * Time: 7:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class KeyBinder {
    private GameModel gameModel;
    private Set<GameAction> triggeredGameActions = new HashSet<GameAction>();

    public KeyBinder(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void registerGameAction(GameAction gameAction) {
        triggeredGameActions.add(gameAction);
    }

    public void cancelGameAction(GameAction gameAction) {
        triggeredGameActions.remove(gameAction);
    }

    public void update(double dt) {
        for (GameAction gameAction : triggeredGameActions) {
            gameAction.doActionOn(gameModel, dt);
        }
    }

    public void bindToPanel(JComponent jComponent) {
        for (GameAction gameAction : GameAction.values()) {
            for (MyKeyStroke triggeringKeyStroke : gameAction.getTriggeringKeyStrokes()) {
                jComponent.getInputMap().put(triggeringKeyStroke.PRESSED, gameAction + "PRESSED");
                jComponent.getInputMap().put(triggeringKeyStroke.RELEASED, gameAction + "RELEASED");
            }
            KeyPressedAction keyPressedAction = new KeyPressedAction(gameAction, this);
            jComponent.getActionMap().put(gameAction + "PRESSED", keyPressedAction);
            KeyReleasedAction keyReleasedAction = new KeyReleasedAction(gameAction, this);
            jComponent.getActionMap().put(gameAction + "RELEASED", keyReleasedAction);
        }
    }

}
