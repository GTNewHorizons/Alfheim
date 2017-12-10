#version 120

uniform sampler2D bgl_RenderedTexture;
uniform float time;

void main() {
	float atd = 0.01; 
	float spd = 1.0;
	float prd = 0.05;
	float wdt = 0.055;

	vec2 xy = vec2(gl_TexCoord[0]);
	xy.x += 11.0;
	xy.y -= 12.0;
	vec4 col = vec4(0.0);

	float sft = sin((xy.x) / prd - (time * spd)) * atd;
	float w = sin((xy.y + sft) / wdt);
	float dw = -sin((xy.x - sft) / wdt);
	col.g = max(w, dw);

	col.b = 1.0 - col.g;
	col.g = min(max(col.g, col.b), col.b);
	col.r = (col.b *= 1.5) / 1.5;
	//col.a = 1.0; //texture2D(bgl_RenderedTexture, xy).a == 0.0 ? 0.0 : 1.0;
	col.a = (col.r+col.g+col.b) / 3.0;
	
	gl_FragColor = col; //vec4(col.rgb, texture2D(bgl_RenderedTexture, xy).a);
}