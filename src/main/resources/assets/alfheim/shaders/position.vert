#version 120

varying vec3 vPos;

void main() {
    gl_Position = ftransform();
	vPos = gl_Vertex.xyz;
}