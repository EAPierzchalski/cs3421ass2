package ass2.util;

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

    W(KeyEvent.VK_W),
    A(KeyEvent.VK_A),
    S(KeyEvent.VK_S),
    D(KeyEvent.VK_D);

    private MyKeyStroke(int keyEventCode) {
        PRESSED = javax.swing.KeyStroke.getKeyStroke(keyEventCode, 0, false);
        RELEASED = javax.swing.KeyStroke.getKeyStroke(keyEventCode, 0, true);
    }

    public javax.swing.KeyStroke PRESSED;
    public javax.swing.KeyStroke RELEASED;
}
