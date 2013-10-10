package ass2.engine.controller.keyinput;

import ass2.engine.controller.keyinput.bindings.GameAction;
import ass2.engine.controller.keyinput.bindings.KeyAction;
import ass2.engine.model.GameModel;

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

    public void update(double dt) {
        for (GameAction gameAction : triggeredGameActions) {
            gameAction.doActionOn(gameModel, dt);
        }
        triggeredGameActions.clear();
    }

    public void bindToPanel(JComponent jComponent) {
        for (GameAction gameAction : GameAction.values()) {
            for (KeyStroke triggeringKeyStroke : gameAction.getTriggeringKeyStrokes()) {
                jComponent.getInputMap().put(triggeringKeyStroke, gameAction);
            }
            KeyAction keyAction = new KeyAction(gameAction, this);
            jComponent.getActionMap().put(gameAction, keyAction);
        }
    }

}
