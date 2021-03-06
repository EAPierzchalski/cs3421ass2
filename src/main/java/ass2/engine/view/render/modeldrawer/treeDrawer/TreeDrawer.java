package ass2.engine.view.render.modeldrawer.treeDrawer;

import ass2.engine.model.components.Terrain;
import ass2.engine.model.components.Tree;
import ass2.engine.view.render.DrawUtil;
import ass2.engine.view.render.modeldrawer.ComponentDrawer;
import ass2.engine.view.textures.Texture;

import javax.media.opengl.GL2;

/**
 * User: Pierzchalski
 * Date: 17/10/13
 * Package: ass2.engine.view.render.modeldrawer.treeDrawer
 * Project: cs3421ass2
 */
public class TreeDrawer implements ComponentDrawer {
    private Terrain terrain;

    private static final float TREE_SHININESS = 10;

    private static final String TREE_TEXTURE_FILE_SRC = "src/main/resources/textures/HeatedMetal.png";
    private static final String TREE_TEXTURE_FILE_TYPE = "png";
    private Texture treeTexture;

    public TreeDrawer(Terrain terrain) {
        this.terrain = terrain;
    }

    public void init(GL2 gl) {
        this.treeTexture = new Texture(
                gl,
                TREE_TEXTURE_FILE_SRC,
                TREE_TEXTURE_FILE_TYPE);
    }

    public void draw(GL2 gl) {
        for (Tree tree : terrain.trees()) {
            drawTree(gl, tree);
        }
    }

    private void drawTree(GL2 gl, Tree tree) {
        double[] pos = tree.getPosition();
        gl.glPushMatrix(); {
            gl.glTranslated(pos[0], pos[1], pos[2]);
            for (int faceIndex = 0; faceIndex < TreeDrawerUtil.treeCylinderVertices.length; faceIndex++) {
                DrawUtil.drawPolygon3d(
                        gl,
                        treeTexture,
                        TREE_SHININESS,
                        TreeDrawerUtil.treeCylinderVertices[faceIndex],
                        TreeDrawerUtil.treeCylinderNormals[faceIndex],
                        TreeDrawerUtil.treeCylinderTexCoords[faceIndex]
                );
            }
        } gl.glPopMatrix();
    }
}
