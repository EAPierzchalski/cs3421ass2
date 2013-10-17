package ass2.engine.view.shaders;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import java.io.*;

/**
 * COMMENT: Comment Shader 
 *
 * @author malcolmr
 */
public class Shader {

    static public class CompilationException extends RuntimeException {

        public CompilationException(String message) {
            super(message);
        }
        
    }
    
    private String[] mySource;
    private int myType;
    private int myID;

    public Shader(int type, File sourceFile) {
        myType = type;
        try {
            BufferedReader in = new BufferedReader(new FileReader(sourceFile));
            StringBuilder sb = new StringBuilder();

            for (String line = in.readLine(); line != null; line = in.readLine()) {
                sb.append(line);
                sb.append("\n");
            }

            mySource = new String[1];
            mySource[0] = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public int getID() {
        return myID;
    }

    public void compile(GL2 gl) {
        myID = gl.glCreateShader(myType);
        gl.glShaderSource(myID, 1, mySource,
            new int[] { mySource[0].length() }, 0);
        gl.glCompileShader(myID);

        //Check compile status.
        int[] compiled = new int[1];
        gl.glGetShaderiv(myID, GL2ES2.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            int[] logLength = new int[1];
            gl.glGetShaderiv(myID, GL2ES2.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(myID, logLength[0], null, 0, log, 0);

            throw new CompilationException("Error compiling the vertex shader: "
                    + new String(log));
        }

        //gl.glLinkProgram(myID);
    }
}
