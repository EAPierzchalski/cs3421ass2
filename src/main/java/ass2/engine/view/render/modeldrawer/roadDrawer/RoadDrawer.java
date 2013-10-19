package ass2.engine.view.render.modeldrawer.roadDrawer;

import ass2.engine.model.Road;
import ass2.engine.model.Terrain;
import ass2.engine.view.render.modeldrawer.ComponentDrawer;
import ass2.engine.view.textures.Texture;

import javax.media.opengl.GL2;

/**
 * User: Pierzchalski
 * Date: 19/10/13
 * Package: ass2.engine.view.render.modeldrawer.roadDrawer
 * Project: cs3421ass2
 */
public class RoadDrawer implements ComponentDrawer {
    private Terrain terrain;

    private static final String ROAD_TEXTURE_FILE_SRC = "src/main/resources/textures/BrightPurpleMarble.png";
    private static final String ROAD_TEXTURE_FILE_TYPE = "png";
    private Texture roadTexture;

    public RoadDrawer(Terrain terrain) {
        this.terrain = terrain;
    }

    public void init(GL2 gl) {
        roadTexture = new Texture(gl, ROAD_TEXTURE_FILE_SRC, ROAD_TEXTURE_FILE_TYPE);
    }

    public void draw(GL2 gl) {
        for (Road road : terrain.roads()) {
            drawRoad(gl, road);
        }
    }

    private void drawRoad(GL2 gl, Road road) {

    }
}
