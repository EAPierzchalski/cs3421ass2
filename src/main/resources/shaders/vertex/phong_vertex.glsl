//VERTEX SHADER

varying vec3 normal;
varying vec3 position;
varying vec4 ambient;

void main(void) {
    normal = normalize(vec3(gl_NormalMatrix * gl_Normal));
    position = vec3(gl_ModelViewMatrix * gl_Vertex);
    ambient = gl_LightModel.ambient * gl_FrontMaterial.ambient;
    gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_TexCoord[1] = gl_MultiTexCoord0;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}