//VERTEX SHADER

varying vec3 normal;
varying vec3 position;
varying vec4 ambient;
varying vec2 texture_coordinate;

void main(void) {
    normal = normalize(vec3(gl_NormalMatrix * gl_Normal));
    position = vec3(gl_ModelViewMatrix * gl_Vertex);
    ambient = gl_LightModel.ambient * gl_FrontMaterial.ambient;
    texture_coordinate = vec2(gl_MultiTexCoord0);
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}