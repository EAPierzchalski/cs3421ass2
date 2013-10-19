package ass2.engine.view.render;

import ass2.util.Util;

/**
 * User: Pierzchalski
 * Date: 18/10/13
 * Package: ass2.engine.view.render
 * Project: cs3421ass2
 */
public class GeometryUtil {
    /***
     *
     *
     * @param radius
     * @param height
     * @param numRadialSegments
     * @param numVerticalSegments
     * @return a tuple containing the list of faces (a list of lists of vertices), a list of normals, and a list of texture coords.
     */
    public static double[][][][] cylinderNoCaps(
            double radius,
            double height,
            int numRadialSegments,
            int numVerticalSegments) {
        double[][][] vertices = new double[numRadialSegments][numVerticalSegments + 1][];
        double[][] normals = new double[numRadialSegments][];
        double[][][] texCoords = new double[numRadialSegments][][];
        for (int radialIndex = 0; radialIndex < numRadialSegments; radialIndex++) {
            double angle = Math.PI * 2 * radialIndex / numRadialSegments;
            double s = Math.sin(angle);
            double c = Math.cos(angle);
            normals[radialIndex] = new double[]{c, 0, s};
            texCoords[radialIndex] = new double[][]{
                    {0, (1.0 * radialIndex) / numRadialSegments},
                    {1, (1.0 * radialIndex) / numRadialSegments},
                    {1, (1.0 * (radialIndex + 1)) / numRadialSegments},
                    {0, (1.0 * (radialIndex + 1)) / numRadialSegments}
            };
            for (int verticalIndex = 0; verticalIndex <= numVerticalSegments; verticalIndex++) {
                double h = height * verticalIndex / numVerticalSegments;
                vertices[radialIndex][verticalIndex] = new double[] { radius * c, h, radius * s};
            }
        }


        double[][][][] faceVertices = new double[numRadialSegments][numVerticalSegments][][];
        double[][][][] faceNormals = new double[numRadialSegments][numVerticalSegments][][];
        double[][][][] faceTexCoords = new double[numRadialSegments][numVerticalSegments][][];
        for (int radialFaceIndex = 0; radialFaceIndex < numRadialSegments; radialFaceIndex++) {
            for (int verticalFaceIndex = 0; verticalFaceIndex < numVerticalSegments; verticalFaceIndex++) {
                int nextRadialFaceIndex = (radialFaceIndex + 1) % numRadialSegments;
                int[][] faceVertexCoords = new int[][] {
                        {radialFaceIndex, verticalFaceIndex},
                        {radialFaceIndex, verticalFaceIndex + 1},
                        {nextRadialFaceIndex, verticalFaceIndex + 1},
                        {nextRadialFaceIndex, verticalFaceIndex}
                };
                faceVertices[radialFaceIndex][verticalFaceIndex] = new double[faceVertexCoords.length][];
                for (int i = 0; i < faceVertexCoords.length; i++) {
                    int vertexRadialCoord = faceVertexCoords[i][0];
                    int vertexVerticalCoord = faceVertexCoords[i][1];
                    faceVertices[radialFaceIndex][verticalFaceIndex][i] =
                            vertices[vertexRadialCoord][vertexVerticalCoord];
                }

                int[] normalCoords = new int[] {
                        radialFaceIndex,
                        radialFaceIndex,
                        nextRadialFaceIndex,
                        nextRadialFaceIndex
                };
                faceNormals[radialFaceIndex][verticalFaceIndex] = new double[normalCoords.length][];
                for (int i = 0; i < normalCoords.length; i++) {
                    faceNormals[radialFaceIndex][verticalFaceIndex][i] = normals[normalCoords[i]];
                }

                faceTexCoords[radialFaceIndex][verticalFaceIndex] = texCoords[radialFaceIndex];
            }
        }

        return new double[][][][] {
                Util.flatten(faceVertices),
                Util.flatten(faceNormals),
                Util.flatten(faceTexCoords)
        };
    }

    public static double[][][][] cylinderCaps(
            double radius,
            double height,
            int numRadialSegments
    ) {
        double[][][] faceVertices = new double[2][numRadialSegments][];
        double[][][] faceNormals = new double[2][numRadialSegments][];
        double[][][] faceTexCoords = new double[2][numRadialSegments][];
        double[] topNormal = new double[]{0, 1, 0};
        double[] bottomNormal = new double[]{0, -1, 0};
        for (int i = 0; i < numRadialSegments; i++) {
            double angle = 2 * Math.PI * i / numRadialSegments;
            double s = Math.sin(angle);
            double c = Math.cos(angle);
            double[] texCoord = new double[]{c, s};

            faceVertices[0][i] = new double[]{radius * c, 0, radius * s};
            faceNormals[0][i] = bottomNormal;
            faceTexCoords[0][i] = texCoord;

            faceVertices[1][i] = new double[]{radius * c, height, radius * s};
            faceNormals[1][i] = topNormal;
            faceTexCoords[1][i] = texCoord;
        }
        return new double[][][][] {
                faceVertices,
                faceNormals,
                faceTexCoords
        };
    }

    public static double[][][][] cylinder(
            double radius,
            double height,
            int numRadialSegments,
            int numVerticalSegments
    ) {
        double[][][][] bodyData = cylinderNoCaps(radius, height, numRadialSegments, numVerticalSegments);
        double[][][][] capData = cylinderCaps(radius, height, numRadialSegments);
        double[][][][] cylinderData = new double[bodyData.length][][][];
        for (int dataIndex = 0; dataIndex < bodyData.length; dataIndex++) {
            cylinderData[dataIndex] = Util.join(bodyData[dataIndex], capData[dataIndex]);
        }
        return cylinderData;
    }
}
