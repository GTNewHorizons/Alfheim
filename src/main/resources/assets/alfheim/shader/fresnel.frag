#version 120
#define TAU 6.28318530718
#define MAX_ITER 5

precision highp float;
precision highp int;

uniform float time;

varying vec3 position;
varying vec3 normal;

void main() {
	vec4 cau = vec4(0.0);
	
	{ // "Tiling Caustic" by line0	-	shaderfrog.com/app/view/329
		vec2 uv = position.xy; //vec2(gl_TexCoord[0]);
		float speed = 0.25;
		vec3 backgroundColor = vec3(0.0);
		vec3 color = vec3(1.0, 0.5, 0.0);
		float brightness = 2.0;
		
		vec2 p = mod(uv * TAU, TAU) - 250.0;
		vec2 i = vec2(p);
		
		float c = 1.0;
		float inten = 0.005;
		
		for ( int n = 0; n < MAX_ITER; n++ )  {
			float t = time * speed * (1.0 - (3.5 / float(n + 1)));
			i = p + vec2(cos(t - i.x) + sin(t + i.y), sin(t - i.y) + cos(t + i.x));
			c += 1.0 / length(vec2(p.x / (sin(i.x + t) / inten), p.y / (cos(i.y + t) / inten)));
		}
		
		c /= float( MAX_ITER  );
		c = 1.17 - pow(c, brightness);
	   
		vec3 res = vec3(vec3(pow(abs(c), 8.0)) * color + backgroundColor);
		
		cau = vec4(res, (res.r + res.g) / 4.0);
		cau.g /= 8.0;
	}
	
    vec4 rimColor = vec4(1.0, 0.25, 0.0, 1.0);
    float scale = 1.0;
    vec3 ray = normalize(position + position);
    float refFactor = max(0.0, min(1.0, scale * pow(1.0 + dot(ray, normal), 1.4)));
	vec4 rez = mix(vec4(1.0, 0.75, 0.0, 0.0), rimColor, refFactor);
	
    gl_FragColor = cau + rez;
}