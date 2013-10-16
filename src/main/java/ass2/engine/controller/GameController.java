package ass2.engine.controller;

import ass2.engine.controller.keyinput.KeyBinder;
import ass2.engine.controller.mouseinput.Mouse;
import ass2.engine.model.GameModel;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.swing.*;

/**
 * User: Pierzchalski
 * Date: 09/10/13
 * Package: ass2.engine
 * Project: cs3421ass2
 */
public class GameController implements GLEventListener {
    private KeyBinder keyBinder;
    private long myTime;
    private Mouse myMouse;

    public GameController(GameModel gameModel) {
        this.keyBinder = new KeyBinder(gameModel);
        this.myMouse = new Mouse(gameModel);
        this.myTime = System.currentTimeMillis();
    }

    public void bindToPanel(JComponent jComponent) {
        jComponent.addMouseListener(myMouse);
        jComponent.addMouseMotionListener(myMouse);
        keyBinder.bindToPanel(jComponent);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        this.myTime = System.currentTimeMillis();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        //Does nothing
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        long newTime = System.currentTimeMillis();
        double dt = (newTime - myTime) / 1000.0;
        myTime = newTime;
        keyBinder.update(dt);
        myMouse.update(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        myMouse.reshape(gl);
    }
}
