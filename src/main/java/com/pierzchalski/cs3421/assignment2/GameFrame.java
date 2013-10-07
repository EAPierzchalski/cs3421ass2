package com.pierzchalski.cs3421.assignment2;

import com.jogamp.opengl.util.FPSAnimator;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.*;
import java.awt.*;

/**
 * User: Pierzchalski
 * Date: 07/10/13
 * Package: com.pierzchalski.cs3421.assignment2
 * Project: cs3421ass2
 */
public class GameFrame extends JFrame {

    public static GameFrame gameFrame = new GameFrame();

    private GameFrame() {
        super("CS3421 Assignment 1");
    }

    public void init() {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        glCapabilities.setSampleBuffers(true);
        glCapabilities.setNumSamples(4);

        GLJPanel gamePanel = new GLJPanel(glCapabilities);

        FPSAnimator fpsAnimator = new FPSAnimator(gamePanel, 60);
        fpsAnimator.start();

        this.getContentPane().add(gamePanel, BorderLayout.CENTER);
        this.setSize(1024, 768);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
