package ass2.engine.view.render;

import ass2.engine.view.textures.Texture;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * User: Pierzchalski
 * Date: 12/10/13
 * Package: ass2.engine.view.render
 * Project: cs3421ass2
 */
public class DrawUtil {

    private static final float[] WHITE = new float[]{1, 1, 1, 1};

    /***
     *
     * @param gl
     * @param texture
     * @param vertices
     * @param normal
     * @param textureCoords
     */
    public static void drawPolygon3d(
            GL2 gl,
            Texture texture,
            double[][] vertices,
            double[] normal,
            double[][] textureCoords) {
        // bind the texture
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getTextureID());
        // use the texture to modulate diffuse and ambient lighting
        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, WHITE, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, WHITE, 0);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glBegin(GL2.GL_POLYGON); {
            for (int i = 0; i < vertices.length; i++) {
                gl.glNormal3dv(normal, 0);
                gl.glTexCoord2dv(textureCoords[i], 0);
                gl.glVertex3dv(vertices[i], 0);
            }
        } gl.glEnd();
    }

    /***
     *
     * @param gl
     * @param texture
     * @param vertices
     * @param normals
     * @param textureCoords
     */
    public static void drawPolygon3d(
            GL2 gl,
            Texture texture,
            double[][] vertices,
            double[][] normals,
            double[][] textureCoords) {
        // bind the texture
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getTextureID());
        // use the texture to modulate diffuse and ambient lighting
        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, WHITE, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, WHITE, 0);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glBegin(GL2.GL_POLYGON); {
            for (int i = 0; i < vertices.length; i++) {
                gl.glNormal3dv(normals[i], 0);
                gl.glTexCoord2dv(textureCoords[i], 0);
                gl.glVertex3dv(vertices[i], 0);
            }
        } gl.glEnd();
    }

    public static void drawMesh3d(
            GL2 gl,
            Texture texture,
            double[][][] vertices,
            double[][][] normals,
            double[][][] textureCoords
    ) {
        for (int faceIndex = 0; faceIndex < vertices.length; faceIndex++) {
            double[][] faceVertices = vertices[faceIndex];
            double[][] faceNormals = normals[faceIndex];
            double[][] faceTextureCoords = textureCoords[faceIndex];
            drawPolygon3d(
                    gl,
                    texture,
                    faceVertices,
                    faceNormals,
                    faceTextureCoords
            );
        }
    }

    private static final float[] RED = new float[]{1, 0, 0, 1};
    private static final float[] GREEN = new float[]{0, 1, 0, 1};
    private static final float[] BLUE = new float[]{0, 0, 1, 1};

    public static void drawAxes(GL2 gl) {
        double[] origin = new double[]{0, 0, 0};
        double[] x = new double[]{1, 0, 0};
        double[] y = new double[]{0, 1, 0};
        double[] z = new double[]{0, 0, 1};
        drawLine(gl, origin, x, RED);
        drawLine(gl, origin, y, GREEN);
        drawLine(gl, origin, z, BLUE);
    }

    public static void drawLine(GL2 gl, double[] start, double[] end, float[] color) {
        gl.glLineWidth(3);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, color, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, color, 0);
        gl.glBegin(GL2.GL_LINES); {
            gl.glVertex3dv(start, 0);
            gl.glVertex3dv(end, 0);
        } gl.glEnd();
    }

    public static void drawLine(GL2 gl, double[][] vertices, float[] color) {
        gl.glLineWidth(3);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, color, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, color, 0);
        gl.glBegin(GL2.GL_LINE_STRIP); {
            for (double[] vertex : vertices) {
                gl.glVertex3dv(vertex, 0);
            }
        } gl.glEnd();
    }
}
