package ass2.engine.view.render;

import ass2.engine.model.Terrain;
import ass2.util.Util;

import javax.media.opengl.GL2;

/**
 * User: Pierzchalski
 * Date: 12/10/13
 * Package: ass2.engine.view.render
 * Project: cs3421ass2
 */
public class TerrainDrawer {
    private static final int FACES_PER_TERRAIN_QUAD = 4;

    private Terrain terrain;

    private double[][][] faceVertices;
    private double[][] faceNormals;

    /***
     * terrainQuadGridVertices is a 2D array of vertex positions of the grid vertices of the terrain.
     */
    private double[][][] terrainGridVertices;

    /***
     * terrainQuadCentreVertices is a 2D array of vertex positions of the centres of the quads of the terrain.
     * terrainQuadCentreVertices[x][z] = {x + 1/2, average height of surrounding vertices, z + 1/2}.
     */
    private double[][][] terrainQuadCentreVertices;

    /***
     * terrainQuadQuarterNormals[x][z][s] is the normal for the s'th quarter of the [x, z] quad.
     * the 0th quarter is [x, z] to [x+1, z]
     * the 1st quarter is [x+1, z] to [x+1, z+1]
     * the 2nd quarter is [x+1, z+1] to [x, z+1]
     * the 3rd quarter is [x, z+1] to [x, z]
     */
    private double[][][][] terrainQuadQuarterNormals;

    public TerrainDrawer(Terrain terrain) {
        this.terrain = terrain;
        int maxGridX = terrain.size().width;
        int maxQuadX = maxGridX - 1;
        int maxGridZ = terrain.size().height;
        int maxQuadZ = maxGridZ - 1;

        this.terrainGridVertices = new double[maxGridX][maxGridZ][];
        for (int gridX = 0; gridX < maxGridX; gridX++) {
            for (int gridZ = 0; gridZ < maxGridZ; gridZ++) {
                this.terrainGridVertices[gridX][gridZ] = new double[]{
                        gridX,
                        terrain.getGridAltitude(gridX, gridZ),
                        gridZ
                };
            }
        }

        this.terrainQuadCentreVertices = new double[maxQuadX][maxQuadZ][];
        for (int quadX = 0; quadX < maxQuadX; quadX++) {
            for (int quadZ = 0; quadZ < maxQuadZ; quadZ++) {
                double averageBoundaryAltitude = (
                        terrain.getGridAltitude(quadX, quadZ) +
                        terrain.getGridAltitude(quadX + 1, quadZ) +
                        terrain.getGridAltitude(quadX + 1, quadZ + 1) +
                        terrain.getGridAltitude(quadX, quadZ + 1)
                        ) / 4;
                this.terrainQuadCentreVertices[quadX][quadZ] = new double[]{
                        quadX + 0.5,
                        averageBoundaryAltitude,
                        quadZ + 0.5
                };
            }
        }

        this.terrainQuadQuarterNormals = new double[maxQuadX][maxQuadZ][][];
        for (int x = 0; x < maxQuadX; x++) {
            for (int z = 0; z < maxQuadZ; z++) {
                double[] zerothQuarterNormal = quarterNormal(x, z, x, z, x + 1, z);
                double[] firstQuarterNormal = quarterNormal(x, z, x + 1, z, x + 1, z + 1);
                double[] secondQuarterNormal = quarterNormal(x, z, x + 1, z + 1, x + 1, z);
                double[] thirdQuarterNormal = quarterNormal(x, z, x + 1, z, x, z);
                this.terrainQuadQuarterNormals[x][z] = new double[][] {
                        zerothQuarterNormal,
                        firstQuarterNormal,
                        secondQuarterNormal,
                        thirdQuarterNormal
                };
            }
        }

        int numQuads = maxQuadX * maxQuadZ;
        this.faceVertices = new double[FACES_PER_TERRAIN_QUAD * numQuads][][];
        this.faceNormals = new double[FACES_PER_TERRAIN_QUAD * numQuads][];

        for (int quadX = 0; quadX < maxQuadX; quadX++) {
            for (int quadZ = 0; quadZ < maxQuadZ; quadZ++) {
                int currentFaceIndex = FACES_PER_TERRAIN_QUAD * (quadX * maxQuadZ + quadZ);

                this.faceVertices[currentFaceIndex] = new double[][] {
                        terrainGridVertices[quadX][quadZ],
                        terrainGridVertices[quadX + 1][quadZ],
                        terrainQuadCentreVertices[quadX][quadZ]
                };
                this.faceNormals[currentFaceIndex]     = this.terrainQuadQuarterNormals[quadX][quadZ][0];

                this.faceVertices[currentFaceIndex + 1] = new double[][] {
                        terrainGridVertices[quadX + 1][quadZ],
                        terrainGridVertices[quadX + 1][quadZ + 1],
                        terrainQuadCentreVertices[quadX][quadZ]
                };
                this.faceNormals[currentFaceIndex + 1] = this.terrainQuadQuarterNormals[quadX][quadZ][1];

                this.faceVertices[currentFaceIndex + 2] = new double[][] {
                        terrainGridVertices[quadX + 1][quadZ + 1],
                        terrainGridVertices[quadX][quadZ + 1],
                        terrainQuadCentreVertices[quadX][quadZ]
                };
                this.faceNormals[currentFaceIndex + 2] = this.terrainQuadQuarterNormals[quadX][quadZ][2];

                this.faceVertices[currentFaceIndex + 3] = new double[][] {
                        terrainGridVertices[quadX][quadZ + 1],
                        terrainGridVertices[quadX][quadZ],
                        terrainQuadCentreVertices[quadX][quadZ]
                };
                this.faceNormals[currentFaceIndex + 3] = this.terrainQuadQuarterNormals[quadX][quadZ][3];
            }
        }

    }

    private double[] quarterNormal(
            int quadX, int quadZ,
            int cornerX1, int cornerZ1,
            int cornerX2, int cornerZ2) {
        double[] centreToCorner1 = Util.sub(
                terrainGridVertices[cornerX1][cornerZ1],
                terrainQuadCentreVertices[quadX][quadZ]
        );
        double[] centreToCorner2 = Util.sub(
                terrainGridVertices[cornerX2][cornerZ2],
                terrainQuadCentreVertices[quadX][quadZ]
        );
        return Util.cross(centreToCorner1, centreToCorner2);
    }

    public void drawTerrain(GL2 gl, float[] terrainColor) {
        gl.glPushMatrix(); {
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
            gl.glColor3f(0, 1, 0);
            for (int faceIndex = 0; faceIndex < faceVertices.length; faceIndex ++) {
                DrawUtil.drawPolygon3d(gl, faceVertices[faceIndex], faceNormals[faceIndex], terrainColor);
            }
        } gl.glPopMatrix();
    }

}
