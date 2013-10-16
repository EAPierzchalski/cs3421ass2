package ass2.engine.view.render;

import ass2.engine.model.Direction;
import ass2.engine.model.Terrain;
import ass2.engine.view.textures.Texture;
import ass2.util.Util;

import javax.media.opengl.GL2;

/**
 * User: Pierzchalski
 * Date: 12/10/13
 * Package: ass2.engine.view.render
 * Project: cs3421ass2
 */
public class TerrainDrawer {
    private static final int VERTICES_PER_QUAD_QUARTER = 3;
    private static final float[] TERRAIN_COLOR = new float[]{0.4f, 1f, 0.1f, 1};

    private double[][][] faceVertices;
    private double[][] faceNormals;
    private double[][][] faceTextureCoords;

    private Texture terrainTexture;

    public TerrainDrawer(Terrain terrain) {

        /**
         terrainQuadGridVertices is a 2D array of vertex positions of the grid vertices of the terrain.
         */double[][][] gridVertices = gridVertices(terrain);

        /**
         quadCentreVertices is a 2D array of vertex positions of the centres of the quads of the terrain.
         quadCentreVertices[x][z] = {x + 1/2, average height of surrounding vertices, z + 1/2}.
         */double[][][] quadCentreVertices = quadCentreVertices(terrain);

        /**
         terrainQuadQuarterNormals[x][z][s] is the normal for the s'th quarter of the [x, z] quad.
         */double[][][][] terrainQuadQuarterNormals = quadQuarterNormals(
                terrain,
                gridVertices,
                quadCentreVertices);

        int maxGridX = terrain.size().width;
        int maxQuadX = maxGridX - 1;
        int maxGridZ = terrain.size().height;
        int maxQuadZ = maxGridZ - 1;

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

                    this.faceTextureCoords[faceIndex] = textureCoords(quadX, quadZ, direction);

                    faceIndex++;
                }
            }
        }

    }

    public void setTerrainTexture(Texture terrainTexture) {
        this.terrainTexture = terrainTexture;
    }

    private static double[][][][] quadQuarterNormals(
            Terrain terrain,
            double[][][] gridVertices,
            double[][][] quadCentreVertices) {
        int maxQuadX = terrain.size().width - 1;
        int maxQuadZ = terrain.size().height - 1;
        double[][][][] quarterNormals = new double[maxQuadX][maxQuadZ][Direction.values().length][];
        for (int x = 0; x < maxQuadX; x++) {
            for (int z = 0; z < maxQuadZ; z++) {
                for (Direction direction: Direction.values()) {
                    quarterNormals[x][z][direction.ordinal()] =
                            quarterNormal(x, z, direction, gridVertices, quadCentreVertices);
                }
            }
        }
        return quarterNormals;
    }

    private static double[][] textureCoords(int quadX, int quadZ, Direction direction) {
        return new double[][] {
                {0.5, 0.5},
                {direction.quadCorners[0][0], direction.quadCorners[0][1]},
                {direction.quadCorners[1][0], direction.quadCorners[1][1]}
        };
    }

    private static double[][][] gridVertices(Terrain terrain) {
        int maxGridX = terrain.size().width;
        int maxGridZ = terrain.size().height;
        double[][][] terrainGridVertices = new double[maxGridX][maxGridZ][];
        for (int gridX = 0; gridX < maxGridX; gridX++) {
            for (int gridZ = 0; gridZ < maxGridZ; gridZ++) {
                terrainGridVertices[gridX][gridZ] = new double[]
                       {gridX,
                        terrain.getGridAltitude(gridX, gridZ),
                        gridZ};
            }
        }
        return terrainGridVertices;
    }

    private static double[][][] quadCentreVertices(Terrain terrain) {
        int maxQuadX = terrain.size().width - 1;
        int maxQuadZ = terrain.size().height - 1;
        double[][][] terrainQuadCentreVertices = new double[maxQuadX][maxQuadZ][];
        for (int quadX = 0; quadX < maxQuadX; quadX++) {
            for (int quadZ = 0; quadZ < maxQuadZ; quadZ++) {
                double averageBoundaryAltitude = (
                        terrain.getGridAltitude(quadX, quadZ) +
                        terrain.getGridAltitude(quadX + 1, quadZ) +
                        terrain.getGridAltitude(quadX + 1, quadZ + 1) +
                        terrain.getGridAltitude(quadX, quadZ + 1)) / 4;
                terrainQuadCentreVertices[quadX][quadZ] = new double[]
                       {quadX + 0.5,
                        averageBoundaryAltitude,
                        quadZ + 0.5};
            }
        }
        return terrainQuadCentreVertices;
    }

    private static double[] quarterNormal(
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

        return Util.cross(v1, v2);
    }

    //private static final float[] GREY = new float[]{0.5f, 0.5f, 0.5f, 1};
    public void drawTerrain(GL2 gl) {
        gl.glPushMatrix(); {
            for (int faceIndex = 0; faceIndex < faceVertices.length; faceIndex ++) {
                DrawUtil.drawPolygon3d(
                        gl,
                        faceVertices[faceIndex],
                        faceNormals[faceIndex],
                        terrainTexture,
                        faceTextureCoords[faceIndex],
                        TERRAIN_COLOR);

                /*double[] faceCentroid = new double[] {0, 0, 0};
                for (double[] vertex: faceVertices[faceIndex]) {
                    faceCentroid = Util.sum(faceCentroid, vertex);
                }
                faceCentroid = Util.scale(faceCentroid, 1.0/faceVertices[faceIndex].length);

                DrawUtil.drawLine(
                        gl,
                        faceCentroid,
                        Util.sum(faceCentroid, faceNormals[faceIndex]),
                        GREY);*/
            }
        } gl.glPopMatrix();
    }
}
