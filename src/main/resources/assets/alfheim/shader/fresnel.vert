#version 120

// + doppleganger "disfigurator"

varying vec3 position;
varying vec3 normal;

uniform float time;

float rand(vec2 co) {
   return (fract(sin(dot(co.xy, vec2(12.9898,78.233))) * 43758.5453) - 0.5) * 2.0;
}

void main() {
	vec4 vert = gl_Vertex;
    float seed = rand(vec2(time, time));

	float disfiguration = 0.01;
	
    vert.x = vert.x + rand(vec2(vert.y * seed, vert.z * seed)) * disfiguration;
    vert.y = vert.y + rand(vec2(vert.x * seed, vert.z * seed)) * disfiguration;
    vert.z = vert.z + rand(vec2(vert.x * seed, vert.y * seed)) * disfiguration;
    
    gl_Position = gl_ModelViewProjectionMatrix * vert;
	
    position = (gl_ModelViewMatrix * gl_Vertex).xyz;
    normal = normalize(gl_NormalMatrix * gl_Normal);
}