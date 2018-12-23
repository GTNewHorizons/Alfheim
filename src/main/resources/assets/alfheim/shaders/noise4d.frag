#version 120

precision highp float;
precision highp int;

uniform float ftime;
float speed = 0.1;
varying vec3 vPos;

vec3 _sn_1_mod289(vec3 x) {
	return x - floor(x * (1.0 / 289.0)) * 289.0;
}

vec4 _sn_1_mod289(vec4 x) {
	return x - floor(x * (1.0 / 289.0)) * 289.0;
}

vec4 _sn_1_permute(vec4 x) {
	   return _sn_1_mod289(((x*34.0)+1.0)*x);
}

vec4 _sn_1_taylorInvSqrt(vec4 r)
{
	return 1.79284291400159 - 0.85373472095314 * r;
}

float _sn_1_snoise(vec3 v)
{
	const vec2  C = vec2(1.0/6.0, 1.0/3.0) ;
	const vec4  D = vec4(0.0, 0.5, 1.0, 2.0);

	
	vec3 i  = floor(v + dot(v, C.yyy) );
	vec3 x0 =   v - i + dot(i, C.xxx) ;

	
	vec3 g = step(x0.yzx, x0.xyz);
	vec3 l = 1.0 - g;
	vec3 i1 = min( g.xyz, l.zxy );
	vec3 i2 = max( g.xyz, l.zxy );

	vec3 x1 = x0 - i1 + C.xxx;
	vec3 x2 = x0 - i2 + C.yyy; 
	vec3 x3 = x0 - D.yyy;      

	
	i = _sn_1_mod289(i);
	vec4 p = _sn_1_permute( _sn_1_permute( _sn_1_permute(
	           i.z + vec4(0.0, i1.z, i2.z, 1.0 ))
	         + i.y + vec4(0.0, i1.y, i2.y, 1.0 ))
	         + i.x + vec4(0.0, i1.x, i2.x, 1.0 ));

	
	
	float n_ = 0.142857142857; 
	vec3  ns = n_ * D.wyz - D.xzx;

	vec4 j = p - 49.0 * floor(p * ns.z * ns.z);  

	vec4 x_ = floor(j * ns.z);
	vec4 y_ = floor(j - 7.0 * x_ );    

	vec4 x = x_ *ns.x + ns.yyyy;
	vec4 y = y_ *ns.x + ns.yyyy;
	vec4 h = 1.0 - abs(x) - abs(y);

	vec4 b0 = vec4( x.xy, y.xy );
	vec4 b1 = vec4( x.zw, y.zw );

	
	
	vec4 s0 = floor(b0)*2.0 + 1.0;
	vec4 s1 = floor(b1)*2.0 + 1.0;
	vec4 sh = -step(h, vec4(0.0));

	vec4 a0 = b0.xzyw + s0.xzyw*sh.xxyy ;
	vec4 a1 = b1.xzyw + s1.xzyw*sh.zzww ;

	vec3 p0 = vec3(a0.xy,h.x);
	vec3 p1 = vec3(a0.zw,h.y);
	vec3 p2 = vec3(a1.xy,h.z);
	vec3 p3 = vec3(a1.zw,h.w);

	
	vec4 norm = _sn_1_taylorInvSqrt(vec4(dot(p0,p0), dot(p1,p1), dot(p2, p2), dot(p3,p3)));
	p0 *= norm.x;
	p1 *= norm.y;
	p2 *= norm.z;
	p3 *= norm.w;

	
	vec4 m = max(0.6 - vec4(dot(x0,x0), dot(x1,x1), dot(x2,x2), dot(x3,x3)), 0.0);
	m = m * m;
	return 42.0 * dot( m*m, vec4( dot(p0,x0), dot(p1,x1),
	                              dot(p2,x2), dot(p3,x3) ) );
}

float _sn_3_octive(vec3 loc){
	float ret = _sn_1_snoise(loc);
	ret += _sn_1_snoise(loc*2.0)*0.5;
	ret += _sn_1_snoise(loc*4.0)*0.25;
	ret += _sn_1_snoise(loc*8.0)*0.125;
	ret += _sn_1_snoise(loc*16.0)*0.0625;
	return ret;
}


vec3 _sn_4_floatToColor(float v){
	  return abs(vec3(v));
}

void main() {
	vec3 _io_4_return = vPos / 10.0 + ftime * speed;
	float _io_5_return = _sn_3_octive(_io_4_return);
	vec3 io8 = _sn_4_floatToColor(_io_5_return);
	gl_FragColor = vec4(io8.r, 0.0, 0.0, 1.0);
}