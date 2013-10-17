//FRAGMENT SHADER

varying vec3 normal;
//varying vec3 model_position;
varying vec2 texture_coordinate;
uniform sampler2D texture_sampler;

void main(void) {
    vec3 light_direction = normalize(gl_LightSource[0].position.xyz);
    vec4 I_ambient = gl_FrontLightProduct[0].ambient;
    vec4 I_diffuse = gl_FrontLightProduct[0].diffuse * max(dot(normal,light_direction), 0.0);
    I_diffuse = clamp(I_diffuse, 0.0, 1.0);

    gl_FragColor =
            (I_ambient +
            I_diffuse +
            texture2D(texture_sampler, texture_coordinate)) / 3;
}