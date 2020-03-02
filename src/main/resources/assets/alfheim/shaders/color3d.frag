#version 120

precision highp float;
precision highp int;

uniform sampler2D bgl_RenderedTexture;

varying vec3 vPos;
varying vec2 texcoord;
uniform vec3 translation;

#define PI 3.1415926535897932384626433832795

// positive values only sin func
float psin(float f) {
	return sin(f - PI/2.0) / 2.0 + 0.5;
}

vec3 psin(vec3 v) {
	return vec3(psin(v.x), psin(v.y), psin(v.z));
}

void main() {
    vec4 color = texture2D(bgl_RenderedTexture, texcoord);
	
	vec3 vol = translation - vPos;
	vol = psin(vol / 100.0);
	
	gl_FragColor = vec4(color.rgb * vol, color.a);
}