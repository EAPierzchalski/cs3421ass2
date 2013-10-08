package ass2.spec;

import org.json.JSONException;

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
