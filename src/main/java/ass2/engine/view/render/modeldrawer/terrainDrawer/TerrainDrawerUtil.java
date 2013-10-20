package ass2.engine.view.render.modeldrawer.terrainDrawer;

import ass2.engine.model.Direction;
import ass2.engine.model.components.Terrain;
import ass2.util.Util;

/**
 * Created with IntelliJ IDEA.
 * User: Edward
 * Date: 16/10/13
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerrainDrawerUtil {

    public static double[][][][] quadQuarterNormals(
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

    public static double[][] textureCoords(Direction direction) {
        return new double[][] {
                {0.5, 0.5},
                {direction.quadCorners[0][0], direction.quadCorners[0][1]},
                {direction.quadCorners[1][0], direction.quadCorners[1][1]}
        };
    }

    public static double[][][] gridVertices(Terrain terrain) {
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

    public static double[][][] quadCentreVertices(Terrain terrain) {
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

    public static double[] quarterNormal(
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
}
