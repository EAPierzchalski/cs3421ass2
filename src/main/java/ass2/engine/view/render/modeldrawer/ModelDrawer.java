package ass2.engine.view.render.modeldrawer;

import ass2.engine.model.components.Terrain;
import ass2.engine.model.shaderoptions.ShaderChoice;
import ass2.engine.view.render.modeldrawer.roadDrawer.RoadDrawer;
import ass2.engine.view.render.modeldrawer.terrainDrawer.TerrainDrawer;
import ass2.engine.view.render.modeldrawer.treeDrawer.TreeDrawer;
import ass2.engine.view.shaders.Program;
import ass2.engine.view.shaders.Shader;

import javax.media.opengl.GL2;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Pierzchalski
 * Date: 19/10/13
 * Package: ass2.engine.view.render
 * Project: cs3421ass2
 */
public class ModelDrawer {
    private List<ComponentDrawer> componentDrawers;

    public ModelDrawer(Terrain terrain) {
        this.componentDrawers = new ArrayList<ComponentDrawer>(3);
        this.componentDrawers.add(new TerrainDrawer(terrain));
        this.componentDrawers.add(new TreeDrawer(terrain));
        this.componentDrawers.add(new RoadDrawer(terrain));
    }

    public void init(GL2 gl) {
        for (ComponentDrawer componentDrawer : componentDrawers) {
            componentDrawer.init(gl);
        }
    }

    public void draw(GL2 gl) {
        for (ComponentDrawer componentDrawer : componentDrawers) {
            componentDrawer.draw(gl);
        }
    }
}
