package ass2;

import ass2.engine.GameEngine;
import ass2.engine.model.components.Terrain;
import ass2.util.LevelIO;
import org.json.JSONException;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game {

    private Terrain myTerrain;

    public Game(Terrain terrain) {
        myTerrain = terrain;
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        glCapabilities.setSampleBuffers(true);
        glCapabilities.setNumSamples(4);

        GLJPanel gamePanel = new GLJPanel(glCapabilities);
        JFrame gameFrame = new JFrame("3D Game");

        GameEngine gameEngine = new GameEngine(myTerrain, gamePanel);

        gameEngine.start();

        gameFrame.getContentPane().add(gamePanel, BorderLayout.CENTER);
        gameFrame.setSize(1024, 768);
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);

        gamePanel.requestFocusInWindow();
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, JSONException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }
}
