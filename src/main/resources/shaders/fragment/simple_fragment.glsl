//FRAGMENT SHADER

varying vec2 texture_coordinate;
uniform sampler2D texture_sampler;

void main(void) {
    gl_FragColor = texture2D(texture_sampler, texture_coordinate);
}