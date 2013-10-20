//FRAGMENT SHADER

//Substantial portions of this shader come from http://wiki.delphigl.com/index.php/shader_PerPixel_Lighting2.

#version 120

varying vec3 normal;
varying vec3 position;
varying vec4 ambient;
varying vec2 texture_coordinate;
uniform sampler2D texture_sampler;
uniform int ActiveLights = 2;
uniform float ShadeCells = 5.0;

void main(void) {
    vec3 lightDir;
    float attenFactor;
    vec3 eyeDir = normalize(-position);
    vec4 lightAmbientDiffuse = vec4(0.0, 0.0, 0.0, 0.0);
    vec4 lightSpecular = vec4(0.0, 0.0, 0.0, 0.0);

    for (int i=0; i<ActiveLights; ++i){

        // attenuation and light direction
        if (gl_LightSource[i].position.w != 0.0) {
            lightDir = normalize(gl_LightSource[i].position.xyz - position);
            float cosTheta = dot(lightDir, -gl_LightSource[i].spotDirection);
            float dist = distance(gl_LightSource[i].position.xyz, position);
            attenFactor = 1.0/( gl_LightSource[i].constantAttenuation +
                    gl_LightSource[i].linearAttenuation * dist +
                    gl_LightSource[i].quadraticAttenuation * dist * dist ) *
                    pow(cosTheta, gl_LightSource[i].spotExponent);
            //if (cosTheta < gl_LightSource[i].spotCosCutoff) {
            //    attenFactor = 0.0;
            //}
        } else {
            attenFactor = 1.0;
            lightDir = gl_LightSource[i].position.xyz;
        }

        // ambient + diffuse
        lightAmbientDiffuse += gl_FrontLightProduct[i].ambient * attenFactor;
        lightAmbientDiffuse += gl_FrontLightProduct[i].diffuse * max(dot(normal, lightDir), 0.0) * attenFactor;

        // specular
        vec3 r = normalize(reflect(lightDir, normal));
        lightSpecular += gl_FrontLightProduct[i].specular *
                pow(max(dot(r, eyeDir), 0.0), gl_FrontMaterial.shininess) *
                attenFactor * 0.5;
    }

    // compute final color
    vec4 texColor = texture2D(texture_sampler, texture_coordinate);
    vec4 tempColor = texColor * (lightAmbientDiffuse + lightSpecular);
    //for (int i = 0; i < 3; i++) {
    //     if (tempColor[i] < 0.5) {
    ///         tempColor[i] = tempColor[i] * tempColor[i];
    //     } else {
    //         float tc = tempColor[i];
    //         tempColor[i] = 1.0 - (1.0 - tc) * (1.0 - tc);
    //     }
    //}
    //for (int i = 0; i < 3; i++) {
    ///    tempColor[i] = (tempColor[i] * ShadeCells - mod(tempColor[i] * ShadeCells, 1.0)) / ShadeCells;
    //}
    float len = length(vec3(tempColor[0], tempColor[1], tempColor[2]));
    len = (len * ShadeCells - mod(len * ShadeCells, 1.0)) / ShadeCells;
    tempColor = len * tempColor;
    tempColor[3] = 1.0;
    gl_FragColor = tempColor;
}