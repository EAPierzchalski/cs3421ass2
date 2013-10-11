package ass2.engine.controller.keyinput.bindings;

import ass2.engine.controller.keyinput.KeyBinder;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * User: Pierzchalski
 * Date: 11/10/13
 * Package: ass2.engine.controller.keyinput.bindings
 * Project: cs3421ass2
 */
public class KeyReleasedAction extends AbstractAction {

    private GameAction gameAction;
    private KeyBinder keyBinder;

    public KeyReleasedAction(GameAction gameAction, KeyBinder keyBinder) {
        this.gameAction = gameAction;
        this.keyBinder = keyBinder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        keyBinder.cancelGameAction(gameAction);
    }
}
