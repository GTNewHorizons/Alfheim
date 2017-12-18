#version 120

varying vec3 position;
varying vec3 normal;

void main() {
    gl_Position = ftransform();

    position = (gl_ModelViewMatrix * gl_Vertex).xyz;
    normal = normalize(gl_NormalMatrix * gl_Normal);
}