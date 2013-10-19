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
    private static double[][][][] cylinderNoCaps(
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

    private static double[][][][] cylinderCaps(
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

    /***
     * result of extruding a straight line along a spine, held constantly horizontal.
     * @param spine an array of 3d positions of the spine
     * @param spineTangents an array of 3d tangents to the spine, at the points in "spine"
     * @param width
     * @return an array containing, in order, the face vertices, face normals, and face texture coordinates.
     */
    public static double[][][][] extrude(
            double[][] spine,
            double[][] spineTangents,
            double width
    ) {
        double[][][] vertices = new double[spine.length][2][];
        double[] normal = new double[]{0, 1, 0};
        double[][] quadNormals = new double[][] {
                normal,
                normal,
                normal,
                normal
        };
        double[][] quadTextureCoords = new double[][]{
                {0, 0},
                {0, 1},
                {1, 1},
                {1, 0}
        };

        for (int i = 0; i < spine.length; i++) {
            double[] tangent = spineTangents[i];
            double[] perpendicular1 = Util.normalize(new double[] {
                    -tangent[2],
                    tangent[1],
                    tangent[0]
            });
            double[] perpendicular2 = Util.normalize(new double[] {
                    tangent[2],
                    tangent[1],
                    -tangent[0]
            });
            double[] pos = spine[i];
            vertices[i][0] = Util.sum(pos, Util.scale(width, perpendicular1));
            vertices[i][1] = Util.sum(pos, Util.scale(width, perpendicular2));
        }

        int numFaces = spine.length - 1;
        double[][][] faceVertices = new double[numFaces][][];
        double[][][] faceNormals = new double[numFaces][][];
        double[][][] faceTexCoords = new double[numFaces][][];
        for (int face = 0; face < spine.length - 1; face++) {
            faceNormals[face] = quadNormals;
            faceTexCoords[face] = quadTextureCoords;
            faceVertices[face] = new double[][] {
                    vertices[face][1],
                    vertices[face + 1][1],
                    vertices[face + 1][0],
                    vertices[face][0]
            };
        }

        return new double[][][][] {
                faceVertices,
                faceNormals,
                faceTexCoords
        };
    }
}
