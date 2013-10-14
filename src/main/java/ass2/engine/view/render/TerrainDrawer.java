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
    private static final float[] TERRAIN_COLOR = new float[]{0.4f, 1f, 0.1f, 1};

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

        this.terrainGridVertices = getTerrainGridVerticesFromTerrain(terrain);

        this.terrainQuadCentreVertices = getTerrainQuadCentreVerticesFromTerrain(terrain);

        this.terrainQuadQuarterNormals = new double[maxQuadX][maxQuadZ][Direction.values().length][];
        for (int x = 0; x < maxQuadX; x++) {
            for (int z = 0; z < maxQuadZ; z++) {
                for (Direction direction: Direction.values()) {
                    terrainQuadQuarterNormals[x][z][direction.index] =
                            quarterNormal(x, z, direction, terrainGridVertices, terrainQuadCentreVertices);
                }
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

    private static double[][][] getTerrainGridVerticesFromTerrain(Terrain terrain) {
        int maxGridX = terrain.size().width;
        int maxGridZ = terrain.size().height;
        double[][][] terrainGridVertices = new double[maxGridX][maxGridZ][];
        for (int gridX = 0; gridX < maxGridX; gridX++) {
            for (int gridZ = 0; gridZ < maxGridZ; gridZ++) {
                terrainGridVertices[gridX][gridZ] = new double[]{
                        gridX,
                        terrain.getGridAltitude(gridX, gridZ),
                        gridZ
                };
            }
        }
        return terrainGridVertices;
    }

    private static double[][][] getTerrainQuadCentreVerticesFromTerrain(Terrain terrain) {
        int maxQuadX = terrain.size().width - 1;
        int maxQuadZ = terrain.size().height - 1;
        double[][][] terrainQuadCentreVertices = new double[maxQuadX][maxQuadZ][];
        for (int quadX = 0; quadX < maxQuadX; quadX++) {
            for (int quadZ = 0; quadZ < maxQuadZ; quadZ++) {
                double averageBoundaryAltitude = (
                        terrain.getGridAltitude(quadX, quadZ) +
                                terrain.getGridAltitude(quadX + 1, quadZ) +
                                terrain.getGridAltitude(quadX + 1, quadZ + 1) +
                                terrain.getGridAltitude(quadX, quadZ + 1)
                ) / 4;
                terrainQuadCentreVertices[quadX][quadZ] = new double[]{
                        quadX + 0.5,
                        averageBoundaryAltitude,
                        quadZ + 0.5
                };
            }
        }
        return terrainQuadCentreVertices;
    }

/*    private double[] quarterNormal(
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
    }*/

    public void drawTerrain(GL2 gl) {
        gl.glPushMatrix(); {
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, TERRAIN_COLOR, 0);
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, TERRAIN_COLOR, 0);
            for (int faceIndex = 0; faceIndex < faceVertices.length; faceIndex ++) {
                DrawUtil.drawPolygon3d(gl, faceVertices[faceIndex], faceNormals[faceIndex]);
            }
        } gl.glPopMatrix();
    }

    private double[] quarterNormal(
            int gridX,
            int gridZ,
            Direction quadQuarter,
            double[][][] gridVertices,
            double[][][] quadCentreVertices) {
        double[] centreVertex = quadCentreVertices[gridX][gridZ];
        int cornerGridX1 = gridX + quadQuarter.quadCorners[0][0];
        int cornerGridZ1 = gridZ + quadQuarter.quadCorners[0][1];
        double[] quarterVertex1 = gridVertices[cornerGridX1][cornerGridZ1];

        int cornerGridX2 = gridX + quadQuarter.quadCorners[1][0];
        int cornerGridZ2 = gridZ + quadQuarter.quadCorners[1][1];
        double[] quarterVertex2 = gridVertices[cornerGridX2][cornerGridZ2];

        double[] v1 = Util.sub(quarterVertex1, centreVertex);
        double[] v2 = Util.sub(quarterVertex2, centreVertex);
        return Util.cross(v2, v1);
    }

    private enum Direction {
        NORTH(1, 1, 0, 1, 0),
        EAST(1, 0, 1, 1, 1),
        SOUTH(0, 0, 1, 0, 2),
        WEST(0, 1, 0, 0, 3);

        public int[][] quadCorners;
        public int index;

        private Direction(int x1, int z1, int x2, int z2, int index) {
            this.quadCorners = new int[][]{{x1, z1}, {x2, z2}};
            this.index = index;
        }
    }
}
