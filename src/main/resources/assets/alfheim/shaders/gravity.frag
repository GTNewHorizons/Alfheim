#version 120

uniform float ftime;

void main() {
	vec2 uv = vec2(gl_TexCoord[0]);
	vec2 center = vec2(0.5, 0.5);

	float dist = distance(center, uv);
	float r = 0.01, g = 0.01, b = 0.01, a = 1.0;

	if (dist <= 0.05);								// BLACK
	else if (dist <= 0.4)							// BLACK -> WHITE
		r = g = b = min(1.0, (dist - 0.05) * 40.0);
	else if (dist <= 0.415) {						// WHITE -> YELLOW
		r = g = 1.0;
		b = (0.415 - dist) * (1.0 / 0.015);
	}
	else if (dist <= 0.445) {						// YELLOW -> ORANE
		r = 1.0;
		g = 0.5 + (0.445 - dist) * (0.5 / 0.03);
	}
	else if (dist <= 0.485) {						// ORANGE -> RED
		r = 1.0;
		g = (0.485 - dist) * (0.5 / 0.04);
	}
	else if (dist <= 0.495) {						// RED -> BLACK
		r = (0.495 - dist) * (1.0 / 0.01);
	}
	else if (dist <= 0.5) {							// BLACK -> OUTER
		a = (0.5 - dist) * (1.0 / 0.005);
	} else a = 0.0;

	float speed = -2.0;
	float fadeAway = 1.0;
	vec3 color = vec3(1.0);
	float uniformity = 10.0;

	float t = ftime * speed;
	vec2 position = uv - 0.5;
	float angle = atan(position.y, position.x) / (2. * 3.14159265359);
	angle -= floor(angle);
	float rad = length(position);
	float angleFract = fract(angle * 256.);
	float angleRnd = floor(angle * 256.) + 1.;
	float angleRnd1 = fract(angleRnd * fract(angleRnd * .7235) * 45.1);
	float angleRnd2 = fract(angleRnd * fract(angleRnd * .82657) * 13.724);
	float t2 = t + angleRnd1 * uniformity;
	float radDist = sqrt(angleRnd2);
	float adist = radDist / rad * .1;
	dist = (t2 * .1 + adist);
	dist = abs(fract(dist) - fadeAway);

	float outputColor = (1.0 / (dist)) * cos(0.7 * sin(t)) * adist / radDist / 30.0;
	angle = fract(angle + .61);
	vec3 parts = vec3(1.0) - vec3(1.0 - outputColor * color);
	if (outputColor == 0.0) parts = vec3(0.0);
	
	//float vave = (1.0 - (sin(time * 10.0 + distance(uv, center) * 128.0) * 0.65));
	gl_FragColor = vec4(vec3(r, g, b) - parts.rgb, a);
}