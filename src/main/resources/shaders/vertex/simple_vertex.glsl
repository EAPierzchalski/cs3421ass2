//VERTEX SHADER

varying vec3 normal;
//varying vec3 model_position;
varying vec2 texture_coordinate;

void main(void) {
    normal = vec3(gl_NormalMatrix * gl_Normal);
    //model_position = vec3(gl_ModelViewMatrix * gl_Vertex);
    texture_coordinate = vec2(gl_MultiTexCoord0);
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}