package ass2.engine.controller.keyinput.mykeystroke;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * User: Pierzchalski
 * Date: 11/10/13
 * Package: ass2.util
 * Project: cs3421ass2
 */
public enum MyKeyStroke {

    UP(KeyEvent.VK_UP),
    DOWN(KeyEvent.VK_DOWN),
    LEFT(KeyEvent.VK_LEFT),
    RIGHT(KeyEvent.VK_RIGHT),

    SPACE(KeyEvent.VK_SPACE),

    SHIFT(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK),

    V(KeyEvent.VK_V),

    W(KeyEvent.VK_W),
    A(KeyEvent.VK_A),
    S(KeyEvent.VK_S),
    D(KeyEvent.VK_D);

    private MyKeyStroke(int keyEventCode) {
        PRESSED = javax.swing.KeyStroke.getKeyStroke(keyEventCode, 0, false);
        RELEASED = javax.swing.KeyStroke.getKeyStroke(keyEventCode, 0, true);
    }

    private MyKeyStroke(int keyEventCode, int modifiers) {
        PRESSED = javax.swing.KeyStroke.getKeyStroke(keyEventCode, modifiers, false);
        RELEASED = javax.swing.KeyStroke.getKeyStroke(keyEventCode, 0, true);
    }

    public javax.swing.KeyStroke PRESSED;
    public javax.swing.KeyStroke RELEASED;
}
