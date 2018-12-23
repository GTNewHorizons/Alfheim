#version 120

uniform sampler2D bgl_RenderedTexture; 

void main() {
	vec2 uv = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(bgl_RenderedTexture, uv);

	color.r = 1.0;
	gl_FragColor = color;
}