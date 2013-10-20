package ass2.engine.view.render.modeldrawer.terrainDrawer;

import ass2.engine.model.Direction;
import ass2.engine.model.components.Terrain;
import ass2.engine.view.render.DrawUtil;
import ass2.engine.view.render.modeldrawer.ComponentDrawer;
import ass2.engine.view.textures.Texture;

import javax.media.opengl.GL2;

/**
 * User: Pierzchalski
 * Date: 12/10/13
 * Package: ass2.engine.view.render
 * Project: cs3421ass2
 */
public class TerrainDrawer implements ComponentDrawer {
    private static final int VERTICES_PER_QUAD_QUARTER = 3;

    private static final float TERRAIN_SHININESS = 1;

    private double[][][] faceVertices;
    private double[][] faceNormals;
    private double[][][] faceTextureCoords;

    private static final String TERRAIN_TEXTURE_FILE_SRC = "src/main/resources/textures/BlueGreenBrick.png";
    private static final String TERRAIN_TEXTURE_FILE_TYPE = "png";
    private Texture terrainTexture;

    public TerrainDrawer(Terrain terrain) {

        double[][][] gridVertices = TerrainDrawerUtil.gridVertices(terrain);

        double[][][] quadCentreVertices = TerrainDrawerUtil.quadCentreVertices(terrain);

        double[][][][] terrainQuadQuarterNormals = TerrainDrawerUtil.quadQuarterNormals(
                terrain,
                gridVertices,
                quadCentreVertices);

        int maxQuadX = terrain.size().width - 1;
        int maxQuadZ = terrain.size().height - 1;

        int numQuads = maxQuadX * maxQuadZ;
        int numFaces = numQuads * Direction.values().length;
        this.faceVertices = new double[numFaces][VERTICES_PER_QUAD_QUARTER][];
        this.faceNormals = new double[numFaces][];
        this.faceTextureCoords = new double[numFaces][][];

        int faceIndex = 0;
        for (int quadX = 0; quadX < maxQuadX; quadX++) {
            for (int quadZ = 0; quadZ < maxQuadZ; quadZ++) {
                for (Direction direction: Direction.values()) {
                    this.faceVertices[faceIndex][0] = quadCentreVertices[quadX][quadZ];

                    int cornerGridX1 = quadX + direction.quadCorners[0][0];
                    int cornerGridZ1 = quadZ + direction.quadCorners[0][1];
                    this.faceVertices[faceIndex][1] = gridVertices[cornerGridX1][cornerGridZ1];

                    int cornerGridX2 = quadX + direction.quadCorners[1][0];
                    int cornerGridZ2 = quadZ + direction.quadCorners[1][1];
                    this.faceVertices[faceIndex][2] = gridVertices[cornerGridX2][cornerGridZ2];

                    this.faceNormals[faceIndex] = terrainQuadQuarterNormals[quadX][quadZ][direction.ordinal()];

                    this.faceTextureCoords[faceIndex] = TerrainDrawerUtil.textureCoords(direction);

                    faceIndex++;
                }
            }
        }
    }

    public void init(GL2 gl) {
        this.terrainTexture = new Texture(
                gl,
                TERRAIN_TEXTURE_FILE_SRC,
                TERRAIN_TEXTURE_FILE_TYPE);
    }

    public void draw(GL2 gl) {
        gl.glPushMatrix(); {
            for (int faceIndex = 0; faceIndex < faceVertices.length; faceIndex ++) {
                DrawUtil.drawPolygon3d(
                        gl,
                        terrainTexture,
                        TERRAIN_SHININESS,
                        faceVertices[faceIndex],
                        faceNormals[faceIndex],
                        faceTextureCoords[faceIndex]);
            }
        } gl.glPopMatrix();
    }
}
