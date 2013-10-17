package ass2.engine.view.shaders;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;

/**
 * User: Pierzchalski
 * Date: 17/10/13
 * Package: ass2.engine.view.shaders
 * Project: cs3421ass2
 */
public class Program {
    private int myID;

    public Program(GL2 gl, Shader shader, Shader... shaders) {
        myID = gl.glCreateProgram();
        gl.glAttachShader(myID, shader.getID());
        for (Shader s: shaders) {
            gl.glAttachShader(myID, s.getID());
        }
        gl.glLinkProgram(myID);
        int[] param = new int[1];
        gl.glGetProgramiv(myID, GL2ES2.GL_LINK_STATUS, param, 0);
        if (param[0] != GL.GL_TRUE) {
            byte[] charArray = new byte[GL2.GL_MAX_DEBUG_MESSAGE_LENGTH];
            gl.glGetProgramInfoLog(myID, );
            System.out.println("failed to link program");
        }
    }

    public int getMyID() {
        return myID;
    }
}
