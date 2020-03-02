#version 120

varying vec3 vPos;
varying vec2 texcoord;

void main() {
    gl_Position = gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; // ftransform();
	vPos = gl_Vertex.xyz;
	texcoord = vec2(gl_MultiTexCoord0);
}