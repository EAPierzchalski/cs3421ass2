package ass2.engine.view.render.modeldrawer;

import ass2.engine.model.Terrain;
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
    private static final String VERTEX_SHADER_SRC = "src/main/resources/shaders/vertex/simple_vertex.glsl";
    private static final String FRAGMENT_SHADER_SRC = "src/main/resources/shaders/fragment/simple_fragment.glsl";
    private Shader vertexShader = new Shader(GL2.GL_VERTEX_SHADER, new File(VERTEX_SHADER_SRC));
    private Shader fragmentShader = new Shader(GL2.GL_FRAGMENT_SHADER, new File(FRAGMENT_SHADER_SRC));
    private Program shaderProgram;

    private List<ComponentDrawer> componentDrawers;

    public ModelDrawer(Terrain terrain) {
        this.componentDrawers = new ArrayList<ComponentDrawer>(3);
        this.componentDrawers.add(new TerrainDrawer(terrain));
        this.componentDrawers.add(new TreeDrawer(terrain));
        this.componentDrawers.add(new RoadDrawer(terrain));
    }

    public void init(GL2 gl) {
        this.vertexShader.compile(gl);
        this.fragmentShader.compile(gl);
        this.shaderProgram = new Program(gl, vertexShader, fragmentShader);
        for (ComponentDrawer componentDrawer : componentDrawers) {
            componentDrawer.init(gl);
        }
    }

    public void draw(GL2 gl, boolean useShaders) {
        if (useShaders) {
            gl.glUseProgram(shaderProgram.getMyID());
        } else {
            gl.glUseProgram(0);
        }
        for (ComponentDrawer componentDrawer : componentDrawers) {
            componentDrawer.draw(gl);
        }
    }
}
