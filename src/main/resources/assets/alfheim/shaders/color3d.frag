#version 120

precision highp float;
precision highp int;

uniform sampler2D bgl_RenderedTexture;

uniform vec3 center;
varying vec3 vPos;

void main() {
	vec2 uv = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(bgl_RenderedTexture, uv);
	
	vec3 vol = abs(center - vPos);
	
	gl_FragColor = vec4(color.rgb * vol, 1.0);
}