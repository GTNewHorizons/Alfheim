#version 120

uniform float time;
uniform sampler2D image;
uniform sampler2D mask;

vec2 rotate(vec2 v, float a) {
	//a = radians(a);
	float x = v.x * cos(a) - v.y * sin(a);
	float y = v.y * cos(a) + v.x * sin(a);
	return vec2(x, y);
}

void main(void) {
	vec2 uv = vec2(gl_TexCoord[0]);
	if (length(uv-0.5) > 0.5) {
		gl_FragColor = vec4(0.0);
		return;
	}
	
	gl_FragColor = texture2D(image, uv) * texture2D(mask, rotate(uv - 0.5, -time)).r;
}