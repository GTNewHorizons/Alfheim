#version 120

varying vec3 position;
varying vec3 normal;

void main() {
    vec4 rimColor = vec4(1.0, 0.5, 0.0, 1.0);
    float scale = 1.0;
    vec3 cameraPos = -position;
    vec3 ray = normalize(position - cameraPos);
    float refFactor = max(0.0, min(1.0, scale * pow(1.0 + dot(ray, normal), 1.4)));

    gl_FragColor = mix(vec4(1.0, 1.0, 1.0, 0.0), rimColor, refFactor);
}