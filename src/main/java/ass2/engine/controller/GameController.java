package ass2.engine.controller;

import ass2.engine.controller.keyinput.KeyBinder;
import ass2.engine.model.GameModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Pierzchalski
 * Date: 09/10/13
 * Package: ass2.engine
 * Project: cs3421ass2
 */
public class GameController implements ActionListener {
    private GameModel gameModel;
    private KeyBinder keyBinder;
    private Timer timer;
    private long myTime;

    private static final int MILLIS_PER_TICK = 20;

    public GameController(GameModel gameModel) {
        this.gameModel = gameModel;
        this.keyBinder = new KeyBinder(gameModel);
        this.timer = new Timer(MILLIS_PER_TICK, this);
        this.myTime = System.currentTimeMillis();
    }

    public void bindToPanel(JComponent jComponent) {
        jComponent.addMouseListener(Mouse.theMouse);
        jComponent.addMouseMotionListener(Mouse.theMouse);
        keyBinder.bindToPanel(jComponent);
    }

    public void start() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
        long newTime = System.currentTimeMillis();
        double dt = (newTime - myTime) / 1000.0;
        myTime = newTime;
        keyBinder.update(dt);
    }
}
