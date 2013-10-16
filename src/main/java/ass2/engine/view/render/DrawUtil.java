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
    public static void drawPolygon3d(
            GL2 gl,
            double[][] vertices,
            double[] normal,
            Texture texture,
            double[][] textureCoords,
            float[] color) {
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getTextureID());
        // use the texture to modulate diffuse and ambient lighting
        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, color, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, color, 0);
        gl.glBegin(GL2.GL_POLYGON); {
            for (int i = 0; i < vertices.length; i++) {
                double[] vertex = vertices[i];
                double[] texCoords = textureCoords[i];
                gl.glNormal3dv(normal, 0);
                gl.glTexCoord2dv(texCoords, 0);
                gl.glVertex3dv(vertex, 0);
            }
        } gl.glEnd();
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
        gl.glBegin(GL2.GL_LINES); {
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, color, 0);
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, color, 0);
            gl.glVertex3dv(start, 0);
            gl.glVertex3dv(end, 0);
        } gl.glEnd();
    }
}
