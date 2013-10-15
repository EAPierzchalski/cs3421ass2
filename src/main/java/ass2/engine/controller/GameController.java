package ass2.engine.controller;

import ass2.engine.controller.keyinput.KeyBinder;
import ass2.engine.model.GameModel;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * User: Pierzchalski
 * Date: 09/10/13
 * Package: ass2.engine
 * Project: cs3421ass2
 */
public class GameController implements GLEventListener {
    private KeyBinder keyBinder;
    private long myTime;

    public GameController(GameModel gameModel) {
        this.keyBinder = new KeyBinder(gameModel);
    }

    public void bindToPanel(JComponent jComponent) {
        jComponent.addMouseListener(Mouse.theMouse);
        jComponent.addMouseMotionListener(Mouse.theMouse);
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
        Mouse.theMouse.update(gl);
        System.out.println(Arrays.toString(Mouse.theMouse.getPosition()));
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        Mouse.theMouse.reshape(gl);
    }
}
