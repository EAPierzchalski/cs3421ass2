package ass2.engine.model.shaderoptions;

import ass2.engine.view.shaders.Program;
import ass2.engine.view.shaders.Shader;

import javax.media.opengl.GL2;
import java.io.File;

/**
 * User: Pierzchalski
 * Date: 20/10/13
 * Package: ass2.engine.model.shaderoptions
 * Project: cs3421ass2
 */
public enum ShaderChoice {
    NONE {
        @Override
        public ShaderChoice getNext() {
            return SIMPLE;
        }
    },

    SIMPLE(
            ShaderChoiceData.SIMPLE_VERTEX_SHADER_SRC,
            ShaderChoiceData.SIMPLE_FRAGMENT_SHADER_SRC
    ) {
        @Override
        public ShaderChoice getNext() {
            return PHONG;
        }
    },

    PHONG(
            ShaderChoiceData.PHONG_VERTEX_SHADER_SRC,
            ShaderChoiceData.PHONG_FRAGMENT_SHADER_SRC) {
        @Override
        public ShaderChoice getNext() {
            return NPR;
        }
    },

    NPR(
            ShaderChoiceData.NPR_VERTEX_SHADER_SRC,
            ShaderChoiceData.NPR_FRAGMENT_SHADER_SRC
    ) {
        @Override
        public ShaderChoice getNext() {
            return NONE;
        }
    };

    private Shader vertexShader;
    private Shader fragmentShader;
    private Program program;

    private ShaderChoice(String vertexShaderSrcLoc, String fragmentShaderSrcLoc) {
        this.vertexShader = new Shader(GL2.GL_VERTEX_SHADER, new File(vertexShaderSrcLoc));
        this.fragmentShader = new Shader(GL2.GL_FRAGMENT_SHADER, new File(fragmentShaderSrcLoc));
    }

    private ShaderChoice() {
    }

    public static void init(GL2 gl) {
        for (ShaderChoice shaderChoice : ShaderChoice.values()) {
            if (shaderChoice != NONE) {
                shaderChoice.vertexShader.compile(gl);
                shaderChoice.fragmentShader.compile(gl);
                shaderChoice.program = new Program(gl, shaderChoice.fragmentShader, shaderChoice.vertexShader);
            }
        }
    }

    public Program getProgram() {
        return program;
    }

    public void recompile(GL2 gl) {
        this.vertexShader.compile(gl);
        this.fragmentShader.compile(gl);
        this.program = new Program(gl, this.fragmentShader, this.vertexShader);
    }

    public abstract ShaderChoice getNext();
}
