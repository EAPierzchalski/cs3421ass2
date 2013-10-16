//VERTEX SHADER

void main(void) {
    gl_Position = glModelViewProjectionMatrix * gl_Vertex;
}