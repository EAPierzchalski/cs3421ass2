package ass2.engine.view.render;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * User: Pierzchalski
 * Date: 12/10/13
 * Package: ass2.engine.view.render
 * Project: cs3421ass2
 */
public class DrawUtil {
    public static void drawPolygon3d(GL2 gl, double[][] vertices, double[] normal, float[] color) {
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glBegin(GL2.GL_POLYGON); {
            for (double[] vertex : vertices) {
                gl.glNormal3dv(normal, 0);
                gl.glColor3f(color[0], color[1], color[2]);
                gl.glVertex3dv(vertex, 0);
            }
        } gl.glEnd();
    }

    public static void drawAxes(GL2 gl) {
        gl.glLineWidth(3);
        gl.glBegin(GL2.GL_LINES); {
            gl.glColor3f(1, 0, 0);
            gl.glVertex3d(0, 0, 0);
            gl.glVertex3d(1, 0, 0);
        } gl.glEnd();
        gl.glBegin(GL2.GL_LINES); {
            gl.glColor3f(0, 1, 0);
            gl.glVertex3d(0, 0, 0);
            gl.glVertex3d(0, 1, 0);
        } gl.glEnd();
        gl.glBegin(GL2.GL_LINES); {
            gl.glColor3f(0, 0, 1);
            gl.glVertex3d(0, 0, 0);
            gl.glVertex3d(0, 0, 1);
        } gl.glEnd();
    }
}
