#version 120

uniform sampler2D bgl_RenderedTexture;

float clamp(float min, float f, float max) {
	if (f > max) f = max;
	if (f < min) f = min;
	return f;
}

bool isSolid(vec2 pos) {
	return texture2D(bgl_RenderedTexture, pos).a > 0.5;
}

bool around(vec2 pos, float rad) {
	for (float u = -rad; u <= rad; u++) {
		for (float v = -rad; v <= rad; v++) {
			float u1 = u / 512.0;
			float v1 = v / 512.0;
			float rad1 = rad / 512.0;
			if (distance(pos, vec2(clamp(0.0, pos.x + u1, 1.0), clamp(0.0, pos.y + v1, 1.0))) < rad1 && isSolid(vec2(clamp(0.0, pos.x + u1, 1.0), clamp(0.0, pos.y + v1, 1.0)))) return true;
		}
	}
	return false;
}

void main() {
	vec2 uv = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(bgl_RenderedTexture, uv);
	if (!isSolid(uv)){
		if (around(uv, 16.0)) {
			color.r = color.g = color.b = 0.0;
			color.a = 1.0;
		}
		gl_FragColor = color;
		return;
	}
	
	gl_FragColor = vec4(color.rgb, gl_Color.a);
}