package ass2.engine.view;

import ass2.util.Util;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * Created with IntelliJ IDEA.
 * User: Edward
 * Date: 10/10/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Camera {
    private float[] backgroundColor = new float[]{1, 0, 0, 0};
    private double[] position;
    private double[] lookDirection;
    private double[] lookingAt;
    private double[] up;

    private static final double VERTICAL_TOLERANCE = 1E-3;

    /***
     *
     * @param position a length 3 array storing the position of the camera
     * @param lookDirection a length 3 array storing the direction the camera is looking at
     */
    public Camera(double[] position, double[] lookDirection) {
        this.position = position;
        this.lookDirection = lookDirection;
        this.lookingAt = Util.add(position, lookDirection);
        this.up = new double[]{0, 1, 0};
        updateUp();
    }

    public double[] getPosition() {
        return Util.copyArray(position);
    }

    public double[] getLookDirection() {
        return Util.copyArray(lookDirection);
    }

    public double[] getLookingAt() {
        return Util.copyArray(lookingAt);
    }

    private void updateUp() {
        if (Math.abs(lookDirection[0]) +
                Math.abs(lookDirection[2]) < VERTICAL_TOLERANCE) {
            up[0] = lookDirection[0];
            up[1] = 0;
            up[2] = lookDirection[2];
        } else {
            up[0] = 0;
            up[1] = 1;
            up[2] = 0;
        }
    }

    public void setPosition(double[] position) {
        this.position = position;
        this.lookingAt = Util.add(position, lookDirection);
        updateUp();
    }

    public void setLookDirection(double[] lookDirection) {
        this.lookDirection = lookDirection;
        this.lookingAt = Util.add(position, lookDirection);
        updateUp();
    }

    public void setView(GL2 gl) {
        gl.glClearColor(backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW); {
            GLU glu = new GLU();
            gl.glLoadIdentity();
            gluLookAt(glu, position, lookingAt, up);
        }
    }

    private static void gluLookAt(GLU glu, double[] eye, double[] centre, double[] up) {
        glu.gluLookAt(
                eye[0], eye[1], eye[2],
                centre[0], centre[1], centre[2],
                up[0], up[1], up[2]
        );
    }

    public void reshape(GL2 gl, int x, int y, int width, int height) {
        double aspectRatio = ((double) width) / height;
        GLU glu = new GLU();
        gl.glMatrixMode(GL2.GL_PROJECTION); {
            gl.glLoadIdentity();
            glu.gluPerspective(90, aspectRatio, 0.2, 10);
        }
    }

}
